package com.doyatama.university.controller;

import com.doyatama.university.config.PathConfig;
import com.doyatama.university.model.Question;
import com.doyatama.university.payload.*;
import com.doyatama.university.service.QuestionService;
import com.doyatama.university.util.AppConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.commons.io.IOUtils;

import java.util.Base64;

@CrossOrigin(origins = "http://localhost:3000//rps#/rps/8ed53076-d779-4f03-8c2b-9558d0d33e18/f7fa2143-6806-4556-b9a4-afec20fce867")
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private QuestionService questionService = new QuestionService();

    @CrossOrigin
    @GetMapping

    public PagedResponse<Question> getQuestions(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "rpsDetailID", defaultValue = "*") String rpsDetailID,
            @RequestParam(value = "rpsID", defaultValue = "*") String rpsID) throws IOException {
        return questionService.getAllQuestion(page, size, rpsDetailID, rpsID);
    }

    @CrossOrigin
    @PostMapping

    public ResponseEntity<?> createQuestion(@RequestParam(value = "file", required = false) MultipartFile file,
            @ModelAttribute QuestionRequest questionRequest) throws IOException {

        if (file == null || file.isEmpty()) {
            // Tidak ada input file
            // upload file
            try {
                Question question = questionService.createQuestion(questionRequest, "");

                if (question == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{questionId}")
                            .buildAndExpand(question.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Question Created Successfully"));
                }
            } catch (IOException e) {
                // Error handling when saving file
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        } else {
            // upload file
            try {
                // Mendapatkan nama file asli
                String originalFileName = file.getOriginalFilename();

                // Mendapatkan ekstensi file
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Mendapatkan timestamp saat ini
                String timestamp = String.valueOf(System.currentTimeMillis());

                // Membuat UUID baru
                String uuid = UUID.randomUUID().toString();

                // Menggabungkan timestamp dan UUID
                String newFileName = "file_" + timestamp + "_" + uuid;
                String filePath = PathConfig.storagePath + "/" + newFileName + fileExtension;
                File newFile = new File(filePath);

                // Menyimpan file ke lokasi yang ditentukan di server
                file.transferTo(newFile);

                // Mendapatkan local path dari file yang disimpan
                String localPath = newFile.getAbsolutePath();
                String uri = "hdfs://h-primary:9000";
                String hdfsDir = "hdfs://h-primary:9000/questions/" + newFileName + fileExtension;
                Configuration configuration = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(uri), configuration);
                fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
                String savePath = "webhdfs/v1/questions/" + newFileName + fileExtension + "?op=OPEN";

                newFile.delete();
                Question question = questionService.createQuestion(questionRequest, savePath);

                if (question == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{questionId}")
                            .buildAndExpand(question.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Question Created Successfully"));
                }
            } catch (IOException e) {
                // Penanganan kesalahan saat menyimpan file
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        }

    }

    @CrossOrigin
    @GetMapping("/image/{imageName}")
    public ResponseEntity<String> getImage(@PathVariable String imageName) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        FileSystem fs = FileSystem.get(conf);
        Path hdfspath = new Path("/user/hadoop/" + imageName);
        FSDataInputStream inputStream = fs.open(hdfspath);
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return ResponseEntity.ok(base64Image);
    }

    @GetMapping("/{questionId}")
    public DefaultResponse<Question> getQuestionById(@PathVariable String questionId) throws IOException {
        return questionService.getQuestionById(questionId);
    }

    @GetMapping("/paged/{questionId}")
    public PagedResponse<Question> getQuestionByIdPaged(@PathVariable String questionId) throws IOException {
        return questionService.getQuestionByIdPaged(questionId);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable(value = "questionId") String questionId,
            @Valid @RequestBody QuestionRequest questionRequest) {
        try {
            Question question = questionService.updateQuestion(questionId, questionRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{questionId}")
                    .buildAndExpand(question.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Question Updated Successfully"));
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Error Updating Question"));
        }
    }

    // @PutMapping("/{questionId}")
    // public ResponseEntity<?> updateQuestion(@PathVariable String questionId,
    // @RequestParam("file") MultipartFile file, @ModelAttribute QuestionRequest
    // questionRequest) throws IOException {
    // // upload file
    // try {
    // // Mendapatkan nama file asli
    // String originalFileName = file.getOriginalFilename();

    // // Mendapatkan ekstensi file
    // String fileExtension =
    // originalFileName.substring(originalFileName.lastIndexOf("."));

    // // Mendapatkan timestamp saat ini
    // String timestamp = String.valueOf(System.currentTimeMillis());

    // // Membuat UUID baru
    // String uuid = UUID.randomUUID().toString();

    // // Menggabungkan timestamp dan UUID
    // String newFileName = "file_" + timestamp + "_" + uuid;
    // String filePath = PathConfig.storagePath + "/" + newFileName + fileExtension;
    // File newFile = new File(filePath);

    // // Menyimpan file ke lokasi yang ditentukan di server
    // file.transferTo(newFile);

    // // Mendapatkan local path dari file yang disimpan
    // String localPath = newFile.getAbsolutePath();
    // String uri = "hdfs://h-primary:9000";
    // String hdfsDir = "hdfs://h-primary:9000/questions/" + newFileName +
    // fileExtension;
    // Configuration configuration = new Configuration();
    // FileSystem fs = FileSystem.get(URI.create(uri), configuration);
    // fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
    // String savePath = "webhdfs/v1/questions/"+ newFileName + fileExtension
    // +"?op=OPEN";

    // newFile.delete();
    // Question question = questionService.updateQuestion(questionId,
    // questionRequest, savePath);

    // URI location = ServletUriComponentsBuilder
    // .fromCurrentRequest().path("/{questionId}")
    // .buildAndExpand(question.getId()).toUri();

    // return ResponseEntity.created(location)
    // .body(new ApiResponse(true, "Question Updated Successfully"));
    // } catch (IOException e) {
    // // Penanganan kesalahan saat menyimpan file
    // e.printStackTrace();
    // return ResponseEntity.badRequest()
    // .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
    // }

    // }

    @DeleteMapping("/{questionId}")
    public HttpStatus deleteQuestion(@PathVariable(value = "questionId") String questionId) throws IOException {
        questionService.deleteQuestionById(questionId);
        return HttpStatus.FORBIDDEN;
    }
}
