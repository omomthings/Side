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


public class GameScreen implements Screen{
    //TODO this

    public GameScreen (Side side){
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

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
    }
}
