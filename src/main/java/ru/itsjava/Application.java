package ru.itsjava;

import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.domain.User;
import ru.itsjava.services.ServerService;
import ru.itsjava.services.ServerServiceImpl;
import ru.itsjava.utils.Props;


public class Application {


    public static void main(String[] args) {
        ServerService serverService = new ServerServiceImpl();
        serverService.start();

    }
}
