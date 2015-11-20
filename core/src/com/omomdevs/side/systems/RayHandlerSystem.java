package com.omomdevs.side.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.SpriteRenderSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.managers.RayHandlerManager;



@Wire
public class RayHandlerSystem extends BaseSystem  {
    CameraManager cameraManager;
    RayHandlerManager rayHandlerManager;


    @Override
    protected void processSystem() {
        rayHandlerManager.getRayHandler().setCombinedMatrix(cameraManager.getCamera());
        rayHandlerManager.getRayHandler().updateAndRender();
    }


}
