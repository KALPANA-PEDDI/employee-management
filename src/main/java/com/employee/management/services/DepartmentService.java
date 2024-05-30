package com.employee.management.services;

import com.employee.management.models.Department;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentService {

    Department addDepartment(Department department);

    Department updateDepartment(int id, Department department);
}
