package ru.otus.spring.hw.controller;

import java.io.PrintStream;

public class IOControllerConsole implements IOController {
    private final PrintStream out;

    public IOControllerConsole(PrintStream out) {
        this.out = out;
    }

    @Override
    public void print(String data) {
        out.println(data);
        out.flush();
    }
}
