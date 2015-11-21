package com.omomdevs.side.components;

import com.artemis.Component;
import com.artemis.Entity;

/**Two linked components e.g lever and door
 *
 * Created by omaro on 14/11/2015.
 */
public class LinkedComponent extends Component {
    public Entity linkedTo;

    public LinkedComponent(Entity linkedTo) {
        this.linkedTo = linkedTo;
    }
}
