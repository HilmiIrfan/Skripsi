package com.doyatama.university.controller;

import com.doyatama.university.model.LinguisticValue;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LinguisticValueRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.LinguisticValueService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.doyatama.university.config.PathConfig;
/**
 * @author alfa
 */
@RestController
@RequestMapping("/api/linguistic-value")
public class LinguisticValueController {
    private LinguisticValueService linguisticValueService = new LinguisticValueService();

    @GetMapping
    public PagedResponse<LinguisticValue> getLinguisticValues(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return linguisticValueService.getAllLinguisticValue(page,size);
    }

    @PostMapping
    public ResponseEntity<?> createLinguisticValue(@RequestParam(value = "file", required = false) MultipartFile file,
                                                @ModelAttribute LinguisticValueRequest linguisticValueRequest) {
        if (file == null || file.isEmpty()) {
            try {
                LinguisticValue linguisticValue = linguisticValueService.createLinguisticValue(linguisticValueRequest, "");

                if(linguisticValue == null){
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{linguisticValueId}")
                            .buildAndExpand(linguisticValue.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Linguistic Value Created Successfully"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        } else {
            try {
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String timestamp = String.valueOf(System.currentTimeMillis());
                String uuid = UUID.randomUUID().toString();
                String newFileName = "file_" + timestamp + "_" + uuid;
                String filePath = PathConfig.storagePath + "/" + newFileName + fileExtension;
                File newFile = new File(filePath);

                file.transferTo(newFile);

                String localPath = newFile.getAbsolutePath();
                String uri = "hdfs://h-primary:9000";
                String hdfsDir = "hdfs://h-primary:9000/linguistic-values/" + newFileName + fileExtension;
                Configuration configuration = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(uri), configuration);
                fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
                String savePath = "webhdfs/v1/linguistic-values/"+ newFileName + fileExtension +"?op=OPEN";

                newFile.delete();
                LinguisticValue linguisticValue = linguisticValueService.createLinguisticValue(linguisticValueRequest, savePath);

                if(linguisticValue == null){
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{linguisticValueId}")
                            .buildAndExpand(linguisticValue.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "Linguistic Value Created Successfully"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        }
    }

    @GetMapping("/{linguisticValueId}")
    public DefaultResponse<LinguisticValue> getLinguisticValueById(@PathVariable String linguisticValueId) throws IOException {
        return linguisticValueService.getLinguisticValueById(linguisticValueId);
    }
    @PutMapping("/{linguisticValueId}")
    public ResponseEntity<?> updateLinguisticValue(@PathVariable String linguisticValueId,
                                          @Valid @RequestBody LinguisticValueRequest linguisticValueRequest) throws IOException {
        LinguisticValue linguisticValue = linguisticValueService.updateLinguisticValue(linguisticValueId, linguisticValueRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{linguisticValueId}")
                .buildAndExpand(linguisticValue.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Linguistic Value Updated Successfully"));
    }
    @DeleteMapping("/{linguisticValueId}")
    public ResponseEntity<?> deleteLinguisticValue(@PathVariable String linguisticValueId) throws IOException {
        linguisticValueService.deleteLinguisticValueById(linguisticValueId);

        return new ResponseEntity<>(new ApiResponse(true, "Linguistic Value Deleted Successfully"), HttpStatus.OK);
    }
}
