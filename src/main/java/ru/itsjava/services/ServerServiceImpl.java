package ru.itsjava.services;

import lombok.SneakyThrows;
import ru.itsjava.dao.MessageDao;
import ru.itsjava.dao.MessageDaoImpl;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.domain.User;
import ru.itsjava.utils.Props;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerServiceImpl implements ServerService{
    public final static int PORT = 8081;
    public final List<Observer> observers = new ArrayList<>();
    private final UserDao userDao = new UserDaoImpl(new Props());
    private final MessageDao messageDao = new MessageDaoImpl(new Props());

    @SneakyThrows
    @Override
    public void start() {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("====== SERVER STARTS ======");
        while (true) {
            Socket socket = serverSocket.accept();
            if (socket != null){
                Thread thread = new Thread(new ClientRunnable(socket, this, userDao, messageDao));
                thread.start();
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String message) {
        for (Observer observer : observers) {
            observer.notifyMe(message);
        }
    }

    @Override
    public void notifyObserversExceptMe(String message, Observer observerWithoutMessage) {
        for (Observer observer : observers){
            if (!observer.equals(observerWithoutMessage)){
                observer.notifyMe(message);
            }
        }
    }
}
