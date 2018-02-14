package ru.otus.hw02;

import java.lang.instrument.Instrumentation;

public class AgentMemoryCounter {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation) {
        AgentMemoryCounter.instrumentation = instrumentation;
    }

    public static long getObjectSize(Object obj) {
        if (instrumentation == null) {
            throw new IllegalStateException("Агент не инициализирован");
        }
        return instrumentation.getObjectSize(obj);
    }
}