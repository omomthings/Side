package com.omomdevs.side.managers;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.component.VariablesComponent;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.components.AIComponent;
import com.omomdevs.side.components.HealthComponent;
import com.omomdevs.side.components.MeleeAttackComponent;
import com.omomdevs.side.components.ShootComponent;
import com.omomdevs.side.components.ViewSightComponent;

/**Spawn Enemies after each init()
 * Created by omaro on 21/11/2015.
 */

@Wire
public class EnemySpawner extends Manager implements AfterSceneInit
{
    private final int ENEMY_HEALTH=50;
    VisIDManager idManager;

    Array<Integer> createdEntites=new Array<>();

    @Override
    public void afterSceneInit() {
        Array<Entity> entities=idManager.getMultiple("Enemy");
        for (Entity entity:entities){
            if (!createdEntites.contains(entity.getId(),true)){
                createEnemy(entity);
            }
        }
    }

    private void createEnemy (Entity entity){
        createdEntites.add(entity.getId());
        VariablesComponent variablesComponent=entity.getComponent(VariablesComponent.class);
        ViewSightComponent viewSight=new ViewSightComponent(variablesComponent.getInt("radius"),variablesComponent.getFloat("angle degrees"));
        entity.edit().add(new AIComponent())
                .add(new HealthComponent(ENEMY_HEALTH))
                .add(viewSight).getEntity();
        if (variablesComponent.getBoolean("melee attack"))
            entity.edit().add(new MeleeAttackComponent());
        else
            entity.edit().add(new ShootComponent());

    }
}
