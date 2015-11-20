package com.omomdevs.side.systems;


import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.runtime.component.ParticleComponent;
import com.kotcrab.vis.runtime.component.PositionComponent;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.components.PointLightComponent;
import com.omomdevs.side.managers.LightManager;

import box2dLight.PointLight;

@Wire
public class LightSystem extends BaseSystem {
    LightManager lightManager;
    VisIDManager idManager;
    final private float SPEED=1f;


    @Override
    protected void processSystem() {
        //Managing move (TEST PURPOSE ONLY)
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            lightManager.setY(lightManager.getY()+SPEED);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            lightManager.setY(lightManager.getY()-SPEED);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            lightManager.setX(lightManager.getX() + SPEED);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            lightManager.setX(lightManager.getX() - SPEED);
    }
}
