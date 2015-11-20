package com.omomdevs.side.utils;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.assets.AtlasRegionAsset;
import com.kotcrab.vis.runtime.assets.BmpFontAsset;
import com.kotcrab.vis.runtime.assets.PathAsset;
import com.kotcrab.vis.runtime.assets.ShaderAsset;
import com.kotcrab.vis.runtime.assets.TextureRegionAsset;
import com.kotcrab.vis.runtime.assets.TtfFontAsset;
import com.kotcrab.vis.runtime.assets.VisAssetDescriptor;
import com.kotcrab.vis.runtime.component.AssetComponent;
import com.kotcrab.vis.runtime.component.ShaderProtoComponent;
import com.kotcrab.vis.runtime.data.EntityData;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.font.FontProvider;
import com.kotcrab.vis.runtime.plugin.EntitySupport;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.util.ImmutableArray;
import com.kotcrab.vis.runtime.util.SpriterData;

/**Manual scene loader , just create data, dont create scene, you need to instanciate scene manually
 * Created by omaro on 16/11/2015.
 */
public class SimpleSceneLoader extends SceneLoader {
    RuntimeContext context;
    SceneData sceneData;
    SceneParameter parameter;
    RuntimeConfiguration configuration;
    Scene scene;

    boolean disctanceFieldShaderLoaded=false;
    FontProvider bmpFontProvider;
    FontProvider ttfFontProvider;
    Batch batch;
    private Array<EntitySupport> supports=new Array<EntitySupport>();

    @Override
    public void registerSupport(AssetManager manager,EntitySupport support){
        supports.add(support);
        support.setLoaders(manager);
        super.registerSupport(manager,support);
    }

    public SimpleSceneLoader() {
        super();
        configuration=new RuntimeConfiguration();
    }

    public SimpleSceneLoader(RuntimeConfiguration configuration) {
        super(configuration);
        this.configuration=configuration;
    }

    public SimpleSceneLoader(FileHandleResolver resolver, RuntimeConfiguration configuration) {
        super(resolver, configuration);
        this.configuration=configuration;
    }

    @Override
    public void setBatch(Batch batch){
        this.batch=batch;
        super.setBatch(batch);
    }


    public Scene createEmptyScene(){
        if (scene==null)
            this.scene=new Scene(context,sceneData,parameter);
        return scene;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SceneParameter parameter) {
        context=new RuntimeContext(configuration,batch,manager,new ImmutableArray<EntitySupport>(supports));
        this.parameter=parameter;
        scene=new Scene(context,sceneData,parameter);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SceneParameter parameter) {
        if(batch==null) throw new IllegalStateException("Batch not set, see #setBatch()");

        Json json=getJson();
        sceneData=json.fromJson(SceneData.class,file);

        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
        loadDependency(dependencies, sceneData.entities);
        return dependencies;
    }

    private void loadDependency(Array<AssetDescriptor> dependencies,Array<EntityData> entities){
        for (EntityData entityData : entities) {
            for (Component component : entityData.components) {
                if (component instanceof AssetComponent) {
                    VisAssetDescriptor asset = ((AssetComponent) component).asset;

                    if (asset instanceof TextureRegionAsset) {
                        dependencies.add(new AssetDescriptor<TextureAtlas>("gfx/textures.atlas", TextureAtlas.class));

                    } else if (asset instanceof AtlasRegionAsset) {
                        AtlasRegionAsset regionAsset = (AtlasRegionAsset) asset;
                        dependencies.add(new AssetDescriptor<TextureAtlas>(regionAsset.getPath(), TextureAtlas.class));

                    } else if (asset instanceof BmpFontAsset) {
                        checkShaderLoaded(dependencies);
                        bmpFontProvider.load(dependencies, asset);
                    } else if (asset instanceof TtfFontAsset) {
                        ttfFontProvider.load(dependencies, asset);
                    } else if (asset instanceof PathAsset) {
                        PathAsset pathAsset = (PathAsset) asset;
                        String path = pathAsset.getPath();

                        if (path.startsWith("sound/")) dependencies.add(new AssetDescriptor<Sound>(path, Sound.class));
                        if (path.startsWith("music/")) dependencies.add(new AssetDescriptor<Music>(path, Music.class));
                        if (path.startsWith("particle/"))
                            dependencies.add(new AssetDescriptor<ParticleEffect>(path, ParticleEffect.class));
                        if (path.startsWith("spriter/"))
                            dependencies.add(new AssetDescriptor<SpriterData>(path, SpriterData.class));
                    }
                }

                if (component instanceof ShaderProtoComponent) {
                    ShaderProtoComponent shaderComponent = (ShaderProtoComponent) component;
                    ShaderAsset asset = shaderComponent.asset;
                    if (asset != null) {
                        String path = asset.getFragPath().substring(0, asset.getFragPath().length() - 5);
                        dependencies.add(new AssetDescriptor<ShaderProgram>(path, ShaderProgram.class));
                    }
                }

                for (EntitySupport support : supports)
                    support.resolveDependencies(dependencies, entityData, component);
            }
        }
    }

    private void checkShaderLoaded(Array<AssetDescriptor> dependency) {
        if (disctanceFieldShaderLoaded == false) {
            dependency.add(new AssetDescriptor<ShaderProgram>(Gdx.files.classpath(DISTANCE_FIELD_SHADER), ShaderProgram.class));
        }
        disctanceFieldShaderLoaded = true;
    }
}
