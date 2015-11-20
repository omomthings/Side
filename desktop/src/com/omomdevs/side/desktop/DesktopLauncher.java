package com.omomdevs.side.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.omomdevs.side.Side;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height=480;
        config.width=800;
        config.resizable=false;
        new LwjglApplication(new Side(), config);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
