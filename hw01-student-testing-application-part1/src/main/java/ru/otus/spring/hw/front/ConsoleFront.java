package ru.otus.spring.hw.front;

public class ConsoleFront implements Front {

    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
