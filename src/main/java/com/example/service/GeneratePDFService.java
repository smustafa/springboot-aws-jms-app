package com.example.service;

import com.example.pojo.User;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GeneratePDFService {

    public File generateUserInformationPDFFile(User user) throws DocumentException, IOException {

        File pdfFile = new File(user.getId() + "_" + user.getFirstName() + "_" + user.getLastName() + ".pdf");
        pdfFile.createNewFile();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        document.open();
        document.add(generateTable(user));
        document.close();

        return pdfFile;
    }

    private PdfPTable generateTable(User user) {

        PdfPTable table = new PdfPTable(4);

        Stream.of("ID", "First Name", "Last Name", "Age").forEachOrdered(columnName -> {

            PdfPCell header = new PdfPCell();

            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnName));

            table.addCell(header);
        });

        //Write 1 Row
        table.addCell(user.getId());
        table.addCell(user.getFirstName());
        table.addCell(user.getLastName());
        table.addCell(String.valueOf(user.getAge()));

        return table;
    }

}
