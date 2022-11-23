package com.nessotech.jbextension.xml;

import com.nessotech.libraries.RestUtil.bean.Header;
import com.nessotech.libraries.RestUtil.enums.Method;
import com.nessotech.libraries.RestUtil.request.GenericRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.goldrenard.jb.beans.ChatResponse;
import org.goldrenard.jb.configuration.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.*;
import java.net.URL;
import java.util.Map;

@XmlRootElement(name = "apiCall")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"url", "method", "param", "headers", "bodyContent"})
@Setter
@Getter
public class APICall implements Operation {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> param;
    private String bodyContent;

    @XmlTransient
    private Map<String, String> placeholders;

    public APICall() {

    }

    @Override
    public ChatResponse run(Map<String, String> placeholder) {

        this.placeholders = placeholder;
        this.runPlaceHolderSubstitution();
        return this.runAPICall();

    }

    public void runPlaceHolderSubstitution() {
        placeholders.forEach((key, value) -> {

            String placeholder = "\\$\\{" + key + "}";

            if (url.contains(key))
                url = url.replaceAll(placeholder, value);

            if (null!=headers && !headers.isEmpty())
                headers.replaceAll((headerKey, headerValue) -> headerValue.replaceAll(placeholder, value));

            if (null !=param && !param.isEmpty())
                param.replaceAll((paramKey, paramValue) -> paramValue.replaceAll(placeholder, value));

            if (null != bodyContent)
                this.bodyContent = bodyContent.replaceAll(placeholder, value);

        });
    }

    @SneakyThrows
    public ChatResponse runAPICall() {

//        GenericRequest request = new GenericRequest(url, Method.valueOf(method));
//
//        if (null != headers && !headers.isEmpty())
//            headers.entrySet().stream().forEach(headerEntry ->
//                    request.addHeader(new Header(headerEntry.getKey(), headerEntry.getValue()))
//            );
//
//        if (null != param && !param.isEmpty())
//            param.entrySet().stream().forEach(paramEntry ->
//                    request.addHeader(new Header(paramEntry.getKey(), paramEntry.getValue()))
//            );
//
//        request.setBody(bodyContent);

//        HttpHeaders httpHeaders = new HttpHeaders();
//        if (null!=headers && !headers.isEmpty())
//            headers.entrySet().stream().forEach(headerEntry ->
//                    httpHeaders.add(headerEntry.getKey(), headerEntry.getValue()));
//
//        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(url);
//        if (null!=param && !param.isEmpty())
//            param.entrySet().stream().forEach(paramEntry ->
//                    urlBuilder.queryParam(paramEntry.getKey(), paramEntry.getValue()));
//
//        RestTemplate restTemplate = new RestTemplate();
//        RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(HttpMethod.valueOf(method), urlBuilder.);


        try {

            RequestEntity<String> requestEntity = RequestEntity.post(new URL(url).toURI())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bodyContent);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(requestEntity, String.class);
            return new ChatResponse("ok");

//            Response response = new RestUtil().executeRequest(request);
//            return new ChatResponse(response.getResult());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ChatResponse(Constants.default_list_item);
        }
    }

}
