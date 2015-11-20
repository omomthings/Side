package com.omomdevs.side.components;

import com.artemis.Component;

/**Two linked components e.g lever and door
 *
 * Created by omaro on 14/11/2015.
 */
public class LinkedComponent extends Component {
    public int linkedTo;

    public LinkedComponent(int linkedTo) {
        this.linkedTo = linkedTo;
    }
}
