package com.omomdevs.side.managers;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kotcrab.vis.runtime.component.PhysicsComponent;
import com.kotcrab.vis.runtime.component.SpriteComponent;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by omaro on 10/11/2015.
 */
@Wire
public class LogicManager extends Manager implements AfterSceneInit {
    VisIDManager idManager;
    PhysicsSystem physicsSystem;
    Entity door;

    @Override
    public void afterSceneInit() {
        door=idManager.get("door");
        SpriteComponent sprite=door.getComponent(SpriteComponent.class);
        BodyDef def=new BodyDef();
        def.type= BodyDef.BodyType.StaticBody;
        def.position.set(sprite.getX(),sprite.getY());
        Body body=physicsSystem.getPhysicsWorld().createBody(def);
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2,sprite.getHeight()/2);
        body.createFixture(shape,1);
        shape.dispose();
        door.edit().add(new PhysicsComponent(body));

    }
}
