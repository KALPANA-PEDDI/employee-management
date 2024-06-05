package com.employee.management.exceptions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeExceptionHandlerTest {

    @Test
    void testHandleEmployeeNotFoundException() {
        EmployeeExceptionHandler handler = new EmployeeExceptionHandler();
        EmployeeNotFoundException exception = new EmployeeNotFoundException(1L);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleEmployeeNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Employee Not Found with provided employee id: 1", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleEmployeeAlreadyExistsException() {
        EmployeeExceptionHandler handler = new EmployeeExceptionHandler();
        EmployeeAlreadyExistsException exception = new EmployeeAlreadyExistsException(1L);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleEmployeeAlreadyExists(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Employee Already Exists with provided employee id: 1", responseEntity.getBody().getMessage());
    }

}
