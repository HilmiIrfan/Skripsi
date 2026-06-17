package com.doyatama.university.controller;

import com.doyatama.university.config.PathConfig;
import com.doyatama.university.model.Answer;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AnswerRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.AnswerService;
import com.doyatama.university.util.AppConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {
    private AnswerService answerService = new AnswerService();

    @GetMapping
    public PagedResponse<Answer> getAnswers(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "questionID", defaultValue = "*") String questionID) throws IOException {
        return answerService.getAllAnswer(page, size, questionID);
    }

    @PostMapping
    public ResponseEntity<?> createAnswer(@RequestPart(value = "file", required = false) MultipartFile file,
            @ModelAttribute AnswerRequest answerRequest) throws IOException {

        if (file != null && !file.isEmpty()) {
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
                String hdfsDir = "hdfs://h-primary:9000/answers/" + newFileName + fileExtension;
                Configuration configuration = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(uri), configuration);
                fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
                String savePath = "webhdfs/v1/answers/" + newFileName + fileExtension + "?op=OPEN";

                newFile.delete();
                Answer answer = answerService.createAnswer(answerRequest, savePath);

                if (answer == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{answerId}")
                            .buildAndExpand(answer.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Answer Created Successfully"));
                }
            } catch (IOException e) {
                // Penanganan kesalahan saat menyimpan file
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        } else {
            // Tidak ada input file
            try {
                Answer answer = answerService.createAnswer(answerRequest, "");

                if (answer == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{answerId}")
                            .buildAndExpand(answer.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Answer Created Successfully"));
                }
            } catch (IOException e) {
                // Penanganan kesalahan saat menyimpan file
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        }

    }

    @GetMapping("/{answerId}")
    public DefaultResponse<Answer> getAnswerById(@PathVariable String answerId) throws IOException {
        return answerService.getAnswerById(answerId);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable String answerId,
            @RequestParam("file") MultipartFile file, @ModelAttribute AnswerRequest answerRequest) throws IOException {
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
            String hdfsDir = "hdfs://h-primary:9000/answers/" + newFileName + fileExtension;
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(uri), configuration);
            fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
            String savePath = "webhdfs/v1/answers/" + newFileName + fileExtension + "?op=OPEN";

            newFile.delete();
            Answer answer = answerService.updateAnswer(answerId, answerRequest, savePath);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{answerId}")
                    .buildAndExpand(answer.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Answer Updated Successfully"));
        } catch (IOException e) {
            // Penanganan kesalahan saat menyimpan file
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
        }

    }

    @DeleteMapping("/{answerId}")
    public HttpStatus deleteAnswer(@PathVariable(value = "answerId") String answerId) throws IOException {
        answerService.deleteAnswerById(answerId);
        return HttpStatus.FORBIDDEN;
    }
}
