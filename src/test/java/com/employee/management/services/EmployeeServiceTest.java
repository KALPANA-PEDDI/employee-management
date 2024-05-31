package com.employee.management.services;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.exceptions.EmployeeAlreadyExistsException;
import com.employee.management.exceptions.EmployeeNotFoundException;
import com.employee.management.exceptions.ExceptionConstants;
import com.employee.management.models.Department;
import com.employee.management.models.Employee;
import com.employee.management.repositories.DepartmentRepository;
import com.employee.management.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    EmployeeServiceImpl employeeService;

    Employee employee;

    @MockBean
    private Logger log;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);

        employeeService = new EmployeeServiceImpl(employeeRepository, departmentRepository);
    }

    @Test
    void testGetEmployees() {
        Mockito.when(employeeRepository.findAll()).thenReturn(List.of(new Employee(), new Employee()));
        assertEquals(2, employeeService.getAllEmployees().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertEquals(1L, employeeService.getEmployeeById(1L).getId());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(2L);
        });

        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
    void testAddEmployeeWithValidDepartment() {
        Department department = new Department();
        department.setId(1);
        when(departmentRepository.findById(anyInt())).thenReturn(java.util.Optional.of(department));

        employee.setDepartment(department);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee savedEmployee = employeeService.addEmployee(employee);

        verify(departmentRepository, times(1)).findById(1);

        verify(employeeRepository, times(1)).save(employee);

        assertEquals(employee, savedEmployee);
    }

    @Test
    void addEmployee_employeeAlreadyExists_shouldThrowException() {

        when(employeeRepository.existsById(employee.getId())).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> {
            employeeService.addEmployee(employee);
        });
        verify(log, never()).error(anyString());
    }

    @Test
    void testAddEmployeeWithInvalidDepartment() {
        when(departmentRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        Department department = new Department();
        department.setId(1);
        employee.setDepartment(department);

        assertThrows(DepartmentNotFoundException.class, () -> {
            employeeService.addEmployee(employee);
        });

        verify(departmentRepository, times(1)).findById(1);

        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        assertEquals(employee, employeeService.updateEmployee(1L, new Employee()));

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.updateEmployee(2L, new Employee()));

        verify(employeeRepository, times(1)).findById(2L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
        verify(log, never()).error(anyString());
    }

    @Test
    void deleteEmployee_WhenEmployeeNotExists() {
        when(employeeRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.deleteEmployee(2L));

        verify(employeeRepository, never()).deleteById(anyLong());

    }


}
