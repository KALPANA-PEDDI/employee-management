package com.employee.management.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    private final Long employeeId;

    public EmployeeNotFoundException(Long employeeId) {
        super(ExceptionConstants.EMPLOYEE_NOT_FOUND + employeeId);
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
