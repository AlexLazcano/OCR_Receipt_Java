package com.example.ocr_receipt.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OCR_service {
    private final ITesseract tesseract;

    @Autowired
    public OCR_service(ITesseract tesseract){
        this.tesseract = tesseract;
    }

    public String doOCR(InputStream inputStream) throws IOException {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            return tesseract.doOCR(image);
        }
        catch (TesseractException e) {
            e.printStackTrace();
            return "OCR Error";
        }
    }
}
