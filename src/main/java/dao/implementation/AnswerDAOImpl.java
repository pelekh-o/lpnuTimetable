package dao.implementation;

import dao.AnswerDAO;
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

public class AnswerDAOImpl implements AnswerDAO {
    private static final Logger log = Logger.getLogger(AnswerDAOImpl.class);


    @Override
    public void addAnswer(AnswerEntity answer) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(answer);
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
    public AnswerEntity getAnswerById(Integer id) throws SQLException {
        Session session = null;
        AnswerEntity answer = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            answer = (AnswerEntity) session.createQuery("select a from AnswerEntity a where a.answerId = :answerId")
                    .setParameter("answerId", id);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return answer;
    }

    @Override
    public AnswerEntity getAnswerByMessage(MessageEntity message) throws SQLException {
        Session session = null;
        AnswerEntity answer = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            answer = (AnswerEntity) session.createQuery("select a from AnswerEntity a where a.message.messageId = :messageId")
                    .setParameter("messageId", message.getMessageId());
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return answer;
    }

    @Override
    public Collection getAnswersByChat(ChatEntity chat) throws SQLException {
        Session session = null;
        List answers = new ArrayList<ChatEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            answers = session.createQuery("select a from AnswerEntity a where a.message.chat.chatId = :chatId")
                    .setParameter("chatId",chat.getChatId())
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return answers;
    }

    @Override
    public Collection getAnswersByUser(UserEntity user) throws SQLException {
        Session session = null;
        List answers = new ArrayList<ChatEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            answers = session.createQuery("select a from AnswerEntity a where a.message.chat.user.userId = :userId")
                    .setParameter("userId", user.getUserId())
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return answers;
    }

    @Override
    public Collection getAllAnswers() throws SQLException {
        Session session = null;
        List answers = new ArrayList<AnswerEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("select a from AnswerEntity a");
            answers = query.list();

        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return answers;
    }

    @Override
    public void deleteAnswer(AnswerEntity answer) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(answer);
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
