package com.employee.management.services;

import com.employee.management.exceptions.EmployeeNotFoundException;
import com.employee.management.models.Employee;
import com.employee.management.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @MockBean
    private Logger log;

    @Test
    void testGetEmployees() {
        Mockito.when(employeeRepository.findAll()).thenReturn(List.of(new Employee(), new Employee()));
        assertEquals(2, employeeService.getAllEmployees().size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);
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
    void testAddEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        assertEquals(employee, employeeService.addEmployee(new Employee()));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        assertEquals(existingEmployee, employeeService.updateEmployee(1L, new Employee()));

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
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
        verify(log, never()).error(anyString());
    }

    @Test
    void deleteEmployee_WhenEmployeeNotExists_ShouldLogError() {
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, never()).deleteById(anyLong());

    }


}
