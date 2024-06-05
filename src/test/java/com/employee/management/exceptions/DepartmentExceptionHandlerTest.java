package com.employee.management.exceptions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentExceptionHandlerTest {

    @Test
    void testHandleDepartmentNotFoundException() {
        DepartmentExceptionHandler handler = new DepartmentExceptionHandler();
        DepartmentNotFoundException exception = new DepartmentNotFoundException(1);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleDepartmentNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Department Not Found with provided department id: 1", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleDepartmentAlreadyExistsException() {
        DepartmentExceptionHandler handler = new DepartmentExceptionHandler();
        DepartmentAlreadyExistsException exception = new DepartmentAlreadyExistsException(1);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleDepartmentAlreadyExistsException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Department Already Exists with provided department id: 1", responseEntity.getBody().getMessage());

    }

}
