package com.attendance;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.EmployeeDAO;
import com.attendance.model.AttendanceRecord;

@WebServlet("/editAttendanceForm")
public class EditAttendanceForm extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private EmployeeDAO  employeeDAO   = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String dateStr = request.getParameter("date");
        String month   = request.getParameter("month");

        try {
            AttendanceRecord r = attendanceDAO.getAttendanceRecordByEmployeeAndDate(employeeId, dateStr);
            Attendance att = new Attendance();
            if (r != null) {
                att.setRecordId(r.getId());
                att.setEmployeeId(r.getEmployeeId());
                att.setWorkDate(new Date(r.getDate().getTime()));
                if (r.getClockIn() != null)  att.setClockIn(new Timestamp(r.getClockIn().getTime()));
                if (r.getClockOut()!= null)  att.setClockOut(new Timestamp(r.getClockOut().getTime()));
                att.setBreakDuration(r.getBreakDuration());
            } else {
                att.setRecordId(0);
                att.setEmployeeId(employeeId);
                att.setWorkDate(Date.valueOf(dateStr));
            }

            request.setAttribute("att", att);
            request.setAttribute("month", month);
            request.getRequestDispatcher("/WEB-INF/view/editAttendanceForm.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("打刻データ取得失敗", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        int recordId   = Integer.parseInt(request.getParameter("recordId"));
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String workDate = request.getParameter("workDate"); // yyyy-MM-dd
        String month    = request.getParameter("month");

        try {
            if ("delete".equals(action)) {
                // 削除処理
                if (recordId != 0) {
                    attendanceDAO.deleteAttendance(recordId);
                }
                request.setAttribute("message", "打刻データを削除しました");
            } else {
                // 更新処理
                Timestamp clockIn = Timestamp.valueOf(workDate + " " + request.getParameter("clockIn") + ":00");
                Timestamp clockOut= Timestamp.valueOf(workDate + " " + request.getParameter("clockOut")+ ":00");
                Time breakDuration = request.getParameter("breakDuration").isEmpty()
                    ? null
                    : Time.valueOf(request.getParameter("breakDuration") + ":00");

                if (recordId == 0) {
                    attendanceDAO.insertAttendance(employeeId, workDate, clockIn, clockOut, breakDuration);
                } else {
                    attendanceDAO.updateAttendance(recordId, clockIn, clockOut, breakDuration);
                }

                Attendance updatedAtt = new Attendance();
                updatedAtt.setRecordId(recordId);
                updatedAtt.setEmployeeId(employeeId);
                updatedAtt.setWorkDate(Date.valueOf(workDate));
                updatedAtt.setClockIn(clockIn);
                updatedAtt.setClockOut(clockOut);
                updatedAtt.setBreakDuration(breakDuration);

                request.setAttribute("att", updatedAtt);
                request.setAttribute("message", "編集を保存しました");
            }

            String employeeName = employeeDAO.findEmployeeNameById(employeeId);
            request.setAttribute("employeeName", employeeName);
            request.getRequestDispatcher("/WEB-INF/view/result.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("打刻データの更新・削除に失敗しました", e);
        }
    }

    public static class Attendance {
        private int recordId;
        private int employeeId;
        private Date workDate;
        private Timestamp clockIn;
        private Timestamp clockOut;
        private Time breakDuration;

        public int getRecordId()       { return recordId; }
        public int getEmployeeId()     { return employeeId; }
        public Date getWorkDate()      { return workDate; }
        public Timestamp getClockIn()  { return clockIn; }
        public Timestamp getClockOut() { return clockOut; }
        public Time getBreakDuration(){ return breakDuration; }

        public void setRecordId(int recordId)             { this.recordId = recordId; }
        public void setEmployeeId(int employeeId)         { this.employeeId = employeeId; }
        public void setWorkDate(Date workDate)            { this.workDate = workDate; }
        public void setClockIn(Timestamp clockIn)         { this.clockIn = clockIn; }
        public void setClockOut(Timestamp clockOut)       { this.clockOut = clockOut; }
        public void setBreakDuration(Time breakDuration)  { this.breakDuration = breakDuration; }
    }
}
