package com.nessotech.jbextension.parser;

import com.google.gson.Gson;
import org.goldrenard.jb.beans.ChatResponse;
import org.goldrenard.jb.core.Chat;
import org.goldrenard.jb.model.APIResponse;
import org.springframework.lang.Nullable;

public class ResponseParser {

    public static ChatResponse parse(String message){
        return parse(message, null);
    }

    public static ChatResponse parse(String message, @Nullable Chat chat){

        if(message.startsWith("JSON:")){
            message = message.replaceFirst("JSON:", "");

            APIResponse apiResponse = new Gson().fromJson(message, APIResponse.class);

            ChatResponse chatResponse = new ChatResponse();

            chatResponse.setMessage(apiResponse.getMessage());
            chatResponse.setMeta(apiResponse.getMeta());
            chatResponse.setOptions(apiResponse.getOptions());

            return chatResponse;

        }else if(message.equalsIgnoreCase("CHATOPERATION:CANCEL")){
            ChatResponse response = new ChatResponse(chat.getForm().getCancel());
            chat.setForm(null);
            return response;

        }else if(message.equalsIgnoreCase("CHATOPERATION:CONTINUE")){
            return chat.getForm().next();

        }else{
            return new ChatResponse(message);
        }

    }

}
