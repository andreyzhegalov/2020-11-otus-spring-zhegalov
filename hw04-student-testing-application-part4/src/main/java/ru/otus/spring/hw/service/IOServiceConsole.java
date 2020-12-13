package ru.otus.spring.hw.service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceConsole implements IOService {
    private final PrintStream out;
    private final Scanner in;

    public IOServiceConsole(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
    }

    @Override
    public void print(String data) {
        out.println(data);
        out.flush();
    }

    @Override
    public String read() {
        if (in.hasNext()) {
            return in.nextLine().strip();
        }
        return "";
    }
}
