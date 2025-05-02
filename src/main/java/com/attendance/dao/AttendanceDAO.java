package com.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.attendance.model.AttendanceRecord;

public class AttendanceDAO {

    private final String dbURL  = "jdbc:mysql://192.168.0.154:3306/demoDB";
    private final String dbUser = "remote_user";
    private final String dbPass = "password";

    /**
     * DB接続を取得する共通メソッド
     */
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    /**
     * ResultSet から AttendanceRecord を生成（共通化）
     */
    private AttendanceRecord mapResultSetToRecord(ResultSet rs, int employeeId) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setId(rs.getInt("record_id"));
        record.setEmployeeId(employeeId);
        record.setDate(rs.getDate("work_date"));
        record.setClockIn(rs.getTime("clock_in"));
        record.setClockOut(rs.getTime("clock_out"));
        record.setBreakOut(rs.getTime("break_out"));
        record.setBreakIn (rs.getTime("break_in"));
        record.setBreakDuration(rs.getTime("break_duration"));
        return record;
    }

    /**
     * 従業員IDと月(YYYY-MM)で複数レコードを取得
     */
    public List<AttendanceRecord> getRecordsByEmployeeAndMonth(int employeeId, String month) throws Exception {
        List<AttendanceRecord> records = new ArrayList<>();
        try (Connection conn = getConnection()) {
        	String sql = "SELECT record_id, work_date, clock_in, clock_out, break_out, break_in, break_duration "
                       + "FROM attendance_records "
                       + "WHERE employee_id = ? AND DATE_FORMAT(work_date, '%Y-%m') = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, month);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AttendanceRecord record = mapResultSetToRecord(rs, employeeId);
                        records.add(record);
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
        try (Connection conn = getConnection()) {
            String sql = "SELECT record_id, work_date, clock_in, clock_out, break_out, break_in, break_duration "
                       + "FROM attendance_records "
                       + "WHERE employee_id = ? AND work_date = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, date);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        record = mapResultSetToRecord(rs, employeeId);
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
        try (Connection conn = getConnection()) {
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
                                 String workDate,
                                 Timestamp clockIn,
                                 Timestamp clockOut,
                                 Time breakDuration) throws Exception {
        try (Connection conn = getConnection()) {
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
    
    public void deleteAttendance(int recordId) throws Exception {
        String sql = "DELETE FROM attendance_records WHERE record_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recordId);
            stmt.executeUpdate();
        }
    }

    /**
     * 打刻記録を保存（統合版）
     */
    public void insertPunch(int employeeId, String action, LocalDateTime time) throws Exception {
        LocalDate date = time.toLocalDate();
        String dateStr = date.toString();
        Timestamp timestamp = Timestamp.valueOf(time);

        try (Connection conn = getConnection()) {
            // すでに当日のレコードがあるかを確認
            String selectSql = "SELECT record_id, clock_in, clock_out FROM attendance_records "
                             + "WHERE employee_id = ? AND work_date = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, dateStr);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int recordId = rs.getInt("record_id");
                        Timestamp clockIn = rs.getTimestamp("clock_in");
                        Timestamp clockOut = rs.getTimestamp("clock_out");

                        if ("出勤".equals(action) && clockIn == null) {
                            updateAttendance(recordId, timestamp, clockOut, null);
                        } else if ("退勤".equals(action) && clockOut == null) {
                            updateAttendance(recordId, clockIn, timestamp, null);
                        }
                    } else {
                        Timestamp inTime = null;
                        Timestamp outTime = null;
                        if ("出勤".equals(action)) {
                            inTime = timestamp;
                        } else if ("退勤".equals(action)) {
                            outTime = timestamp;
                        }
                        insertAttendance(employeeId, dateStr, inTime, outTime, null);
                    }
                }
            }
        }
    }
}
