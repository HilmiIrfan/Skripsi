package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Department;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.DepartmentRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.DepartmentRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository = new DepartmentRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(DepartmentService.class);

    // public PagedResponse<Department> getAllDepartment(int page, int size) throws
    // IOException {
    // validatePageNumberAndSize(page, size);
    //
    // // Retrieve Polls
    // List<Department> departmentResponse = departmentRepository.findAll(size);
    //
    //
    // return new PagedResponse<>(departmentResponse, departmentResponse.size(),
    // "Successfully get data", 200);
    // }

    public PagedResponse<Department> getDepartments(String schoolId, int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Department> departments;

        if (schoolId != null && !schoolId.isEmpty()) {
            departments = departmentRepository.findBySchoolId(schoolId, size);
        } else {

            departments = departmentRepository.findAll(size);
        }

        return new PagedResponse<>(departments, departments.size(), "Successfully get data", 200);
    }

    public Department createDepartment(DepartmentRequest departmentRequest, String schoolID) throws IOException {
        Department department = new Department();
        department.setSchoolID(schoolID);
        department.setName(departmentRequest.getName());
        department.setDescription(departmentRequest.getDescription());

        return departmentRepository.save(department);
    }

    // public List<Department> getDepartmentsBySchoolId(String schoolId, int size)
    // throws IOException {
    // return departmentRepository.findBySchoolId(schoolId, size);
    // }

    public DefaultResponse<Department> getDepartmentById(String departmentId) throws IOException {
        // Retrieve Department
        Department departmentResponse = departmentRepository.findById(departmentId);
        return new DefaultResponse<>(departmentResponse.isValid() ? departmentResponse : null,
                departmentResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public Department updateDepartment(String departmentId, DepartmentRequest departmentRequest) throws IOException {
        Department department = new Department();

        Department existingDepartment = departmentRepository.findById(departmentId);

        if (existingDepartment == null) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        department.setSchoolID(existingDepartment.getSchoolID());
        department.setName(departmentRequest.getName());
        department.setDescription(departmentRequest.getDescription());

        return departmentRepository.update(departmentId, department);
    }

    public void deleteDepartmentById(String departmentId) throws IOException {
        Department departmentResponse = departmentRepository.findById(departmentId);
        if (departmentResponse.isValid()) {
            departmentRepository.deleteById(departmentId);
        } else {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
