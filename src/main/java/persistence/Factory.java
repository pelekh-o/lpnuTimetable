package persistence;

import dao.AnswerDAO;
import dao.ChatDAO;
import dao.MessageDAO;
import dao.UserDAO;
import dao.implementation.AnswerDAOImpl;
import dao.implementation.ChatDAOImpl;
import dao.implementation.MessageDAOImpl;
import dao.implementation.UserDAOImpl;

public class Factory {
    public static AnswerDAO answerDAO = null;
    public static ChatDAO chatDAO = null;
    public static MessageDAO messageDAO = null;
    public static UserDAO userDAO = null;
    public static Factory instance = null;

    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public AnswerDAO getAnswerDAO() {
        if (answerDAO == null) {
            answerDAO = new AnswerDAOImpl();
        }
        return answerDAO;
    }

    public ChatDAO getChatDAO() {
        if (chatDAO == null) {
            chatDAO = new ChatDAOImpl();
        }
        return chatDAO;
    }

    public MessageDAO getMessageDAO() {
        if (messageDAO == null) {
            messageDAO = new MessageDAOImpl();
        }
        return messageDAO;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }
}
