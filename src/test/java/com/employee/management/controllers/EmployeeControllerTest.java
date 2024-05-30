package com.employee.management.controllers;

import com.employee.management.models.Department;
import com.employee.management.models.Employee;
import com.employee.management.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    EmployeeController employeeController;

    private Employee employee;

    private Department department;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        employeeController = new EmployeeController(employeeService);
        employee = new Employee();
        department = new Department();
        employee.setId(1L);
        employee.setName("Kalpana");
        employee.setAddress("Guntur");
        employee.setMobileNo("9505932689");
        department.setDeptName("IT");
        department.setId(1);
        employee.setDepartment(department);
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kalpana"))
                .andExpect(jsonPath("$[0].department.deptName").value("IT"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(8L)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 8L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kalpana"))
                .andExpect(jsonPath("$.department.deptName").value("IT"));

    }

    @Test
    void testAddEmployee() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\":1,\n" +
                                "    \"name\":\"Kalpana\",\n" +
                                "    \"department\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"deptName\": \"IT\"\n" +
                                "    }," +
                                "    \"mobileNo\":\"9505932689\",\n" +
                                "    \"address\":\"Guntur\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(employeeService, times(1)).addEmployee(employee);
    }

    @Test
    void testUpdateEmployee() throws Exception {
        mockMvc.perform(put("/employees/{id}", 8124006L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\":1,\n" +
                                "    \"name\":\"Kalpana\",\n" +
                                "    \"department\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"deptName\": \"IT\"\n" +
                                "    }," +
                                "    \"mobileNo\":\"9505932689\",\n" +
                                "    \"address\":\"Guntur\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).updateEmployee(8124006L, employee);
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}



