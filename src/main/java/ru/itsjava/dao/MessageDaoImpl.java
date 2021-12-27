package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import ru.itsjava.domain.Message;
import ru.itsjava.domain.User;
import ru.itsjava.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private final Props props;

    @Override
    public Message addMessage(String from, String to, String text) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into schema_online_course.myprojectmessages (from_username, to_username, message) values (?, ?, ?);");
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            preparedStatement.setString(3, text);

            int addMessageResult = preparedStatement.executeUpdate();
            if (addMessageResult > 0) {
                return new Message(from, to, text);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new RuntimeException("Message wasn't inserted!");
    }

    @Override
    public List<Message> lastFifteenMessagesHistory() {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT * FROM schema_online_course.myprojectmessages where to_username = \"All\" order by id desc limit 15;");

            List<Message> messageList = new ArrayList<>();
            while (resultSet.next()) {
                String from = resultSet.getString("from_username");
                String to = resultSet.getString("to_username");
                String text = resultSet.getString("message");

                Message messagesFromSql = new Message(from, to, text);
                messageList.add(messagesFromSql);
            }
            return messageList;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new RuntimeException("Message history wasn't founded!");

    }
}
