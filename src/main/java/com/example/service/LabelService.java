package com.example.service;

import com.example.dao.LabelDao;

import com.example.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    public void save(Label label) {
        labelDao.save(label);
    }

    public List<Label> list() {
        return labelDao.list();
    }

    public void delete(int id) {
        labelDao.delete(id);
    }

    public Label get(int id) {
        return labelDao.get(id);
    }
}

