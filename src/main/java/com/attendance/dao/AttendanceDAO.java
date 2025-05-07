package com.attendance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.attendance.model.AttendanceRecord;

public class AttendanceDAO {

    private final String dbURL  = "jdbc:mysql://192.168.0.154:3306/demoDB";
    private final String dbUser = "remote_user";
    private final String dbPass = "password";

    /** DB接続を取得する共通メソッド */
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    /** ResultSet から AttendanceRecord を生成（共通化） */
    private AttendanceRecord mapResultSetToRecord(ResultSet rs, int employeeId) throws Exception {
        AttendanceRecord record = new AttendanceRecord();
        record.setId(rs.getInt("record_id"));
        record.setEmployeeId(employeeId);
        record.setDate(rs.getDate("work_date"));
        record.setClockIn(rs.getTime("clock_in"));
        record.setClockOut(rs.getTime("clock_out"));
        record.setBreakOut(rs.getTime("break_out"));
        record.setBreakIn(rs.getTime("break_in"));
        record.setBreakDuration(rs.getTime("break_duration"));
        return record;
    }

    /** 従業員IDと月(YYYY-MM)で複数レコードを取得 */
    public List<AttendanceRecord> getRecordsByEmployeeAndMonth(int employeeId, String month) throws Exception {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT record_id, work_date, clock_in, clock_out, break_out, break_in, break_duration "
                   + "FROM attendance_records "
                   + "WHERE employee_id = ? AND DATE_FORMAT(work_date, '%Y-%m') = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToRecord(rs, employeeId));
                }
            }
        }
        return records;
    }

    /** 従業員IDと日付(yyyy-MM-dd)で単一レコードを取得 */
    public AttendanceRecord getAttendanceRecordByEmployeeAndDate(int employeeId, String date) throws Exception {
        AttendanceRecord record = null;
        String sql = "SELECT record_id, work_date, clock_in, clock_out, break_out, break_in, break_duration "
                   + "FROM attendance_records "
                   + "WHERE employee_id = ? AND work_date = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    record = mapResultSetToRecord(rs, employeeId);
                }
            }
        }
        return record;
    }

    /** record_id で出勤/退勤と休憩時間を更新 */
    public void updateAttendance(int recordId, Timestamp clockIn, Timestamp clockOut, Time breakDuration) throws Exception {
        String sql = "UPDATE attendance_records "
                   + "SET clock_in = ?, clock_out = ?, break_duration = ?, updated_at = CURRENT_TIMESTAMP "
                   + "WHERE record_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, clockIn);
            ps.setTimestamp(2, clockOut);
            ps.setTime(3, breakDuration);
            ps.setInt(4, recordId);
            ps.executeUpdate();
        }
    }

    /** break_out を更新 (外出) */
    public void updateBreakOut(int recordId, Timestamp breakOutTime) throws Exception {
        String sql = "UPDATE attendance_records SET break_out = ?, updated_at = CURRENT_TIMESTAMP WHERE record_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, breakOutTime);
            ps.setInt(2, recordId);
            ps.executeUpdate();
        }
    }

    /** break_in と break_duration を更新 (再入) */
    public void updateBreakIn(int recordId, Timestamp breakInTime, Timestamp previousBreakOut) throws Exception {
        long diffMillis = breakInTime.getTime() - previousBreakOut.getTime();
        long seconds = diffMillis / 1000;
        LocalTime duration = LocalTime.ofSecondOfDay(seconds);
        Time breakDuration = Time.valueOf(duration);

        String sql = "UPDATE attendance_records "
                   + "SET break_in = ?, break_duration = ?, updated_at = CURRENT_TIMESTAMP "
                   + "WHERE record_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, breakInTime);
            ps.setTime(2, breakDuration);
            ps.setInt(3, recordId);
            ps.executeUpdate();
        }
    }

    /** 新規レコードを挿入 */
    public void insertAttendance(int employeeId, String workDate, Timestamp clockIn, Timestamp clockOut, Time breakDuration) throws Exception {
        String sql = "INSERT INTO attendance_records "
                   + "(employee_id, work_date, clock_in, clock_out, break_duration, created_at, updated_at) "
                   + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, workDate);
            ps.setTimestamp(3, clockIn);
            ps.setTimestamp(4, clockOut);
            ps.setTime(5, breakDuration);
            ps.executeUpdate();
        }
    }

    /** レコード削除 */
    public void deleteAttendance(int recordId) throws Exception {
        String sql = "DELETE FROM attendance_records WHERE record_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
        }
    }

    /**
     * 打刻記録を保存（出勤・退勤・外出・再入）
     * 既存機能はそのまま保持しつつ、外出・再入で自動計算を追加
     */
    public void insertPunch(int employeeId, String action, LocalDateTime now) throws Exception {
        String workDate = now.toLocalDate().toString();
        Timestamp timestamp = Timestamp.valueOf(now);

        String selectSql =
            "SELECT record_id, clock_in, clock_out, break_out, break_in, break_duration "
          + "FROM attendance_records WHERE employee_id = ? AND work_date = ? FOR UPDATE";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setInt(1, employeeId);
            ps.setString(2, workDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int recordId = rs.getInt("record_id");
                    Timestamp ci = rs.getTimestamp("clock_in");
                    Timestamp co = rs.getTimestamp("clock_out");
                    Timestamp bo = rs.getTimestamp("break_out");
                    Timestamp bi = rs.getTimestamp("break_in");
                    Time bd     = rs.getTime("break_duration");

                    switch (action) {
                        case "出勤":
                            if (ci == null) {
                                updateAttendance(recordId, timestamp, co, bd);
                            }
                            break;
                        case "退勤":
                            if (co == null) {
                                updateAttendance(recordId, ci, timestamp, bd);
                            }
                            break;
                        case "外出":
                            if (bo == null) {
                                updateBreakOut(recordId, timestamp);
                            }
                            break;
                        case "再入":
                            if (bo != null && bi == null) {
                                updateBreakIn(recordId, timestamp, bo);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    // 初回出勤・退勤
                    Timestamp in  = "出勤".equals(action) ? timestamp : null;
                    Timestamp out = "退勤".equals(action) ? timestamp : null;
                    insertAttendance(employeeId, workDate, in, out, null);
                }
            }
        }
    }
}
