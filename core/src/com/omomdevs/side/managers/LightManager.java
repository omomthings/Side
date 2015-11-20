package com.omomdevs.side.managers;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.kotcrab.vis.runtime.accessor.BasicPropertiesAccessor;
import com.kotcrab.vis.runtime.component.LayerComponent;
import com.kotcrab.vis.runtime.component.PointComponent;
import com.kotcrab.vis.runtime.component.RenderableComponent;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.components.PointLightComponent;

import box2dLight.PointLight;


@Wire
public class LightManager extends Manager implements AfterSceneInit,BasicPropertiesAccessor {
    RayHandlerManager rayHandlerManager;

    final float LIGHT_DISTANCE=20f;
    final int NUM_RAYS=128;

    private Entity pointLight;
    private boolean notInit=true;

    @Override
    public void afterSceneInit() {
        if (notInit) {
            notInit=false;
            Entity entity = world.getManager(VisIDManager.class).get("start");
            pointLight=world.createEntity();
            pointLight.edit().add(new RenderableComponent(0))
                    .add(new LayerComponent(0))
                    .add(new PointLightComponent(new PointLight(rayHandlerManager.getRayHandler(), NUM_RAYS, Color.WHITE, LIGHT_DISTANCE, entity.getComponent(PointComponent.class).x, entity.getComponent(PointComponent.class).y)));
        }

    }

    public Entity getPointLight() {
        return pointLight;
    }

    @Override
    public float getX() {
        return pointLight.getComponent(PointLightComponent.class).pointLight.getX();
    }

    @Override
    public void setX(float x) {
        pointLight.getComponent(PointLightComponent.class).pointLight.setPosition(x,pointLight.getComponent(PointLightComponent.class).pointLight.getY());
    }

    @Override
    public float getY() {
        return pointLight.getComponent(PointLightComponent.class).pointLight.getY();
    }

    @Override
    public void setY(float y) {
        pointLight.getComponent(PointLightComponent.class).pointLight.setPosition(pointLight.getComponent(PointLightComponent.class).pointLight.getX(),y);
    }

    @Override
    public void setPosition(float x, float y) {
        pointLight.getComponent(PointLightComponent.class).pointLight.setPosition(x,y);
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return null;
    }
}
