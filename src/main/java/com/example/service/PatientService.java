package com.example.service;

import com.example.dao.PatientDao;
import com.example.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientDao patientDao;

    public List<Patient> getPatients(String search, int page, int pageSize) {
        return patientDao.getPatients(search, page, pageSize);
    }

    public int getTotalPages(String search, int pageSize) {
        return patientDao.getTotalPages(search, pageSize);
    }
}
