package com.nessotech.jbextension.xml;

import com.nessotech.jbextension.parser.FieldAction;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class Field {
    
    private Integer order;
    private String message;
    private String input;
    private TypeInput type;
    private Action action;

    public boolean hasInput() {
        return !StringUtils.isEmpty(this.input);
    }

}
