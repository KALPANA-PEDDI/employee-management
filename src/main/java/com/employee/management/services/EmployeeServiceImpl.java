package com.employee.management.services;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.exceptions.EmployeeAlreadyExistsException;
import com.employee.management.exceptions.EmployeeNotFoundException;
import com.employee.management.exceptions.ExceptionConstants;
import com.employee.management.models.Department;
import com.employee.management.models.Employee;
import com.employee.management.repositories.DepartmentRepository;
import com.employee.management.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRespository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRespository, DepartmentRepository departmentRepository) {
        this.employeeRespository = employeeRespository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Get all employees");
        return (employeeRespository.findAll());
    }

    @Override
    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee details with employee id " + id);
        return employeeRespository.findById(id).orElseThrow(() -> {
                    log.error(ExceptionConstants.EMPLOYEE_NOT_FOUND + id);
                    throw new EmployeeNotFoundException(id);
                }
        );
    }

    @Override
    public Employee addEmployee(Employee employee) {
        log.info("Adding new employee with employee id" + employee.getId());
        if (employeeRespository.existsById(employee.getId())) {
            throw new EmployeeAlreadyExistsException(employee.getId());
        } else {
            if (Objects.nonNull(employee.getDepartment())) {
                Department department = departmentRepository.findById(employee.getDepartment().getId())
                        .orElseThrow(() -> {
                            log.error(ExceptionConstants.DEPARTMENT_NOT_FOUND + employee.getDepartment().getId());
                            return new DepartmentNotFoundException(employee.getDepartment().getId());
                        });
                employee.setDepartment(department);
            }
            return employeeRespository.save(employee);
        }
    }

    @Transactional
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        log.info("Updating employee with employee id " + id);
        var existingEmployee = employeeRespository.findById(id).orElseThrow(() -> {
            log.error(ExceptionConstants.EMPLOYEE_NOT_FOUND + id);
            throw new EmployeeNotFoundException(id);
        });
        existingEmployee.setName(employee.getName());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setMobileNo(employee.getMobileNo());
        return employeeRespository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with employee id " + id);
        if (!employeeRespository.existsById(id)) {
            log.error(ExceptionConstants.EMPLOYEE_NOT_FOUND + id);
            throw new EmployeeNotFoundException(id);
        }
        employeeRespository.deleteById(id);
    }
}