package org.school.housing.enums;

public enum CategoryId {
    ResidentService(1),
    Salary(2),
    Purchases(3),
    Maintenance(4);

    public final int id;

    CategoryId(int id) {
        this.id = id;
    }
}
