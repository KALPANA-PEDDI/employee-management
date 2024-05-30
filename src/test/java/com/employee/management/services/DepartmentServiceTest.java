package com.employee.management.services;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.models.Department;
import com.employee.management.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @MockBean
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void testAddDepartment() {
        var department = new Department();
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        assertEquals(department, departmentService.addDepartment(new Department()));

        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment() {
        var existingDepartment = new Department();
        existingDepartment.setId(1);
        existingDepartment.setDeptName("IT");
        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(Department.class))).thenReturn(existingDepartment);
        var updatedDepartment = departmentService.updateDepartment(1, new Department(1, "HR"));
        assertEquals(existingDepartment, updatedDepartment);
        assertEquals("HR",updatedDepartment.getDeptName() );
        assertEquals(1,updatedDepartment.getId());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () ->
                departmentService.updateDepartment(2, new Department()));

        verify(departmentRepository, times(1)).findById(2);
        verify(departmentRepository, never()).save(any(Department.class));
    }

}
