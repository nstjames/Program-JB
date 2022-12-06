package com.nessotech.jbextension.xml;

import org.goldrenard.jb.beans.ChatResponse;

public interface FormOperation {

    ChatResponse start();

    ChatResponse next();
    ChatResponse write(String input);

    void runOperation();

}
