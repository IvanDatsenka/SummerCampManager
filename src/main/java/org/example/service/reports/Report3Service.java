package org.example.service.reports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.dao.view.EmployeeViewDao;
import org.example.entity.views.EmployeeView;
import org.example.entity.views.ReportView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Report3Service {
    private final EmployeeViewDao employeeViewDao = new EmployeeViewDao();

    private final String excelFilePath = "report3.xls";

    public void createReport3() throws IOException {
        List<EmployeeView> reportViewList = employeeViewDao.getView();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("employee data");

        Row headerRow1 = sheet.createRow(0);
        Cell headerCell12 = headerRow1.createCell(4);
        headerCell12.setCellValue("СОТРУДНИКИ ЛЕТНЕГО ЛАГЕРЯ");

        Row headerRow = sheet.createRow(2);
        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("ИМЯ, ФАМИЛИЯ СОТРУДНИКА:");

        Cell headerCell2 = headerRow.createCell(6);
        headerCell2.setCellValue("ДОЛЖНОСТЬ:");

        int rowNum = 4;
        for (EmployeeView employeeView:reportViewList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(1).setCellValue(employeeView.getEmployeeFullName());
            row.createCell(6).setCellValue(employeeView.getJobTitleName());
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
