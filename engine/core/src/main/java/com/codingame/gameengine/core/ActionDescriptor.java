package com.codingame.gameengine.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An <code>Action</code> parses players outputs. An action must be either <b>matched</b> (if it is the only possible action) or <b>handled</b> by an
 * <code>ActionManager</b>.
 * <p>
 * It features:
 * <ul>
 * <li>The keyword of the action</li>
 * <li>(Optional) A set of parameters</li>
 * <li>(Optional) Allowing players to send messages</li>
 * </ul>
 */
public class ActionDescriptor {
    private final static Map<Class<?>, Function<String, ?>> ACCEPTED_CLASSES_CALLBACKS = new HashMap<>();
    protected static Log log = LogFactory.getLog(ActionDescriptor.class);

    static {
        ACCEPTED_CLASSES_CALLBACKS.put(Integer.class, s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        ACCEPTED_CLASSES_CALLBACKS.put(Long.class, s -> {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        ACCEPTED_CLASSES_CALLBACKS.put(Float.class, s -> {
            try {
                return Float.parseFloat(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        ACCEPTED_CLASSES_CALLBACKS.put(Double.class, s -> {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        ACCEPTED_CLASSES_CALLBACKS.put(String.class, s -> s);
        ACCEPTED_CLASSES_CALLBACKS.put(Character.class, s -> s.charAt(0));
    }

    private String keyword;
    private List<Class<?>> parametersSettings;
    private boolean allowMessage;

    public ActionDescriptor(String keyword, List<Class<?>> parametersSettings, boolean message) {
        super();
        this.keyword = keyword;
        this.parametersSettings = parametersSettings;
        this.allowMessage = message;

        checkParameters();
    }

    public ActionDescriptor(String keyword, List<Class<?>> parametersSettings) {
        this(keyword, parametersSettings, false);
    }

    public ActionDescriptor(String keyword) {
        this(keyword, false);
    }

    public ActionDescriptor(String keyword, boolean message) {
        this(keyword, new ArrayList<>(), message);
    }

    private void checkParameters() {
        for (Class<?> parameterClass : parametersSettings) {

            if (ACCEPTED_CLASSES_CALLBACKS.keySet().stream().noneMatch(cls -> parameterClass.equals(cls))) {
                throw new RuntimeException(
                    "Parameter of index " + parametersSettings.indexOf(parameterClass) + " of action \"" + keyword + "\" does not have a valid type. It must be one among "
                        + ACCEPTED_CLASSES_CALLBACKS.keySet().stream().map(Class::getName).collect(Collectors.joining(", "))
                );
            }
        }
    }

    /**
     * Checks if the action matches the given instruction and has legal parameters.
     * 
     * @param instruction
     *            the <code>String</code> to match
     * @return <b>true</b> if the action is correctly matched.
     */
    Action matches(String instruction) {
        List<Object> parameters = new ArrayList<>();
        String message = "";
        
        instruction = instruction.trim();
        List<String> instructions = Arrays.asList(instruction.split(" "));
            
        if (instructions.size() <= parametersSettings.size()) {
            return null;
        }
            
        instructions = instructions.subList(0, parametersSettings.size() + 1);

        if (keyword == null || !keyword.equalsIgnoreCase(instructions.get(0))) {
            return null;
        }

        // Parse parameters
        for (int i = 1; i < instructions.size(); i++) {
            Object parameter = ACCEPTED_CLASSES_CALLBACKS.get(parametersSettings.get(i - 1)).apply(instructions.get(i));
            if (parameter == null) {
                return null;
            } else {
                parameters.add(parameter);
            }
        }

        // Parse message
        if (allowMessage) {
            List<String> splittedInstruction = Arrays.asList(instruction.split(" "));
            splittedInstruction = splittedInstruction.subList(parametersSettings.size() + 1, splittedInstruction.size());
            message = splittedInstruction.stream().collect(Collectors.joining(" ")).trim();
        }

        return new Action(keyword, parameters, message);
    }
}
