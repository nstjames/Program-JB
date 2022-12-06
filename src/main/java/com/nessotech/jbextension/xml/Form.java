package com.nessotech.jbextension.xml;

import com.nessotech.jbextension.parser.ResponseParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.goldrenard.jb.beans.ChatResponse;
import org.goldrenard.jb.core.Chat;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@XmlRootElement(name = "Form")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"field", "confirmation", "success", "cancel", "apiCall"})
@Getter
@Setter
public class Form implements FormOperation {

    enum RoutineState {
        CANCELLED,
        FINALIZING,
        RUNNING,
        ENDED
    }

    private Map<String, Field> field = new HashMap();
    private String confirmation;

    @XmlTransient
    private Integer step = 0;

    @XmlTransient
    private Chat chat;

    @XmlTransient
    private RoutineState state = RoutineState.RUNNING;
    private String success;
    private String cancel;
    private APICall apiCall;

    public Form() {

    }

    @Override
    public ChatResponse start() {
        return ResponseParser.parse(field.entrySet().stream().findFirst().get().getValue().getMessage(), this.chat);
    }

    @Override
    public ChatResponse write(String input) {

        String stepKey = new ArrayList<>(field.keySet()).get(step);

        Field fieldEntry = field.get(stepKey);

        if (fieldEntry.getType().equals(TypeInput.YES_NO)) {

            if (input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("NO")) {
                return ResponseParser.parse(fieldEntry.getAction().getChoices().get(input.toUpperCase()), chat);
            } else {
                return ResponseParser.parse("Invalid Input");
            }

        } else if (fieldEntry.getType().equals(TypeInput.EMAIL)) {
            if (input.matches("^(.+)@(.+)$")) {
                fieldEntry.setInput(input);
                return ResponseParser.parse(fieldEntry.getAction().getChoices().get("TRUE"), chat);
            } else if (input.equalsIgnoreCase("CANCEL")) {
                return ResponseParser.parse(fieldEntry.getAction().getChoices().get(input.toUpperCase()), chat);
            } else {
                return ResponseParser.parse("Invalid Email format");
            }

        } else {
            fieldEntry.setInput(input);
            return ResponseParser.parse("CHATOPERATION:CONTINUE", chat);
        }
    }

    @Override
    public ChatResponse next() {
        if(step >= field.keySet().size()-1){
            runOperation();
            chat.setForm(null);
            return ResponseParser.parse(this.success);
        }else{
            step++;
            String stepKey = new ArrayList<>(field.keySet()).get(step);
            Field fieldEntry = field.get(stepKey);
            return ResponseParser.parse(fieldEntry.getMessage());
        }
    }

    @Override
    public void runOperation() {
        Map<String, String> placeholders = new HashMap<>();
        field.entrySet().forEach(stringFieldEntry -> {
            placeholders.put(stringFieldEntry.getKey(), stringFieldEntry.getValue().getInput());
        });
        apiCall.run(placeholders);
    }

    public boolean hasEnded() {
        return this.state.equals(RoutineState.ENDED);
    }
}
