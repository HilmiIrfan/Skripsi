package com.doyatama.university.controller;

import com.doyatama.university.model.RPSDetail;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.RPSDetailRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExcelUploadService;
import com.doyatama.university.service.RPSDetailService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rps-detail")
public class RPSDetailController {
    private RPSDetailService rpsDetailService = new RPSDetailService();

    @GetMapping
    public PagedResponse<RPSDetail> getRPSDetails(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size, @RequestParam(value = "rpsID", defaultValue = "*") String rpsID) throws IOException {
        return rpsDetailService.getAllRPSDetail(page, size, rpsID);
    }
    @CrossOrigin
    @PostMapping("/import")
        public ResponseEntity<?> importRPSDetailFromExcel(@RequestParam("file") MultipartFile file) {
            if (!ExcelUploadService.isValidExcelFile(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file format. Please upload an Excel file.");
            }

            try {
                List<RPSDetail> rpsDetailList =  rpsDetailService.importRPSDetailFromExcel(file);
                return ResponseEntity.status(HttpStatus.OK).body(rpsDetailList);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import data from Excel file.");
            }
    }
    @PostMapping
    public ResponseEntity<?> createRPSDetail(@Valid @RequestBody RPSDetailRequest rpsDetailRequest) throws IOException {
        RPSDetail rpsDetail = rpsDetailService.createRPSDetail(rpsDetailRequest);

        if(rpsDetail == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{rpsDetailId}")
                    .buildAndExpand(rpsDetail.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "RPSDetail Created Successfully"));
        }
    }

    @GetMapping("/{rpsDetailId}")
    public DefaultResponse<RPSDetail> getRPSDetailById(@PathVariable String rpsDetailId) throws IOException {
        return rpsDetailService.getRPSDetailById(rpsDetailId);
    }


    @PutMapping("/{rpsDetailId}")
    public ResponseEntity<?> updateRPSDetail(@PathVariable String rpsDetailId,
                                       @Valid @RequestBody RPSDetailRequest rpsDetailRequest) throws IOException {
        RPSDetail rpsDetail = rpsDetailService.updateRPSDetail(rpsDetailId, rpsDetailRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{rpsDetailId}")
                .buildAndExpand(rpsDetail.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "RPSDetail Updated Successfully"));
    }

    @DeleteMapping("/{rpsDetailId}")
    public HttpStatus deleteRPSDetail(@PathVariable (value = "rpsDetailId") String rpsDetailId) throws IOException {
        rpsDetailService.deleteRPSDetailById(rpsDetailId);
        return HttpStatus.FORBIDDEN;
    }
}
