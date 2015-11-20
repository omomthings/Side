package com.omomdevs.side.managers;

import com.artemis.Manager;
import com.artemis.SystemInvocationStrategy;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.system.SpriteRenderSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.omomdevs.side.systems.CameraSystem;
import com.omomdevs.side.systems.RayHandlerSystem;


@Wire
public class UtilsManager extends Manager  implements AfterSceneInit{
    RayHandlerSystem rayHandlerSystem;
    CameraSystem cameraSystem;
    private boolean rayHandlerReordered=false;
    private boolean invocationStrategyChanged=false;

    @Override
    public void afterSceneInit() {
        enabling();
        if (!rayHandlerReordered)
            replaceRayHandlerSystem();
        if (!invocationStrategyChanged)
            changeInvocationStrategy();

    }

    void enabling(){
        rayHandlerSystem.setEnabled(false);
        cameraSystem.setEnabled(true);
        cameraSystem.setZoom(1);
    }

    private void changeInvocationStrategy(){
        SystemInvocationStrategy strategy= world.getInvocationStrategy();
    }

    private void replaceRayHandlerSystem(){
        rayHandlerReordered=true;
        //place RayHandlerSystem to render AFTER SpriteRendererSystem
        Object[] systems=((Bag) world.getSystems()).getData();
        int rayHandlerPos=-1,spritePos=-1;
        for (int i=0;i<systems.length && (rayHandlerPos<0 || spritePos<0);i++) {
            if (systems[i] instanceof RayHandlerSystem) {
                Gdx.app.log("RayHandlerSystem", "RayHandlerSystem found in systems, placing");
                rayHandlerPos = i;
            }else if (systems[i] instanceof SpriteRenderSystem){
                spritePos=i;
            }
        }
        if (rayHandlerPos<0){
            Gdx.app.error("RayHandlerSystem","could not find RayHandlerSystem in systems, shadows and light won't apear, make sure to first add RayHandlerSystem to parameter systems");
            return;
        }
        Object lightSystem=systems[rayHandlerPos];
        Object rayHandlerObject=systems[rayHandlerPos];
        for(int i=rayHandlerPos;i<=spritePos;i++){
            systems[i]=systems[i+1];
        }
        systems[spritePos]=rayHandlerObject;
    }

}
