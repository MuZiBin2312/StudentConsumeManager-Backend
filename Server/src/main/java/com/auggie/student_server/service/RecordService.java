package com.auggie.student_server.service;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.mapper.RecordMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;

@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    public boolean addRecord(ConsumeRecord consumeRecord) {
        return recordMapper.addRecord(consumeRecord); // 使用 record 参数
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

    /**
     * 批量导入消费记录，前端上传 CSV 文件
     *
     * @param file 前端上传的 CSV 文件
     * @return 导入是否成功
     */
    public boolean batchImportFromCSV(MultipartFile file) {
        try {
            // 读取 CSV 文件内容
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim());

            // 定义日期时间格式，支持多个常见格式
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

            // 存储所有消费记录
            List<ConsumeRecord> records = new ArrayList<>();

            // 遍历 CSV 文件中的每一行
            for (CSVRecord csvRecord : csvParser) {
                // 将每一行的数据映射到 ConsumeRecord 对象
                ConsumeRecord record = new ConsumeRecord();
                record.setName(csvRecord.get("name"));
                record.setStudentId(csvRecord.get("student_id"));
                record.setAmount(new BigDecimal(csvRecord.get("amount")));
                record.setLocation(csvRecord.get("location"));

                // 获取时间字符串，进行格式处理和解析
                String timeStr = csvRecord.get("time").trim(); // 去掉空格
                System.out.println("开始转换");
                System.out.println("Parsing time: " + timeStr); // 打印出时间字符串，查看格式是否正确

                try {
                    // 如果日期时间格式是 "yyyy/MM/dd HH:mm"（比如 "2024/1/1 12:00"）
                    if (timeStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{2}")) {
                        // 尝试解析日期时间
                        try {
                            record.setTime(LocalDateTime.parse(timeStr, formatter1));
                        } catch (Exception e) {
                            // 如果第一种格式失败，尝试另一种格式
                            record.setTime(LocalDateTime.parse(timeStr, formatter2));
                        }
                    }
                } catch (Exception e) {
                    // 输出失败的日期字符串和异常信息
                    System.out.println("Failed to parse time: " + timeStr);
                    e.printStackTrace();
                    // 重新抛出异常以确保切面捕获
                    throw new IllegalArgumentException("Failed to parse time for record: " + timeStr, e);
                }

                // 设置支付类型和消费类型
                record.setPaymentType(csvRecord.get("payment_type"));
                record.setConsumptionType(csvRecord.get("consumption_type"));

                // 打印每条记录，检查数据
                System.out.println("Inserting record: " + record);

                // 添加到记录列表
                records.add(record);
            }

            // 批量插入到数据库
            return recordMapper.insertBatch(records) > 0; // 调用批量插入方法
        } catch (Exception e) {
            e.printStackTrace();
            // 重新抛出异常，确保切面捕获
            throw new RuntimeException("Error occurred during batch import from CSV", e);
        }

    }
    /**
     * 批量导入消费记录，前端上传 Excel 文件
     *
     * @param file 前端上传的 Excel 文件
     * @return 导入是否成功
     */
    public boolean batchImportFromExcel(MultipartFile file) {
        try {
            // 获取 Excel 文件输入流
            InputStream inputStream = file.getInputStream();

            // 使用 Apache POI 读取 Excel 文件
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 假设数据在第一个 Sheet

            // 定义日期时间格式，支持多个常见格式
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

            // 存储所有消费记录
            List<ConsumeRecord> records = new ArrayList<>();

            // 遍历 Excel 文件中的每一行（跳过标题行）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 将每一行的数据映射到 ConsumeRecord 对象
                ConsumeRecord record = new ConsumeRecord();
                record.setName(getCellValueAsString(row.getCell(0)));
                record.setStudentId(getCellValueAsString(row.getCell(1)));
                record.setAmount(new BigDecimal(getCellValueAsString(row.getCell(2))));
                record.setLocation(getCellValueAsString(row.getCell(3)));

                // 获取时间字符串，进行格式处理和解析
                String timeStr = getCellValueAsString(row.getCell(4)).trim();
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

                // 设置支付类型和消费类型
                record.setPaymentType(getCellValueAsString(row.getCell(5)));
                record.setConsumptionType(getCellValueAsString(row.getCell(6)));

                // 添加到记录列表
                records.add(record);
            }

            // 关闭工作簿
            workbook.close();

            // 批量插入到数据库
            return recordMapper.insertBatch(records) > 0; // 调用批量插入方法
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during batch import from Excel", e);
        }
    }

    /**
     * 辅助方法：获取单元格的字符串值
     *
     * @param cell 单元格
     * @return 单元格的字符串值
     */

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 使用 SimpleDateFormat 格式化日期
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d H:mm");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


}
