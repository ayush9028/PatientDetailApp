package com.example.controller;

import com.example.entity.Patient;
import com.example.service.PatientService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class Tab3Controller {

    @Autowired
    private PatientService patientService;

    @CrossOrigin(origins = "*")

    @GetMapping(value = "/patients", produces = "application/json")
    public String getPatients(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 5;
        List<Patient> patients = patientService.getPatients(search, page, pageSize);
        int totalPages = patientService.getTotalPages(search, pageSize);

        JSONObject response = new JSONObject();
        response.put("patients", patients);
        response.put("totalPages", totalPages);
        return response.toString();
    }
}
