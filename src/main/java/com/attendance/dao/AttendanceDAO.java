package com.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.attendance.model.AttendanceRecord;

public class AttendanceDAO {

    private final String dbURL  = "jdbc:mysql://192.168.0.154:3306/demoDB";
    private final String dbUser = "remote_user";
    private final String dbPass = "password";

    /**
     * 従業員IDと月(YYYY-MM)で複数レコードを取得
     */
    public List<AttendanceRecord> getRecordsByEmployeeAndMonth(int employeeId, String month) throws Exception {
        List<AttendanceRecord> records = new ArrayList<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql = "SELECT record_id, work_date, clock_in, clock_out, break_duration "
                       + "FROM attendance_records "
                       + "WHERE employee_id = ? AND DATE_FORMAT(work_date, '%Y-%m') = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, month);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AttendanceRecord r = new AttendanceRecord();
                        r.setId(rs.getInt("record_id"));
                        r.setEmployeeId(employeeId);
                        r.setDate(rs.getDate("work_date"));
                        r.setClockIn(rs.getTime("clock_in"));
                        r.setClockOut(rs.getTime("clock_out"));
                        r.setBreakDuration(rs.getTime("break_duration"));
                        records.add(r);
                    }
                }
            }
        }
        return records;
    }

    /**
     * 従業員IDと日付(yyyy-MM-dd)で単一レコードを取得
     */
    public AttendanceRecord getAttendanceRecordByEmployeeAndDate(int employeeId, String date) throws Exception {
        AttendanceRecord record = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql = "SELECT record_id, work_date, clock_in, clock_out, break_duration "
                       + "FROM attendance_records "
                       + "WHERE employee_id = ? AND work_date = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, date);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        record = new AttendanceRecord();
                        record.setId(rs.getInt("record_id"));
                        record.setEmployeeId(employeeId);
                        record.setDate(rs.getDate("work_date"));
                        record.setClockIn(rs.getTime("clock_in"));
                        record.setClockOut(rs.getTime("clock_out"));
                        record.setBreakDuration(rs.getTime("break_duration"));
                    }
                }
            }
        }
        return record;
    }

    /**
     * record_id で更新
     */
    public void updateAttendance(int recordId, Timestamp clockIn, Timestamp clockOut, Time breakDuration) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql = "UPDATE attendance_records "
                       + "SET clock_in = ?, clock_out = ?, break_duration = ? "
                       + "WHERE record_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setTimestamp(1, clockIn);
                ps.setTimestamp(2, clockOut);
                ps.setTime(3, breakDuration);
                ps.setInt(4, recordId);
                ps.executeUpdate();
            }
        }
    }

    /**
     * 新規レコードを挿入
     */
    public void insertAttendance(int employeeId,
                                 String workDate,    // "yyyy-MM-dd"
                                 Timestamp clockIn,
                                 Timestamp clockOut,
                                 Time breakDuration) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql = "INSERT INTO attendance_records "
                       + "(employee_id, work_date, clock_in, clock_out, break_duration) "
                       + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, workDate);
                ps.setTimestamp(3, clockIn);
                ps.setTimestamp(4, clockOut);
                ps.setTime(5, breakDuration);
                ps.executeUpdate();
            }
        }
    }
}
