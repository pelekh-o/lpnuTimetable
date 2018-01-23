package dao;

import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;

import java.sql.SQLException;
import java.util.Collection;

public interface AnswerDAO {
    void addAnswer(AnswerEntity answer) throws SQLException;
    AnswerEntity getAnswerById(Integer id) throws SQLException;
    AnswerEntity getAnswerByMessage(MessageEntity message) throws SQLException;
    Collection getAnswersByChat(ChatEntity chat) throws SQLException;
    Collection getAnswersByUser(UserEntity user) throws SQLException;
    Collection getAllAnswers() throws SQLException;
    void deleteAnswer(AnswerEntity answer) throws SQLException;
}
