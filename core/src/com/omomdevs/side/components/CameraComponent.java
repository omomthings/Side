package com.omomdevs.side.components;

import com.artemis.Component;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.system.CameraManager;

/**Camera Component
 * Created by omaro on 21/11/2015.
 */

@Wire
public class CameraComponent extends Component {
    public OrthographicCamera camera;
    CameraManager cameraManager;

    public CameraComponent(){
        camera=cameraManager.getCamera();
    }
}
