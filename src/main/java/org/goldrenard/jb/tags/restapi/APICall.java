package org.goldrenard.jb.tags.restapi;

import com.nessotech.libraries.RestUtil.RestUtil;
import com.nessotech.libraries.RestUtil.bean.Header;
import com.nessotech.libraries.RestUtil.bean.Param;
import com.nessotech.libraries.RestUtil.enums.Method;
import com.nessotech.libraries.RestUtil.request.GenericRequest;
import com.nessotech.libraries.RestUtil.request.Response;
import org.goldrenard.jb.configuration.Constants;
import org.goldrenard.jb.model.ParseState;
import org.goldrenard.jb.tags.base.BaseTagProcessor;
import org.w3c.dom.Node;

public class APICall extends BaseTagProcessor {

    public APICall() {
        super("apicall");
    }

    @Override
    public String eval(Node node, ParseState ps) {
        String url = getAttributeOrTagValue(node, ps, "url");
        String method = getAttributeOrTagValue(node, ps, "method");


        GenericRequest request = new GenericRequest(url, Method.valueOf(method));


        for (int i = 0; i < node.getChildNodes().getLength(); i++) {

            Node entry = node.getChildNodes().item(i);

            if (entry.getNodeName().equalsIgnoreCase("header"))
                request.addHeader(new Header(getAttributeOrTagValue(entry, ps, "param"), entry.getTextContent().replaceAll("[\t]+","").replaceAll("[\n]+","")));

            if (entry.getNodeName().equalsIgnoreCase("param"))
                request.addParam(new Param(getAttributeOrTagValue(entry, ps, "param"), entry.getTextContent().replaceAll("[\t]+","").replaceAll("[\n]+","")));

            if (entry.getNodeName().equalsIgnoreCase("body"))
                request.setBody(entry.getTextContent().replaceAll("[\t]+","").replaceAll("[\n]+",""));

        }

        try {
            Response response = new RestUtil().executeRequest(request);

            return response.getResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Constants.default_list_item;
        }

    }
}
