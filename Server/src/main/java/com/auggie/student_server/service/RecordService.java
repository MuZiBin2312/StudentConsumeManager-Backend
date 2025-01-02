package com.auggie.student_server.service;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.mapper.RecordMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    // 英文字段到中文字段的映射
    private static final Map<String, String> fieldMapping = new HashMap<>();
    static {
        fieldMapping.put("name", "商品名");
        fieldMapping.put("student_id", "学号");
        fieldMapping.put("amount", "金额");
        fieldMapping.put("location", "位置");
        fieldMapping.put("time", "时间");
        fieldMapping.put("payment_type", "支付方式");
        fieldMapping.put("consumption_type", "消费类别");
    }

    public boolean addRecord(ConsumeRecord consumeRecord) {
        return recordMapper.addRecord(consumeRecord);
    }

    public boolean deleteRecordById(int recordId) {
        return recordMapper.deleteById(recordId) > 0;
    }

    public List<ConsumeRecord> getAllRecords() {
        return recordMapper.selectAllRecords();
    }

    public List<ConsumeRecord> findByCriteria(Map<String, Object> criteria) {
        return recordMapper.findByCriteria(criteria);
    }

    public ConsumeRecord findById(Integer recordId) {
        return recordMapper.findById(recordId);
    }

    public int updateRecordById(ConsumeRecord record) {
        return recordMapper.updateRecordById(record);
    }

    public boolean batchDelete(List<Integer> recordIds) {
        return recordMapper.batchDelete(recordIds) > 0;
    }

    public boolean batchImportFromCSV(MultipartFile file) {
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim());
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

            List<ConsumeRecord> records = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                ConsumeRecord record = new ConsumeRecord();

                record.setName(csvRecord.get(mapFieldName("name")));
                record.setStudentId(csvRecord.get(mapFieldName("student_id")));
                record.setAmount(new BigDecimal(csvRecord.get(mapFieldName("amount"))));
                record.setLocation(csvRecord.get(mapFieldName("location")));

                String timeStr = csvRecord.get(mapFieldName("time")).trim();
                try {
                    if (timeStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{2}")) {
                        try {
                            record.setTime(LocalDateTime.parse(timeStr, formatter1));
                        } catch (Exception e) {
                            record.setTime(LocalDateTime.parse(timeStr, formatter2));
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to parse time for record: " + timeStr, e);
                }

                record.setPaymentType(csvRecord.get(mapFieldName("payment_type")));
                record.setConsumptionType(csvRecord.get(mapFieldName("consumption_type")));

                records.add(record);
            }

            return recordMapper.insertBatch(records) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred during batch import", e);
        }
    }

    public boolean batchImportFromExcel(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

            List<ConsumeRecord> records = new ArrayList<>();

            // 列名数组
            String[] columnNames = {"name", "studentId", "amount", "location", "time", "paymentType", "consumptionType"};

            System.out.println("Start processing rows...");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row, columnNames)) {
                    System.out.println("Row " + i + " is empty, skipping...");
                    continue;
                }

                System.out.println("Processing row " + i + "...");
                ConsumeRecord record = new ConsumeRecord();

                // 获取并打印每列数据
                record.setName(getCellValueAsString(row.getCell(0), "name"));
                System.out.println("Name: " + record.getName());

                record.setStudentId(getCellValueAsString(row.getCell(1), "studentId"));
                System.out.println("Student ID: " + record.getStudentId());

                record.setAmount(new BigDecimal(getCellValueAsString(row.getCell(2), "amount")));
                System.out.println("Amount: " + record.getAmount());

                record.setLocation(getCellValueAsString(row.getCell(3), "location"));
                System.out.println("Location: " + record.getLocation());

                String timeStr = getCellValueAsString(row.getCell(4), "time").trim();
                System.out.println("Time: " + timeStr);

                try {
                    if (timeStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{2}")) {
                        try {
                            record.setTime(LocalDateTime.parse(timeStr, formatter1));
                        } catch (Exception e) {
                            record.setTime(LocalDateTime.parse(timeStr, formatter2));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing time for row " + i + ": " + timeStr);
                    throw new IllegalArgumentException("Failed to parse time for record: " + timeStr, e);
                }

                record.setPaymentType(getCellValueAsString(row.getCell(5), "paymentType"));
                System.out.println("Payment Type: " + record.getPaymentType());

                record.setConsumptionType(getCellValueAsString(row.getCell(6), "consumptionType"));
                System.out.println("Consumption Type: " + record.getConsumptionType());

                // Add record to the list
                records.add(record);
            }

            workbook.close();
            System.out.println("Finished processing rows. Inserting batch...");
            return recordMapper.insertBatch(records) > 0;
        } catch (Exception e) {
            System.out.println("Error occurred during batch import from Excel: " + e.getMessage());
            throw new RuntimeException("Error occurred during batch import from Excel", e);
        }
    }

    // 检查行是否为空
    private boolean isRowEmpty(Row row, String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.getCell(i);
            // 使用列名来获取单元格的值
            if (cell != null && !getCellValueAsString(cell, columnNames[i]).trim().isEmpty()) {
                return false;  // 行中有有效数据
            }
        }
        return true;  // 行为空
    }


    private String getCellValueAsString(Cell cell, String columnName) {
        if (cell == null) {
            System.out.println("Cell is null for column: " + columnName);
            return "";
        }

        System.out.println("Processing cell value for column: " + columnName);
        switch (cell.getCellType()) {
            case STRING:
                String stringValue = cell.getStringCellValue();
                System.out.println("STRING value for " + columnName + ": " + stringValue);
                return stringValue;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d H:mm");
                    String dateValue = dateFormat.format(cell.getDateCellValue());
                    System.out.println("DATE value for " + columnName + ": " + dateValue);
                    return dateValue;
                } else {
                    String numericValue = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    // 去掉末尾的 .0
                    if (numericValue.endsWith(".0")) {
                        numericValue = numericValue.substring(0, numericValue.length() - 2);
                    }
                    System.out.println("NUMERIC value for " + columnName + ": " + numericValue);
                    return numericValue;
                }
            case BOOLEAN:
                String booleanValue = Boolean.toString(cell.getBooleanCellValue());
                System.out.println("BOOLEAN value for " + columnName + ": " + booleanValue);
                return booleanValue;
            case FORMULA:
                String formulaValue = cell.getCellFormula();
                System.out.println("FORMULA value for " + columnName + ": " + formulaValue);
                return formulaValue;
            default:
                System.out.println("UNKNOWN value type for column: " + columnName);
                return "";
        }
    }

    // 根据字段映射表将英文字段名转换为中文字段名
    private String mapFieldName(String fieldName) {
        return fieldMapping.getOrDefault(fieldName, fieldName);
    }
}
