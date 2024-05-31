package com.employee.management.exceptions;

public class DepartmentAlreadyExistsException extends RuntimeException {
    private final int departmentId;
    public DepartmentAlreadyExistsException(int id) {
        super(ExceptionConstants.DEPARTMENT_ALREADY_EXISTS+id);
        this.departmentId=id;
    }

    public int getDepartmentId() {
        return departmentId;
    }
}
