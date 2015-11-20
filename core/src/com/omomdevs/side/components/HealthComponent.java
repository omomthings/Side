package com.omomdevs.side.components;

import com.artemis.Component;

/**Health Component for destructible entities
 *
 * Created by omaro on 14/11/2015.
 */
public class HealthComponent extends Component {
    public int health;
    public int damage=0;

    public HealthComponent(int health) {
        this.health = health;
    }
}
