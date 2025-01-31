package de.emilschlampp.scheCPU.util;

public interface ThrowingFunction<I, O, E extends Throwable> {
    public O apply(I input) throws E;
}
