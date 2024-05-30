package com.employee.management.services;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.models.Department;
import com.employee.management.repositories.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        var existingDepartment = departmentRepository.findById(id).orElseThrow(() -> {
            log.error("Department not found with provided id {} " + id);
            return new DepartmentNotFoundException(id);
        });
        existingDepartment.setDeptName(department.getDeptName());
        return departmentRepository.save(existingDepartment);

    }
}
