package com.omomdevs.side.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**Defines what an entity see/where it looks
 *
 * Created by omaro on 14/11/2015.
 */
public class ViewSightComponent extends Component {
    public Vector2 direction;
    public Circle area;
    public int radius;
    public float angleDegrees;

    public ViewSightComponent(int radius,float angleDegrees) {
        this.angleDegrees=angleDegrees;
        this.radius = radius;
        direction=new Vector2(0,0);
        area=new Circle(0,0,radius);
    }

    public ViewSightComponent(Vector2 direction, int radius, float angleDegrees) {
        this.direction = direction;
        this.radius = radius;
        this.angleDegrees = angleDegrees;
        area=new Circle(0,0,radius);
    }

    public ViewSightComponent() {
    }
}
