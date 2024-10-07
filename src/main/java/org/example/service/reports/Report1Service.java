package org.example.service.reports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.dao.reports.Report1Dao;
import org.example.entity.views.ReportView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class Report1Service {
    private final Report1Dao report1Dao = new Report1Dao();

    private final String excelFilePath = "report1.xls";

    public void createFirstReport() throws IOException {
        List<ReportView> reportViewList = report1Dao.getView();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Squad Data");

        Row headerRow1 = sheet.createRow(1);
        Cell headerCell12 = headerRow1.createCell(6);
        headerCell12.setCellValue("количество детей в каждом отряде");

        Row headerRow = sheet.createRow(2);
        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("название отряда");

        Cell headerCell2 = headerRow.createCell(6);
        headerCell2.setCellValue("количество детей");

        int rowNum = 3;
        for (ReportView reportView:reportViewList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(1).setCellValue(reportView.getName());
            row.createCell(6).setCellValue(reportView.getCount());
        }

        try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOut);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        workbook.close();

        System.out.println("Data exported successfully to " + excelFilePath);
    }
}
