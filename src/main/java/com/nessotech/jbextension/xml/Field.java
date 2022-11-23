package com.nessotech.jbextension.xml;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Field {
    private Integer order;
    private String message;
    private String input;

    public Field() {

    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean hasInput() {
        return !StringUtils.isEmpty(this.input);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
