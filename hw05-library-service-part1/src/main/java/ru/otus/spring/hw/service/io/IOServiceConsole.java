package ru.otus.spring.hw.service.io;

import java.io.PrintStream;

import ru.otus.spring.hw.io.ScannerInputStream;

public class IOServiceConsole implements IOService {
    private final PrintStream out;
    private final ScannerInputStream in;

    public IOServiceConsole(PrintStream out, ScannerInputStream in) {
        this.out = out;
        this.in = in;
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
