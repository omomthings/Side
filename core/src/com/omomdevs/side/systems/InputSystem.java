package com.omomdevs.side.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.omomdevs.side.components.MovableComponent;
import com.omomdevs.side.components.PlayerControledComponent;

/**Handles inputs
 * Created by omaro on 11/11/2015.
 */
public class InputSystem extends EntityProcessingSystem {


    public InputSystem() {
        super(Aspect.all(PlayerControledComponent.class));
    }

    @Override
    protected void process(Entity e) {

    }
}
