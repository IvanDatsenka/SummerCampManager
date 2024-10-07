package org.example.service.reports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.dao.reports.Report2Dao;
import org.example.entity.views.Report2View;
import org.example.entity.views.ReportView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Report2Service {
    private final Report2Dao report2Dao = new Report2Dao();

    private final String excelFilePath = "report2.xls";
    public void createSecondReport(String eventName) throws IOException {
        List<Report2View> reportViewList = report2Dao.getReport2Views(eventName);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("отчёт 2, отряды участвующие в "+reportViewList.getFirst().getEventName());
        Row headerRow1 = sheet.createRow(0);
        Cell cell = headerRow1.createCell(7);
        cell.setCellValue("отряды участвующие в "+reportViewList.getFirst().getEventName() );

        Row headerRow = sheet.createRow(2);
        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("название отряда");

        Cell headerCell2 = headerRow.createCell(6);
        headerCell2.setCellValue("вожатый");

        int rowNum = 3;
        for (Report2View reportView:reportViewList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(1).setCellValue(reportView.getSquadName());
            row.createCell(6).setCellValue(reportView.getEmployeeName());
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
