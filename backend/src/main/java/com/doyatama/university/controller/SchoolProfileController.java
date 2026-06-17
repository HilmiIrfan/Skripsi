package com.doyatama.university.controller;

import com.doyatama.university.config.PathConfig;
import com.doyatama.university.model.SchoolProfile;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SchoolProfileRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SchoolProfileService;
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
@RequestMapping("/api/school-profile")
public class SchoolProfileController {
    private SchoolProfileService schoolProfileService = new SchoolProfileService();

    @GetMapping
    public PagedResponse<SchoolProfile> getSchoolProfiles(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "schoolId", defaultValue = "*") String schoolId) throws IOException {
        return schoolProfileService.getAllProfile(page, size, schoolId);
    }

    @PostMapping
    public ResponseEntity<?> createSchoolProfile(@RequestPart(value = "file", required = false) MultipartFile file,
            @ModelAttribute SchoolProfileRequest profileRequest) throws IOException {

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
                String uri = "hdfs://hadoop-primary:9000";
                String hdfsDir = "hdfs://hadoop-primary:9000/profiles/" + newFileName + fileExtension;
                Configuration configuration = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(uri), configuration);
                fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
                String savePath = "webhdfs/v1/profiles/" + newFileName + fileExtension + "?op=OPEN";

                newFile.delete();
                SchoolProfile profile = schoolProfileService.createSchoolProfile(profileRequest, savePath);

                if (profile == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{profileId}")
                            .buildAndExpand(profile.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "SchoolProfile Created Successfully"));
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
                SchoolProfile profile = schoolProfileService.createSchoolProfile(profileRequest, "");

                if (profile == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Please check relational ID"));
                } else {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{profileId}")
                            .buildAndExpand(profile.getId()).toUri();

                    return ResponseEntity.created(location)
                            .body(new ApiResponse(true, "SchoolProfile Created Successfully"));
                }
            } catch (IOException e) {
                // Penanganan kesalahan saat menyimpan file
                e.printStackTrace();
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
            }
        }
    }

    @GetMapping("/{profileId}")
    public DefaultResponse<SchoolProfile> getSchoolProfileById(@PathVariable String profileId) throws IOException {
        return schoolProfileService.getSchoolProfileById(profileId);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<?> updateSchoolProfile(@PathVariable String profileId,
            @RequestParam("file") MultipartFile file, @ModelAttribute SchoolProfileRequest profileRequest)
            throws IOException {
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
            String uri = "hdfs://hadoop-primary:9000";
            String hdfsDir = "hdfs://hadoop-primary:9000/profiles/" + newFileName + fileExtension;
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(uri), configuration);
            fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));
            String savePath = "webhdfs/v1/profiles/" + newFileName + fileExtension + "?op=OPEN";

            newFile.delete();
            SchoolProfile profile = schoolProfileService.updateSchoolProfile(profileId, profileRequest, savePath);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{profileId}")
                    .buildAndExpand(profile.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "SchoolProfile Updated Successfully"));
        } catch (IOException e) {
            // Penanganan kesalahan saat menyimpan file
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Cannot Upload File into Hadoop"));
        }
    }

    @DeleteMapping("/{profileId}")
    public HttpStatus deleteSchoolProfile(@PathVariable(value = "profileId") String profileId) throws IOException {
        schoolProfileService.deleteSchoolProfileById(profileId);
        return HttpStatus.FORBIDDEN;
    }
}
