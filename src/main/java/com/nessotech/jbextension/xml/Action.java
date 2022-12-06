package com.nessotech.jbextension.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Map;

@XmlRootElement(name = "action")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"choices", "run", "cancel", "none"})
@Setter
@Getter
public class Action {

    private Map<String,String> choices;
    private String run;
    private String cancel;
    private String none;

}
