package com.example.ocr_receipt.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ocr_receipt.services.OCR_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class ReceiptController {

    private final OCR_service ocrService;

    @Autowired
    public ReceiptController(OCR_service ocrService) {
        this.ocrService = ocrService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Online";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        String tempDir = "temp/";
        File dir = new File(tempDir);

        if(!dir.exists()) {
          Boolean dirCreated = dir.mkdirs();
          if(!dirCreated) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create directory");
          }
        } else {
            System.out.println("temp Dir: " + tempDir);
        }

        for(String entry : dir.list()) {
            System.out.println(entry);
        }

        File tempFile = new File(tempDir + file.getOriginalFilename());

        System.out.println(tempFile.toString());

        try {
            System.out.println("Transfering");
            file.transferTo(tempFile);
            if(tempFile.exists()) {
                System.out.println("File exists");
            } else {
                System.out.println("File does not transfer");
            }
           ;
            // Perform OCR using OCR_service
            
            String ocrResult = ocrService.doOCR(tempFile.getAbsolutePath());
            System.out.println("Result: " + ocrResult);
            // Delete the temporary file

            boolean isDeleted = tempFile.delete();
            if(!isDeleted) {
                System.err.println("Failed to delete temp file" + tempFile.getAbsolutePath());
            }
            return ResponseEntity.ok().body(ocrResult);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }

    }
}
