package org.goldrenard.jb.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.goldrenard.jb.model.Option;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponse {

    public ChatResponse(String message) {
        this.message = message;
    }

    public ChatResponse(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    private String sessionId;
    private String message;
    private List<Option> options;
    private Object meta;
    private Boolean ended = false;

}
