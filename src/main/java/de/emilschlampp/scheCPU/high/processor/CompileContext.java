package de.emilschlampp.scheCPU.high.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CompileContext {
    private final Map<String, Integer> variableAddresses;
    private final Map<String, String> options;
    private final List<String> methods;
    private final List<String> protectedVariables;
    private final List<String> protectedOptions;
    private final Consumer<String> onWarn;
    private final Consumer<String> onError;

    public CompileContext(Map<String, Integer> variableAddresses, Map<String, String> options, List<String> methods, List<String> protectedVariables, List<String> protectedOptions, Consumer<String> onWarn, Consumer<String> onError) {
        this.variableAddresses = variableAddresses;
        this.options = options;
        this.methods = methods;
        this.protectedVariables = protectedVariables;
        this.protectedOptions = protectedOptions;
        this.onWarn = onWarn;
        this.onError = onError;
    }

    public Map<String, Integer> getVariableAddresses() {
        return variableAddresses;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public List<String> getMethods() {
        return methods;
    }

    public List<String> getProtectedVariables() {
        return protectedVariables;
    }

    public List<String> getProtectedOptions() {
        return protectedOptions;
    }

    public void warn(String message) {
        onWarn.accept(message);
    }

    public void error(String message) {
        onError.accept(message);
    }
}
