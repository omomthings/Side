package com.omomdevs.side.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.omomdevs.side.Side;


public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer =false;
        config.useCompass=false;
        config.useGLSurfaceView20API18=true;
        initialize(new Side(), config);
        Gdx.app.setLogLevel(LOG_DEBUG);
    }

}
