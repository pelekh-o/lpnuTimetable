package dao.implementation;

import dao.MessageDAO;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import persistence.HibernateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageDAOImpl implements MessageDAO{

    private static final Logger log = Logger.getLogger(MessageDAOImpl.class);

    @Override
    public void addMessage(MessageEntity message) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(message);
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
    public void updateMessage(MessageEntity message, Integer messageId, String messageText) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            message.setMessageId(messageId);
            message.setMessageText(messageText);
            session.save(message);
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
    public MessageEntity getMessageById(Integer id) throws SQLException {
        Session session = null;
        MessageEntity message = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            message = (MessageEntity) session.createQuery("select m from MessageEntity m where m.messageId = :messageId")
                    .setParameter("messageId" , id);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return message;
    }

    @Override
    public Collection getMessagesByChat(ChatEntity chat) throws SQLException {
        Session session = null;
        List messages = new ArrayList<MessageEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            messages = session.createQuery("select m from MessageEntity m where m.chat.chatId = :chatId")
                    .setParameter("chatId", chat.getChatId())
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;
    }

    @Override
    public Collection getMessagesByUser(UserEntity user) throws SQLException {
        Session session = null;
        List messages = new ArrayList<MessageEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            messages = session.createQuery("select m from MessageEntity m where m.chat.user.userId = :userId")
                    .setParameter("userId", user.getUserId())
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;
    }

    @Override
    public Collection getAllMessages() throws SQLException {
        Session session = null;
        List messages = new ArrayList<MessageEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            messages = session.createQuery("select a from AnswerEntity a").list();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;
    }

    @Override
    public void deleteMessage(MessageEntity message) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(message);
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
