package com.auggie.student_server.controller;

import com.auggie.student_server.entity.ConsumeRecord;
import com.auggie.student_server.service.RecordService;
import com.auggie.student_server.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: auggie
 * @Date: 2024/12/27
 * @Description: RecordController
 * @Version 1.0.0
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    // 新增消费记录
    @PostMapping("/addRecord")
    public ApiResponse<Boolean> addRecord(@RequestBody ConsumeRecord record) throws Exception {
        System.out.println("新增消费记录: " + record);
        boolean result = recordService.addRecord(record);

        if (result) {
            return ApiResponse.success(true);
        } else {
            throw new RuntimeException("新增消费记录失败");
        }
    }

    // 获取所有消费记录
    @GetMapping("/all")
    public ApiResponse<List<ConsumeRecord>> getAllRecords() throws Exception {
        List<ConsumeRecord> records = recordService.getAllRecords();
        return ApiResponse.success(records);
    }

    // 根据查询条件获取消费记录
    @PostMapping("/search")
    public ApiResponse<List<ConsumeRecord>> findByCriteria(@RequestBody Map<String, Object> criteria) throws Exception {
        List<ConsumeRecord> records = recordService.findByCriteria(criteria);
        return ApiResponse.success(records);
    }

    // 根据记录ID查询消费记录
    @GetMapping("/findById/{recordId}")
    public ApiResponse<ConsumeRecord> findById(@PathVariable("recordId") Integer recordId) throws Exception {
        System.out.println("查询消费记录 By ID: " + recordId);
        ConsumeRecord record = recordService.findById(recordId);
        if (record != null) {
            return ApiResponse.success(record);
        } else {
            throw new RuntimeException("记录未找到");
        }
    }

    // 根据ID删除消费记录
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteRecordById(@PathVariable("id") int id) throws Exception {
        boolean success = recordService.deleteRecordById(id);
        if (success) {
            return ApiResponse.success("记录删除成功");
        } else {
            throw new RuntimeException("记录未找到");
        }
    }

    // 根据ID更新消费记录
    @PutMapping("/{id}")
    public ApiResponse<String> updateRecord(@PathVariable Integer id, @RequestBody ConsumeRecord record) throws Exception {
        record.setRecordId(id);
        int updatedRows = recordService.updateRecordById(record);
        if (updatedRows > 0) {
            return ApiResponse.success("记录更新成功");
        } else {
            throw new RuntimeException("记录未找到");
        }
    }

    // 批量删除消费记录
    @DeleteMapping("/batchDelete")
    public ApiResponse<String> batchDelete(@RequestBody List<Integer> ids) throws Exception {
        boolean success = recordService.batchDelete(ids);
        if (success) {
            return ApiResponse.success("批量删除成功");
        } else {
            throw new RuntimeException("未找到对应的记录");
        }
    }

    // 批量导入消费记录
    @PostMapping("/import")
    public ApiResponse<String> importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        boolean success = recordService.batchImportFromExcel(file);
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传的文件为空");
        }

        if (success) {
            return ApiResponse.success("批量导入成功");
        } else {
            throw new RuntimeException("导入失败");
        }
    }


    // 发送 Excel 文件到前端
    @GetMapping("/exportExcel")
    public ResponseEntity<InputStreamResource> exportExcel() throws IOException {
        // 假设你有一个生成 Excel 文件的方法，并将其保存在服务器上
        String filePath = "/Users/lijiahe/IdeaProjects/StudentConsumeManager-Backend/Server/src/main/java/com/auggie/student_server/static/excel插入数据_说中文.xlsx";

        // 创建文件对象
        File file = new File(filePath);
        // 将文件作为流返回给前端
        FileInputStream fileInputStream = new FileInputStream(file);

        // 设置文件类型和头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(fileInputStream));
    }

    // 测试日志接口
    @GetMapping("/log")
    public ApiResponse<Integer> log() {
        System.out.println("RecordController log");
        return ApiResponse.success(1);
    }



    // 测试异常抛出
    @GetMapping("/throw")
    public ApiResponse<String> throwException() {
        throw new RuntimeException("测试异常");
    }

}
