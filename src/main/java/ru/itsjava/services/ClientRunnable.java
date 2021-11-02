package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.domain.User;
import ru.itsjava.utils.Props;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final ServerService serverService;
    private User user;
    private final UserDao userDao;


    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Client connected");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        if (authorization(bufferedReader)) {
            serverService.addObserver(this);
        } else if (registration(bufferedReader)) {
            serverService.addObserver(this);
        } else {
            throw new InputMismatchException("Input error!");
        }
        String messageFromClient;
        while ((messageFromClient = bufferedReader.readLine()) != null) {
            System.out.println(user.getName() + ":" + messageFromClient);
            serverService.notifyObserversExceptMe(user.getName() + ":" + messageFromClient, this);
        }
    }

    @SneakyThrows
    private boolean authorization(BufferedReader bufferedReader) {
        String authorizationMessage;
        if ((authorizationMessage = bufferedReader.readLine()) != null && authorizationMessage.startsWith("!autho!")) {
            //!autho!login:password
            String login = authorizationMessage.substring(7).split(":")[0];
            String password = authorizationMessage.substring(7).split(":")[1];
            user = userDao.findByNameAndPassword(login, password);
            return true;
        }
        return false;
    }
    @SneakyThrows
    private boolean registration(BufferedReader bufferedReader) {
        String registrationMessage;
        if ((registrationMessage = bufferedReader.readLine()) != null && registrationMessage.startsWith("!reg!")) {
            //!reg!login:password
            String regLogin = registrationMessage.substring(5).split(":")[0];
            String regPassword = registrationMessage.substring(5).split(":")[1];
            user = userDao.addUser(regLogin, regPassword);
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = new PrintWriter(socket.getOutputStream());
        clientWriter.println(message);
        clientWriter.flush();
    }
}
