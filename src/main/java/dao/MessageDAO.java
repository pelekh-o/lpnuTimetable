package dao;

import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;

import java.sql.SQLException;
import java.util.Collection;

public interface MessageDAO {
    void addMessage(MessageEntity message) throws SQLException;
    void updateMessage(MessageEntity message, Integer messageId, String messageText) throws SQLException;
    MessageEntity getMessageById(Integer id) throws SQLException;
    Collection getMessagesByChat(ChatEntity chat) throws SQLException;
    Collection getMessagesByUser(UserEntity user) throws SQLException;
    Collection getAllMessages() throws SQLException;
    void deleteMessage(MessageEntity message) throws SQLException;
}
