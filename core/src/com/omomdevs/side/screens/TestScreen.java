package com.omomdevs.side.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.font.FreeTypeFontProvider;
import com.omomdevs.side.Side;
import com.omomdevs.side.managers.LightManager;
import com.omomdevs.side.managers.RayHandlerManager;
import com.omomdevs.side.managers.UtilsManager;
import com.omomdevs.side.systems.CameraSystem;
import com.omomdevs.side.systems.LightSystem;
import com.omomdevs.side.systems.RayHandlerSystem;
import com.omomdevs.side.utils.AdvancedVisAssetManager;
import com.omomdevs.side.utils.EmptyScene;
import com.omomdevs.side.utils.Loader;


public class TestScreen implements Screen{

    final Side side;
    EmptyScene scene;
    SpriteBatch batch;
    AdvancedVisAssetManager advancedVisAssetManager;



    public TestScreen (Side side){
        this.side=side;
        batch=new SpriteBatch();
        advancedVisAssetManager=new AdvancedVisAssetManager("scene/lvlTest",1,batch);
    }
    @Override
    public void show() {
        Loader.EmptySceneParameter parameter=new Loader.EmptySceneParameter();
        parameter.systems.addAll(new RayHandlerSystem(),new LightSystem(),new CameraSystem());
        parameter.managers.addAll(new UtilsManager(),new RayHandlerManager(),new LightManager());

        RuntimeConfiguration configuration=new RuntimeConfiguration();
        configuration.useBox2dDebugRenderer=true;
        advancedVisAssetManager.getSceneLoader().setRuntimeConfig(configuration);

        advancedVisAssetManager.enableFreeType(new FreeTypeFontProvider());

        scene=advancedVisAssetManager.loadNow(parameter);
    }

    long t;
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            t = TimeUtils.millis();
            advancedVisAssetManager.loadPreviousScene();
            Gdx.app.debug("prev time", TimeUtils.timeSinceMillis(t) + "ms");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            t = TimeUtils.millis();
            advancedVisAssetManager.loadNextScene();
            Gdx.app.debug("next time", TimeUtils.timeSinceMillis(t) + "ms");
        }
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        batch.dispose();
        if (advancedVisAssetManager!=null)
            advancedVisAssetManager.dispose();
        Gdx.app.log("GameScreen","dispose()");
    }
}
