package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/api/tab7")  //
public class Tab7Controller {

    @GetMapping("/generate-pdf")  // Final URL: /api/tab7/generate-pdf
    public ResponseEntity<String> generatePdf() {
        try {
            // IMPORTANT: Folder name must match exactly
            ProcessBuilder pb = new ProcessBuilder("node", "generate-pdf.js");
            pb.directory(new File("Puppeteer"));  // ✅ lowercase folder at root level
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // log from node script
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok("PDF generated successfully!");
            } else {
                return ResponseEntity.status(500).body("Failed to generate PDF. Exit code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
