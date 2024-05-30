package com.employee.management.controllers;

import com.employee.management.models.Department;
import com.employee.management.models.Employee;
import com.employee.management.services.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @MockBean
    private DepartmentService departmentService;

    private MockMvc mockMvc;
    private Department department;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        department = new Department(1, "IT");
    }

    @Test
    void testAddDepartment() throws Exception {
        var department = new Department(1, "IT");

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\":1,\n" +
                                "    \"deptName\":\"IT\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());


        verify(departmentService, times(1)).addDepartment(department);
    }

    @Test
    void testUpdateDepartment() throws Exception {
        var department = new Department(1, "IT");

        mockMvc.perform(put("/departments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\":1,\n" +
                                "    \"deptName\":\"IT\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ;

        verify(departmentService, times(1)).updateDepartment(1, department);
    }


}
