package com.example.dao;

import com.example.entity.Patient;
import com.example.entity.Prefix;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class PrefixDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void create(Prefix prefix) {
        sessionFactory.getCurrentSession().save(prefix);
    }

    public void delete(int id) {
        Prefix prefix = sessionFactory.getCurrentSession().get(Prefix.class, id);
        if (prefix != null) {
            sessionFactory.getCurrentSession().delete(prefix);
        }
    }

    public List<Prefix> list() {
        Session session = sessionFactory.openSession();
        String hql = "FROM Prefix";
        Query<Prefix> query = session.createQuery(hql, Prefix.class);

        List<Prefix> result = query.list();
        session.close();
        return result;

    }
}
