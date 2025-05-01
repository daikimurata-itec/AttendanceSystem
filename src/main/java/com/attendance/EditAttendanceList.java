package com.attendance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editAttendanceList")
public class EditAttendanceList extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String dbURL = "jdbc:mysql://192.168.0.154:3306/demoDB?serverTimezone=UTC";
    private String dbUser = "remote_user";
    private String dbPass = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        String month = request.getParameter("month"); // format YYYY-MM
        List<Record> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
                String sql = "SELECT record_id, work_date, clock_in, clock_out, break_duration FROM attendance_records "
                        + "WHERE employee_id=? AND DATE_FORMAT(work_date, '%Y-%m')=? ORDER BY work_date";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(employeeId));
                    ps.setString(2, month);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Record r = new Record();
                            r.id = rs.getInt("record_id");
                            r.date = rs.getDate("work_date");
                            r.clockIn = rs.getTime("clock_in");
                            r.clockOut = rs.getTime("clock_out");
                            r.breakDuration = rs.getTime("break_duration");
                            list.add(r);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.setAttribute("records", list);
        request.setAttribute("employeeId", employeeId);
        request.getRequestDispatcher("/WEB-INF/view/editAttendanceList.jsp").forward(request, response);
    }

    public static class Record {
        private int id;
        private Date date;
        private java.sql.Time clockIn;
        private java.sql.Time clockOut;
        private java.sql.Time breakDuration;

        // getters and setters
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public java.sql.Time getClockIn() {
            return clockIn;
        }
        public void setClockIn(java.sql.Time clockIn) {
            this.clockIn = clockIn;
        }
        public java.sql.Time getClockOut() {
            return clockOut;
        }
        public void setClockOut(java.sql.Time clockOut) {
            this.clockOut = clockOut;
        }
        public java.sql.Time getBreakDuration() {
            return breakDuration;
        }
        public void setBreakDuration(java.sql.Time breakDuration) {
            this.breakDuration = breakDuration;
        }
    }
}
