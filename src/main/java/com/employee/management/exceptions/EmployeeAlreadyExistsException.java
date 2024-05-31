package com.employee.management.exceptions;

public class EmployeeAlreadyExistsException extends RuntimeException {

    private final Long employeeId;

    public EmployeeAlreadyExistsException(Long employeeId) {
        super(ExceptionConstants.EMPLOYEE_ALREADY_EXISTS + employeeId);
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }


}
