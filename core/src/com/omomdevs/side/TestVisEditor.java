package com.omomdevs.side;

import com.artemis.BaseSystem;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;


public class TestVisEditor implements Screen {

    VisAssetManager assetManager;
    SpriteBatch batch;
    Scene scene;

    @Override
    public void show() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch=new SpriteBatch();
        assetManager=new VisAssetManager(batch);
        SceneLoader.SceneParameter parameter=new SceneLoader.SceneParameter();
        parameter.systems.add(new CamManager());
        scene=assetManager.loadSceneNow("scene/lvlTest.scene",parameter);
    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width,height);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scene.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }


@Wire
    class CamManager extends BaseSystem implements AfterSceneInit{
    CameraManager cameraManager;
    float width,height;

    @Override
    public void afterSceneInit() {
        float z=cameraManager.getCamera().zoom=1/3f;
        width=cameraManager.getCamera().viewportWidth*z;
        height=cameraManager.getCamera().viewportHeight*z;
        cameraManager.getCamera().position.set(width/2,height/2,0);
        Gdx.app.log("after cam","done ");
    }

    @Override
    protected void processSystem() {
        Vector3 v=cameraManager.getCamera().position;
        float d=0.1f;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            v.x+=d;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            v.x-=d;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            v.y+=d;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            v.y-=d;
        //Setting position so the camera dont go out of bounds
        Vector3 position=cameraManager.getCamera().position;
        if(position.x<width/2)
            position.x=width/2;
        if(position.x>cameraManager.getCamera().viewportWidth-width/2)
            position.x=cameraManager.getCamera().viewportWidth-width/2;
        if(position.y<height/2)
            position.y=height/2;
        if(position.y>cameraManager.getCamera().viewportHeight-height/2)
            position.y=cameraManager.getCamera().viewportHeight-height/2;

    }
}
}
