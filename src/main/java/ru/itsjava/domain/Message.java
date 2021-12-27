package ru.itsjava.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Message {
    private final String from;
    private final String to;
    private final String text;


    @Override
    public String toString() {
        return "Message {" + "from: " + from + ", to: " + to + ", text: " + text + '}';
    }
}

