package dao.implementation;

import dao.ChatDAO;
import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import persistence.HibernateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatDAOImpl implements ChatDAO {
    private static final Logger log = Logger.getLogger(ChatDAOImpl.class);

    @Override
    public void addChat(ChatEntity chat) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(chat);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void updateChat(ChatEntity chat, Byte isGroupChat, Byte isUserChat) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            chat.setIsGroupChat(isGroupChat);
            chat.setIsUserChat(isUserChat);
            session.save(chat);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public ChatEntity getChatById(Long id) throws SQLException {
        Session session = null;
        ChatEntity chat = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            chat = (ChatEntity) session.createQuery("select c from ChatEntity c where c.chatId = :chatId")
                    .setParameter("chatId", id);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return chat;
    }

    @Override
    public ChatEntity getChatByMessage(MessageEntity message) throws SQLException {
        Session session = null;
        ChatEntity chat = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Long chatId = message.getChat().getChatId();
            chat = (ChatEntity) session.createQuery("select c from ChatEntity c where c.chatId = :chatId")
                    .setParameter("chatId", chatId);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return chat;
    }

    @Override
    public Collection getChatsByUser(UserEntity user) throws SQLException {
        Session session = null;
        List chats = new ArrayList<ChatEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int userId = user.getUserId();
            chats = session.createQuery("select c from ChatEntity c where c.user.id = :userId")
                    .setParameter("userId", userId)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return chats;
    }

    @Override
    public ChatEntity getChatByAnswer(AnswerEntity answer) throws SQLException {
        Session session = null;
        ChatEntity chat = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Long chatId = answer.getMessage().getChat().getChatId();
            chat = (ChatEntity) session.createQuery("select c from ChatEntity c where c.chatId = :chatId")
                    .setParameter("chatId", chatId);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return chat;
    }

    @Override
    public Collection getAllChats() throws SQLException {
        Session session = null;
        List chats = new ArrayList<ChatEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("select c from ChatEntity c");
            chats = query.list();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return chats;
    }

    @Override
    public void deleteChat(ChatEntity chat) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(chat);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
