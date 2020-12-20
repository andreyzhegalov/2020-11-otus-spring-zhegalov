package ru.otus.spring.hw.io;

import java.util.Scanner;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScannerInputStreamImpl implements ScannerInputStream {
    private final Scanner scanner;

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}
