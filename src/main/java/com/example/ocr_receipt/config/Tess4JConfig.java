package com.example.ocr_receipt.config;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Tess4JConfig {

    @Bean
    public ITesseract getTesseractInstance() {
        ITesseract instance = new Tesseract();

//        instance.setDatapath(RunConfiguration);
        // Set Tesseract data path if needed
        // instance.setDatapath("path/to/tessdata");
        return instance;
    }
}