package com.attendance.model;

import java.sql.Time;
import java.util.Date;

public class AttendanceRecord {
    private int id;
    private int employeeId;
    private Date date;
    private Time clockIn;
    private Time clockOut;
    private Time breakDuration;

    // ゲッターとセッターを定義する
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getClockIn() {
        return clockIn;
    }

    public void setClockIn(Time clockIn) {
        this.clockIn = clockIn;
    }

    public Time getClockOut() {
        return clockOut;
    }

    public void setClockOut(Time clockOut) {
        this.clockOut = clockOut;
    }

    public Time getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(Time breakDuration) {
        this.breakDuration = breakDuration;
    }
}
