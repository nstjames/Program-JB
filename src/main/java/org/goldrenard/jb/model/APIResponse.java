package org.goldrenard.jb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class APIResponse {

    private List<Option> options;
    private Map meta;
    private String message;

}
