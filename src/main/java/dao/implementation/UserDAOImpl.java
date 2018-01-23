package dao.implementation;

import dao.UserDAO;
import entity.AnswerEntity;
import entity.ChatEntity;
import entity.MessageEntity;
import entity.UserEntity;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import persistence.HibernateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger log = Logger.getLogger(UserDAOImpl.class);

    @Override
    public void addUser(UserEntity user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
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
    public void updateUser(UserEntity user, String name, String username, String instituteTitle, String groupTitle,
                           Byte isRemembered) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            user.setName(name);
            user.setGroupTitle(groupTitle);
            user.setInstituteTitle(instituteTitle);
            user.setIsRemembered(isRemembered);
            user.setUsername(username);
            session.update(user);
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
    public UserEntity getUserById(Integer userId) throws SQLException {
        Session session = null;
        UserEntity user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            user = (UserEntity) session.createQuery("select u from UserEntity u where u.userId = :userId")
                    .setParameter("userId", userId)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public Collection getUsersByChat(ChatEntity chat) throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int userId = chat.getUser().getUserId();
            users = session.createQuery("select u from UserEntity u, ChatEntity c where u.id = :userId")
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
        return users;
    }

    @Override
    public UserEntity getUserByMessage(MessageEntity message) throws SQLException {
        Session session = null;
        UserEntity user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int userId = message.getChat().getUser().getUserId();
            user = (UserEntity) session.createQuery("select u from UserEntity u where u.id = :userId")
                    .setParameter("userId", userId)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public Collection getUsersByAnswer(AnswerEntity answer) throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int userId = answer.getMessage().getChat().getUser().getUserId();
            users = session.createQuery("select u from UserEntity u where u.id = :userId")
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
        return users;
    }

    @Override
    public UserEntity getUserByUsername(String username) throws SQLException {
        Session session = null;
        UserEntity user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            user = (UserEntity) session.createQuery("select  u from UserEntity u where u.username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public Collection getUsersByInstitute(String institute) throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            users = session.createQuery("select u from UserEntity u where u.instituteTitle = :institute")
                    .setParameter("institute", institute)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public Collection getUsersByGroup(String group) throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            users = session.createQuery("select u from UserEntity u where u.groupTitle = :groupTitle")
                    .setParameter("groupTitle", group)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public Collection getRememberedUsers() throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            users = session.createQuery("select u from UserEntity u where u.isRemembered = 1").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public Collection getUsersByRegDate(Date date) throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            users = session.createQuery("select u from UserEntity u where u.regDate = :date")
                    .setParameter("date", date)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public Collection getAllUsers() throws SQLException {
        Session session = null;
        List users = new ArrayList<UserEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            users = session.createQuery("from UserEntity u").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public void deleteUser(UserEntity user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
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
