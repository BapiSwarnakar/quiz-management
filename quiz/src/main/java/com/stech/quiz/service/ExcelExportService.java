package com.stech.quiz.service;

import com.stech.quiz.dto.QuizReportDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {
    private final ReportService reportService;

    public byte[] generateQuizReport() throws Exception {
        List<QuizReportDTO> reports = reportService.generateQuizReports();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Quiz Reports");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Quiz Title");
            headerRow.createCell(1).setCellValue("Total Attempts");
            headerRow.createCell(2).setCellValue("Average Score");
            headerRow.createCell(3).setCellValue("Pass Rate");
            
            // Fill data rows
            int rowNum = 1;
            for (QuizReportDTO report : reports) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(report.getQuizTitle());
                row.createCell(1).setCellValue(report.getTotalAttempts());
                row.createCell(2).setCellValue(report.getAverageScore() + "%");
                row.createCell(3).setCellValue(report.getPassRate() + "%");
            }
            
            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
