package ru.itsjava.dao;

import ru.itsjava.domain.Message;

import java.util.List;

public interface MessageDao {
    Message addMessage (String from, String to, String text);

    List<Message> lastFifteenMessagesHistory ();
}
