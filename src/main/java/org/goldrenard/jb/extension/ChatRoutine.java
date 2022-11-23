package org.goldrenard.jb.extension;

import org.goldrenard.jb.beans.ChatResponse;

public interface ChatRoutine {

    ChatResponse sendMessage(String message);

    ChatResponse firstResponse();

    ChatResponse runRoutine();

    Boolean isFinalized();


}
