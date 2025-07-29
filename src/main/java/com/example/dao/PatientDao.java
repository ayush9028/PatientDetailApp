package com.example.dao;

import com.example.entity.Patient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Patient> getPatients(String search, int page, int pageSize) {
        Session session = sessionFactory.openSession();
        String hql = "FROM Patient WHERE name LIKE :search ORDER BY id";
        Query<Patient> query = session.createQuery(hql, Patient.class);
        query.setParameter("search", "%" + search + "%");
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        List<Patient> result = query.list();
        session.close();
        return result;
    }

    public int getTotalPages(String search, int pageSize) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT count(*) FROM Patient WHERE name LIKE :search";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("search", "%" + search + "%");
        long count = query.uniqueResult();
        session.close();
        return (int) Math.ceil((double) count / pageSize);
    }

    public void save(Patient p) {
        sessionFactory.getCurrentSession().save(p);
    }

    public List<Patient> list() {
        Session session = sessionFactory.openSession();
        List<Patient> list = session.createQuery("FROM Patient", Patient.class).list();
        session.close();
        return list;
    }

}
