package org.goldrenard.jb.tags.restapi;

import org.goldrenard.jb.model.ParseState;
import org.goldrenard.jb.tags.base.BaseTagProcessor;
import org.w3c.dom.Node;

public class APIParam extends BaseTagProcessor {

    public APIParam(){
        super("param");
    }

    @Override
    public String eval(Node node, ParseState ps) {
        return evalTagContent(node, ps, null);
    }
}
