package com.example.ocr_receipt.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OCR_service {
    private final ITesseract tesseract;

    @Autowired
    public OCR_service(ITesseract tesseract){
        this.tesseract = tesseract;
    }

    public String doOCR(String imagePath) {
        try {
            return tesseract.doOCR(new File(imagePath));
        }
        catch (TesseractException e) {
            return "error " + e.getMessage();
        }
    }
}
