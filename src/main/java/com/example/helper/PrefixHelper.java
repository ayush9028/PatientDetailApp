package com.example.helper;

import com.example.dao.PrefixDao;
import com.example.entity.Prefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrefixHelper {

    @Autowired
    private PrefixDao dao;

    public void save(Prefix prefix) {
        // You can add extra validation or logic here
        dao.create(prefix);
    }

    public void deleteById(int id) {
        dao.delete(id);
    }

    public List<Prefix> fetchAll() {
        return dao.list();
    }
}
