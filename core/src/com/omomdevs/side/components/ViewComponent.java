package com.omomdevs.side.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**Defines what an entity see/where it looks
 *
 * Created by omaro on 14/11/2015.
 */
public class ViewComponent extends Component {
    public Vector2 direction;
    public Circle circle;
    public int radius;
    public float angleDegrees;

    public ViewComponent(int radius,float angleDegrees) {
        this.angleDegrees=angleDegrees;
        this.radius = radius;
        direction=new Vector2(0,0);
        circle=new Circle(0,0,radius);
    }

    public ViewComponent(Vector2 direction, int radius, float angleDegrees) {
        this.direction = direction;
        this.radius = radius;
        this.angleDegrees = angleDegrees;
        circle=new Circle(0,0,radius);
    }

    public ViewComponent() {
    }
}
