package ru.otus.spring.hw.service.io;

import ru.otus.spring.hw.io.ScannerInputStream;

import java.io.PrintStream;

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
