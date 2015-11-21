package com.omomdevs.side.components;

import com.artemis.Component;
import com.artemis.Entity;

/**Entity will follow the target Entity with an absolute speed or with a relative speed
 * Created by omaro on 20/11/2015.
 */
public class FollowComponent extends Component {
    public Entity target;
    public float speed;


    /**@param target The Entity to follow
     * @param speed the speed of following, for absolute speed pass 0
     */
    public FollowComponent(Entity target,float speed){
        this.target=target;
        this.speed=speed;
    }

    public FollowComponent(){target=null;}
}
