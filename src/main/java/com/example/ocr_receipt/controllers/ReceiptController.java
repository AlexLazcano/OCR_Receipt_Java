package com.example.ocr_receipt.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ocr_receipt.services.OCR_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/ocr")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {

            // Perform OCR using OCR_service
            String ocrResult = ocrService.doOCR(file.getInputStream());
            System.out.println("Result: " + ocrResult);

            return ResponseEntity.ok().body(ocrResult);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }

    }
}
