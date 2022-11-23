package com.nessotech.jbextension.xml;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private RoutineState state = RoutineState.RUNNING;
    private String success;
    private String cancel;
    private APICall apiCall;

    public Form() {

    }

    @Override
    public String start() {
        return field.entrySet().iterator().next().getValue().getMessage();
    }

    @Override
    public void write(String input) {
        AtomicBoolean matchFound = new AtomicBoolean(false);
        field.entrySet().stream().forEach(stringFieldEntry -> {
            if (!stringFieldEntry.getValue().hasInput() && !matchFound.get()) {
                stringFieldEntry.getValue().setInput(input);
                matchFound.set(true);
            }
        });

        if (!matchFound.get() && !state.equals(RoutineState.FINALIZING)) {
            state = RoutineState.FINALIZING;
        } else if (!matchFound.get() && state.equals(RoutineState.FINALIZING)) {
            if (input.equalsIgnoreCase("Yes")) {
                state = RoutineState.ENDED;
            } else {
                state = RoutineState.CANCELLED;
            }
        }
    }

    @Override
    public String next() {

        String response = "";

        if (state.equals(RoutineState.RUNNING)) {
            Optional<Map.Entry<String, Field>> result = field.entrySet().stream()
                    .filter(stringFieldEntry -> !stringFieldEntry.getValue().hasInput())
                    .findFirst();

            if (result.isEmpty()) {
                state = RoutineState.FINALIZING;
                return next();
            } else {
                return result.get().getValue().getMessage();
            }

        } else if (this.state.equals(RoutineState.FINALIZING)) {
            if (StringUtils.isEmpty(this.getConfirmation())) {
                this.state = RoutineState.ENDED;
                return this.next();
            } else {
                return this.getConfirmation();
            }
        } else if (state.equals(RoutineState.CANCELLED)) {
            return this.getCancel();

        } else if (state.equals(RoutineState.ENDED)) {
            this.runOperation();
            return this.getSuccess();
        }

        return response;
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
