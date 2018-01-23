package dao;

import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

public interface UserDAO {
    void addUser(UserEntity user) throws SQLException;
    void updateUser(UserEntity user, String name, String username, String instituteTitle, String groupTitle,
                    Byte isRemembered) throws SQLException;
    UserEntity getUserById(Integer userId) throws SQLException;
    UserEntity getUserByMessage(MessageEntity message) throws SQLException;
    UserEntity getUserByUsername(String username) throws SQLException;
    Collection getUsersByChat(ChatEntity chat) throws SQLException;
    Collection getUsersByAnswer(AnswerEntity answer) throws SQLException;
    Collection getUsersByInstitute(String institute) throws SQLException;
    Collection getUsersByGroup(String group) throws SQLException;
    Collection getRememberedUsers() throws SQLException;
    Collection getUsersByRegDate(Date date) throws SQLException;
    Collection getAllUsers() throws SQLException;
    void deleteUser(UserEntity user) throws SQLException;
}
