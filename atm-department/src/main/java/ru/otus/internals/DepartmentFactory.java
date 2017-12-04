package ru.otus.internals;

public class DepartmentFactory {

    private DepartmentFactory() {}

    public static ATMDepartment create() {
        return new Department();
    }
}
