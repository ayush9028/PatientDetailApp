package com.example.service;
import com.example.dao.PatientDao;
import com.example.entity.Patient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private PatientDao patientDao;
    @Transactional
    public void importPatientsFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                Patient p = new Patient();
                p.setName(row.getCell(0).getStringCellValue());
                p.setAge((int) row.getCell(1).getNumericCellValue());
                p.setGender(row.getCell(2).getStringCellValue());

                patientDao.save(p);
            }
        }
    }

    public ByteArrayInputStream exportPatientsToExcel() throws IOException {
        List<Patient> patients = patientDao.list();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Patients");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Age");
        header.createCell(2).setCellValue("Gender");

        int rowNum = 1;
        for (Patient p : patients) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getName());
            row.createCell(1).setCellValue(p.getAge());
            row.createCell(2).setCellValue(p.getGender());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
