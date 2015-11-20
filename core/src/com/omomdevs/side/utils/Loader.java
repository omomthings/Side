package com.omomdevs.side.utils;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.assets.AtlasRegionAsset;
import com.kotcrab.vis.runtime.assets.BmpFontAsset;
import com.kotcrab.vis.runtime.assets.PathAsset;
import com.kotcrab.vis.runtime.assets.ShaderAsset;
import com.kotcrab.vis.runtime.assets.SpriterAsset;
import com.kotcrab.vis.runtime.assets.TextureRegionAsset;
import com.kotcrab.vis.runtime.assets.TtfFontAsset;
import com.kotcrab.vis.runtime.assets.VisAssetDescriptor;
import com.kotcrab.vis.runtime.component.AssetComponent;
import com.kotcrab.vis.runtime.component.GroupComponent;
import com.kotcrab.vis.runtime.component.IDComponent;
import com.kotcrab.vis.runtime.component.InvisibleComponent;
import com.kotcrab.vis.runtime.component.LayerComponent;
import com.kotcrab.vis.runtime.component.MusicProtoComponent;
import com.kotcrab.vis.runtime.component.ParticleProtoComponent;
import com.kotcrab.vis.runtime.component.PhysicsPropertiesComponent;
import com.kotcrab.vis.runtime.component.PolygonComponent;
import com.kotcrab.vis.runtime.component.RenderableComponent;
import com.kotcrab.vis.runtime.component.ShaderProtoComponent;
import com.kotcrab.vis.runtime.component.SoundProtoComponent;
import com.kotcrab.vis.runtime.component.SpriteProtoComponent;
import com.kotcrab.vis.runtime.component.SpriterProtoComponent;
import com.kotcrab.vis.runtime.component.TextProtoComponent;
import com.kotcrab.vis.runtime.component.VariablesComponent;
import com.kotcrab.vis.runtime.data.EntityData;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.font.BitmapFontProvider;
import com.kotcrab.vis.runtime.font.FontProvider;
import com.kotcrab.vis.runtime.plugin.EntitySupport;
import com.kotcrab.vis.runtime.scene.IntMapJsonSerializer;
import com.kotcrab.vis.runtime.scene.LayerCordsSystem;
import com.kotcrab.vis.runtime.scene.SceneViewport;
import com.kotcrab.vis.runtime.util.EntityEngine;
import com.kotcrab.vis.runtime.util.ImmutableArray;
import com.kotcrab.vis.runtime.util.SpriterData;

/**
 * Created by omaro on 16/11/2015.
 */
public class Loader extends AsynchronousAssetLoader<EmptyScene,Loader.EmptySceneParameter> {
    public static final String DISTANCE_FIELD_SHADER = "com/kotcrab/vis/runtime/bmp-font-df";

    RuntimeConfiguration configuration;
    Batch batch;
    Array <EntitySupport> supports=new Array<>();
    EmptyScene scene;
    SceneData data;
    private FontProvider bmpFontProvider;
    private FontProvider ttfFontProvider;
    private  boolean distanceFieldShaderLoaded;

    public void setBatch(Batch batch) {
        this.batch=batch;
    }

    public void registerSupport(AssetManager manager, EntitySupport support) {
        supports.add(support);
        support.setLoaders(manager);
    }

    public Loader(Batch batch) {
        this(batch, new InternalFileHandleResolver(), new RuntimeConfiguration());
    }

    public Loader(Batch batch,RuntimeConfiguration configuration) {
        this(batch, new InternalFileHandleResolver(),configuration );
    }

    public Loader(Batch batch,FileHandleResolver resolver, RuntimeConfiguration configuration) {
        super(resolver);
        setBatch(batch);
        this.configuration=configuration;
        bmpFontProvider=new BitmapFontProvider();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EmptySceneParameter parameter) {
        RuntimeContext context=new RuntimeContext(configuration,batch,manager,new ImmutableArray<>(supports));
        scene =new EmptyScene(context,data,parameter);
    }

