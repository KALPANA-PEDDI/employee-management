package com.employee.management.services;

import com.employee.management.exceptions.DepartmentAlreadyExistsException;
import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.exceptions.ExceptionConstants;
import com.employee.management.models.Department;
import com.employee.management.repositories.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Department addDepartment(Department department) {
        log.info("Adding department with department id " + department.getId());
        if (departmentRepository.existsById(department.getId())) {
            throw new DepartmentAlreadyExistsException(department.getId());
        } else
            return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        log.info("Updating department with department id " + id);
        var existingDepartment = departmentRepository.findById(id).orElseThrow(() -> {
            log.error(ExceptionConstants.DEPARTMENT_NOT_FOUND + id);
            return new DepartmentNotFoundException(id);
        });
        existingDepartment.setDeptName(department.getDeptName());
        return departmentRepository.save(existingDepartment);

    }
}
