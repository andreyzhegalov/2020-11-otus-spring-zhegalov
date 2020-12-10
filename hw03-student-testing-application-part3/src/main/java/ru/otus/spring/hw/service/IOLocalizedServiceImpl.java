package ru.otus.spring.hw.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IOLocalizedServiceImpl implements IOLocalizedService {
    private final IOService ioService;
    private final LocalizationService localizationService;

    @Override
    public String read() {
        return ioService.read();
    }

    @Override
    public void print(String key, Object... args) {
        final var text = localizationService.getText(key, args);
        ioService.print(text);
    }
}
