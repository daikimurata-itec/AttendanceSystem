package com.attendance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editAttendanceForm")
public class EditAttendanceForm extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String dbURL = "jdbc:mysql://192.168.0.154:3306/demoDB?serverTimezone=UTC";
    private String dbUser = "remote_user";
    private String dbPass = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        Attendance data = new Attendance();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
                String sql = "SELECT employee_id, work_date, clock_in, clock_out, break_duration FROM attendance_records WHERE record_id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, recordId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            data.recordId = recordId;
                            data.employeeId = rs.getInt("employee_id");
                            data.workDate = rs.getDate("work_date");
                            data.clockIn = rs.getTimestamp("clock_in");
                            data.clockOut = rs.getTimestamp("clock_out");
                            data.breakDuration = rs.getTime("break_duration");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.setAttribute("att", data);
        request.getRequestDispatcher("/WEB-INF/view/editAttendanceForm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        String workDate = request.getParameter("workDate"); // yyyy-MM-dd

        Timestamp clockIn = Timestamp.valueOf(workDate + " " + request.getParameter("clockIn") + ":00");
        Timestamp clockOut = Timestamp.valueOf(workDate + " " + request.getParameter("clockOut") + ":00");

        // 休憩時間 (HH:mm) を受け取り java.sql.Time に変換
        Time breakDuration = Time.valueOf(request.getParameter("breakDuration") + ":00");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
                String sql = "UPDATE attendance_records SET clock_in=?, clock_out=?, break_duration=? WHERE record_id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setTimestamp(1, clockIn);
                    ps.setTimestamp(2, clockOut);
                    ps.setTime(3, breakDuration);
                    ps.setInt(4, recordId);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        request.setAttribute("message", "編集を保存しました");
        request.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(request, response);
    }

    public static class Attendance {
        private int recordId;
        private int employeeId;
        public java.sql.Date workDate;
        public java.sql.Timestamp clockIn;
        public java.sql.Timestamp clockOut;
        public java.sql.Time breakDuration;
        
        public int getRecordId() {
            return recordId;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public java.sql.Date getWorkDate() {
            return workDate;
        }

        public java.sql.Timestamp getClockIn() {
            return clockIn;
        }

        public java.sql.Timestamp getClockOut() {
            return clockOut;
        }

        public java.sql.Time getBreakDuration() {
            return breakDuration;
        }

        // Setter
        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        public void setWorkDate(java.sql.Date workDate) {
            this.workDate = workDate;
        }

        public void setClockIn(java.sql.Timestamp clockIn) {
            this.clockIn = clockIn;
        }

        public void setClockOut(java.sql.Timestamp clockOut) {
            this.clockOut = clockOut;
        }

        public void setBreakDuration(java.sql.Time breakDuration) {
            this.breakDuration = breakDuration;
        }
    }
}
