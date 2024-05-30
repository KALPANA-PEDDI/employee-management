package com.employee.management.services;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.exceptions.EmployeeNotFoundException;
import com.employee.management.models.Employee;
import com.employee.management.repositories.DepartmentRepository;
import com.employee.management.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return (employeeRespository.findAll());
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRespository.findById(id).orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(id);
                }
        );
    }

    @Override
    public Employee addEmployee(Employee employee) {
        if(Objects.nonNull(employee.getDepartment()))
        departmentRepository.findById(employee.getDepartment().getId()).orElseThrow(() -> {
            log.error("Department not found with provided department id {} " + employee.getDepartment().getId());
            return new DepartmentNotFoundException(employee.getDepartment().getId());
        });
        return employeeRespository.save(employee);
    }

    @Transactional
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        var existingEmployee = employeeRespository.findById(id).orElseThrow(() -> {
            log.error("Employee not found with id: {}", id);
            return new EmployeeNotFoundException(id);
        });
        existingEmployee.setName(employee.getName());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setMobileNo(employee.getMobileNo());
        return employeeRespository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        try {
            if (!employeeRespository.existsById(id)) {
                throw new EmployeeNotFoundException(id);
            }
            employeeRespository.deleteById(id);
        } catch (EmployeeNotFoundException ex) {
            log.error("Error: Deleting employee with id not found : {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error deleting employee with id : {}", id, ex);
        }
    }
}