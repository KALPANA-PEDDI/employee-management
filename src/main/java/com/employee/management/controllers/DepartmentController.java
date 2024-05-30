package com.employee.management.controllers;

import com.employee.management.exceptions.DepartmentNotFoundException;
import com.employee.management.models.Department;
import com.employee.management.services.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Object> addDepartment(@RequestBody Department department, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Error: " + bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.addDepartment(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDepartment(@PathVariable int id, @RequestBody Department newDepartmentData, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getAllErrors());
        }
        try {
            var updatedDept = departmentService.updateDepartment(id, newDepartmentData);
            return ResponseEntity.ok(updatedDept);
        } catch (DepartmentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
