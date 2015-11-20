package com.omomdevs.side.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.components.PointLightComponent;
import com.omomdevs.side.managers.LightManager;


@Wire
public class CameraSystem extends BaseSystem implements AfterSceneInit {
    CameraManager cameraManager;
    LightManager lightManager;

    float width, height,zoom=1;


    @Override
    public void afterSceneInit() {
        zoom=1f/3f;

    }

    @Override
    protected void processSystem() {
        //Setting the zoom , height and width of the camera
        cameraManager.getCamera().zoom=zoom;
        width = cameraManager.getCamera().viewportWidth*zoom;
        height = cameraManager.getCamera().viewportHeight*zoom;

        Vector3 position = cameraManager.getCamera().position;
        Vector2 posLight = lightManager.getPointLight().getComponent(PointLightComponent.class).pointLight.getPosition();
        position.set(posLight.x, posLight.y, 0);

        //Setting position so the camera dont go out of bounds
/*
        if (position.x < width / 2)
            position.x = width / 2;
        if (position.x > cameraManager.getCamera().viewportWidth - width / 2)
            position.x = cameraManager.getCamera().viewportWidth - width / 2;
        if (position.y < height / 2)
            position.y = height / 2;
        if (position.y > cameraManager.getCamera().viewportHeight - height / 2)
            position.y = cameraManager.getCamera().viewportHeight - height / 2;*/
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

}
