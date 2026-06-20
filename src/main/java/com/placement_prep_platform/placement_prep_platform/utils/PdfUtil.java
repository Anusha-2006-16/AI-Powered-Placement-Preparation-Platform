package com.placement_prep_platform.placement_prep_platform.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfUtil {
    public static String extractText(String filePath) throws IOException{
        File file = new File(filePath);
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper stripper=new PDFTextStripper();
        String text=stripper.getText(document);
        document.close();
        return text;

    }
}
