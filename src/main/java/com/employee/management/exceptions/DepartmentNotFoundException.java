package com.employee.management.exceptions;

public class DepartmentNotFoundException extends RuntimeException {

    private final int departmentId;

    public DepartmentNotFoundException(int departmentId) {
        super(ExceptionConstants.DEPARTMENT_NOT_FOUND + departmentId);
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

}
