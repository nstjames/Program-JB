package com.nessotech.jbextension.xml;

import org.goldrenard.jb.beans.ChatResponse;

import java.util.Map;

public interface Operation {

    ChatResponse run(Map<String,String> placeholder);

}
