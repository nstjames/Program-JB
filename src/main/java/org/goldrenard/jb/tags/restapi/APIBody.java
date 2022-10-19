package org.goldrenard.jb.tags.restapi;

import org.goldrenard.jb.model.ParseState;
import org.goldrenard.jb.tags.base.BaseTagProcessor;
import org.w3c.dom.Node;

public class APIBody extends BaseTagProcessor {

    public APIBody(){
        super("body");
    }

    @Override
    public String eval(Node node, ParseState ps) {
        return null;
    }
}
