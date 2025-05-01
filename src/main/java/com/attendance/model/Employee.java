package com.attendance.model;

public class Employee {
    private String id;
    private String name;
    // 必要に応じて他のフィールドも追加

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
