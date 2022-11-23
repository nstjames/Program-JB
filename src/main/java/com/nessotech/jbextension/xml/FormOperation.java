package com.nessotech.jbextension.xml;

import org.goldrenard.jb.beans.ChatResponse;

public interface FormOperation {

    String start();

    String next();
    void write(String input);

    void runOperation();

}