    @Override
    public EmptyScene loadSync(AssetManager manager, String fileName, FileHandle file, EmptySceneParameter parameter) {
        EmptyScene s=scene;
        scene=null;
        return s;
    }


    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, EmptySceneParameter parameter) {
        if (batch == null) throw new IllegalStateException("Batch not set, see #setBatch(Batch)");

        Json json = getJson();
        data = json.fromJson(SceneData.class, file);

        Array<AssetDescriptor> dependencies = new Array<>();
        loadDependencies(dependencies, data.entities);
        return dependencies;
    }

    private void loadDependencies (Array<AssetDescriptor> dependencies, Array<EntityData> entities) {
        for (EntityData entityData : entities) {
            for (Component component : entityData.components) {
                if (component instanceof AssetComponent) {
                    VisAssetDescriptor asset = ((AssetComponent) component).asset;

                    if (asset instanceof TextureRegionAsset) {
                        dependencies.add(new AssetDescriptor<>("gfx/textures.atlas", TextureAtlas.class));

                    } else if (asset instanceof AtlasRegionAsset) {
                        AtlasRegionAsset regionAsset = (AtlasRegionAsset) asset;
                        dependencies.add(new AssetDescriptor<>(regionAsset.getPath(), TextureAtlas.class));

                    } else if (asset instanceof BmpFontAsset) {
                        checkShader(dependencies);
                        bmpFontProvider.load(dependencies, asset);
                    } else if (asset instanceof TtfFontAsset) {
                        ttfFontProvider.load(dependencies, asset);
                    } else if (asset instanceof PathAsset) {
                        PathAsset pathAsset = (PathAsset) asset;
                        String path = pathAsset.getPath();

                        if (path.startsWith("sound/")) dependencies.add(new AssetDescriptor<>(path, Sound.class));
                        if (path.startsWith("music/")) dependencies.add(new AssetDescriptor<>(path, Music.class));
                        if (path.startsWith("particle/"))
                            dependencies.add(new AssetDescriptor<>(path, ParticleEffect.class));
                        if (path.startsWith("spriter/"))
                            dependencies.add(new AssetDescriptor<>(path, SpriterData.class));
                    }
                }

                if (component instanceof ShaderProtoComponent) {
                    ShaderProtoComponent shaderComponent = (ShaderProtoComponent) component;
                    ShaderAsset asset = shaderComponent.asset;
                    if (asset != null) {
                        String path = asset.getFragPath().substring(0, asset.getFragPath().length() - 5);
                        dependencies.add(new AssetDescriptor<>(path, ShaderProgram.class));
                    }
                }

                for (EntitySupport support : supports)
                    support.resolveDependencies(dependencies, entityData, component);
            }
        }
    }

    public void enableFreeType (AssetManager manager, FontProvider fontProvider) {
        this.ttfFontProvider = fontProvider;
        fontProvider.setLoaders(manager);
    }

    private void checkShader (Array<AssetDescriptor> dependencies) {
        if (!distanceFieldShaderLoaded)
            dependencies.add(new AssetDescriptor<>(Gdx.files.classpath(DISTANCE_FIELD_SHADER), ShaderProgram.class));

        distanceFieldShaderLoaded = true;
    }

    public static Json getJson () {
        Json json = new Json();
        json.addClassTag("SceneData", SceneData.class);
        json.addClassTag("SceneViewport", SceneViewport.class);
        json.addClassTag("LayerCordsSystem", LayerCordsSystem.class);

        json.addClassTag("PathAsset", PathAsset.class);
        json.addClassTag("BmpFontAsset", BmpFontAsset.class);
        json.addClassTag("TtfFontAsset", TtfFontAsset.class);
        json.addClassTag("AtlasRegionAsset", AtlasRegionAsset.class);
        json.addClassTag("TextureRegionAsset", TextureRegionAsset.class);
        json.addClassTag("ShaderAsset", ShaderAsset.class);
        json.addClassTag("SpriterAsset", SpriterAsset.class);

        json.addClassTag("AssetComponent", AssetComponent.class);
        json.addClassTag("GroupComponent", GroupComponent.class);
        json.addClassTag("IDComponent", IDComponent.class);
        json.addClassTag("InvisibleComponent", InvisibleComponent.class);
        json.addClassTag("LayerComponent", LayerComponent.class);
        json.addClassTag("RenderableComponent", RenderableComponent.class);
        json.addClassTag("VariablesComponent", VariablesComponent.class);
        json.addClassTag("PhysicsPropertiesComponent", PhysicsPropertiesComponent.class);
        json.addClassTag("PolygonComponent", PolygonComponent.class);

        json.addClassTag("SpriteProtoComponent", SpriteProtoComponent.class);
        json.addClassTag("MusicProtoComponent", MusicProtoComponent.class);
        json.addClassTag("SoundProtoComponent", SoundProtoComponent.class);
        json.addClassTag("ParticleProtoComponent", ParticleProtoComponent.class);
        json.addClassTag("TextProtoComponent", TextProtoComponent.class);
        json.addClassTag("ShaderProtoComponent", ShaderProtoComponent.class);
        json.addClassTag("SpriterProtoComponent", SpriterProtoComponent.class);

        json.setSerializer(IntMap.class, new IntMapJsonSerializer());

        return json;
    }

    public void setRuntimeConfig (RuntimeConfiguration configuration) {
        this.configuration = configuration;
    }


    /** Allows to add additional system and managers into {@link EntityEngine} */
    static public class EmptySceneParameter extends AssetLoaderParameters<EmptyScene>{
        public Array<BaseSystem> systems = new Array<BaseSystem>();
        public Array<BaseSystem> passiveSystems = new Array<BaseSystem>();
        public Array<Manager> managers = new Array<Manager>();
    }
}
