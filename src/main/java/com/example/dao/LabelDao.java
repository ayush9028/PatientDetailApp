package com.example.dao;


import com.example.entity.Label;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LabelDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Label label) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(label);
    }

    public List<Label> list() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Label", Label.class)
                .list();
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Label label = session.get(Label.class, id);
        if (label != null) session.delete(label);
    }

    public Label get(int id) {
        return sessionFactory.getCurrentSession().get(Label.class, id);
    }
}
