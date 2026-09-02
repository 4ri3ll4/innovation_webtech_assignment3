package auca.ac.rw.eventticket.dao;

import auca.ac.rw.eventticket.model.Event;
import auca.ac.rw.eventticket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EventDAO {

    public boolean insertEvent(Event event) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(event);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(Event event) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(event);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Event event = session.get(Event.class, id);
            if (event != null) {
                session.delete(event);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public Event getEventById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Event.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Event> getAllEvents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Event> query = session.createQuery("FROM Event ORDER BY id", Event.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existsByTitle(String title, Long excludeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = excludeId == null
                    ? "SELECT COUNT(e) FROM Event e WHERE LOWER(e.title) = LOWER(:title)"
                    : "SELECT COUNT(e) FROM Event e WHERE LOWER(e.title) = LOWER(:title) AND e.id != :excludeId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("title", title);
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
