package dao;

import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;

import java.sql.SQLException;
import java.util.Collection;

public interface ChatDAO {
    void addChat(ChatEntity chat) throws SQLException;
    void updateChat(ChatEntity chat, Byte isGroupChat, Byte isUserChat) throws SQLException;
    ChatEntity getChatById(Long id) throws SQLException;
    ChatEntity getChatByMessage(MessageEntity message) throws SQLException;
    ChatEntity getChatByAnswer(AnswerEntity answer) throws SQLException;
    Collection getChatsByUser(UserEntity user) throws SQLException;
    Collection getAllChats() throws SQLException;
    void deleteChat(ChatEntity chat) throws SQLException;
}
