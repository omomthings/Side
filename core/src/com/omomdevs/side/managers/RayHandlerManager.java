package com.omomdevs.side.managers;

import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import box2dLight.RayHandler;


@Wire
public class RayHandlerManager extends Manager implements AfterSceneInit {
    PhysicsSystem physicsSystem;
    private RayHandler rayHandler;
    private boolean notInit=true;

    @Override
    public void afterSceneInit() {
        if (notInit) {
            notInit=false;
            rayHandler = new RayHandler(physicsSystem.getPhysicsWorld());
        }
    }


    public RayHandler getRayHandler() {
        return rayHandler;
    }

    @Override
    protected void dispose() {
        rayHandler.dispose();
    }
}
