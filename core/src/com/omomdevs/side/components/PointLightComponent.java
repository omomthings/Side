package com.omomdevs.side.components;

import com.artemis.Component;
import com.artemis.annotations.Transient;

import box2dLight.PointLight;

@Transient
public class PointLightComponent extends Component {

    public PointLight pointLight;

    public PointLightComponent(){pointLight=null;}
    public PointLightComponent(PointLight point) {
        this.pointLight = point;
    }

}
