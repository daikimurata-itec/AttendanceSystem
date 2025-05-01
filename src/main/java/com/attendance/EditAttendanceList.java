package com.attendance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.EmployeeDAO;
import com.attendance.model.AttendanceRecord;

@WebServlet("/editAttendanceList")
public class EditAttendanceList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init() {
        attendanceDAO = new AttendanceDAO();
        employeeDAO   = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // パラメータ or セッションから employeeId, month を取得
        String empParam   = request.getParameter("employeeId");
        String monthParam = request.getParameter("month");

        if (empParam != null && !empParam.isEmpty()) {
            session.setAttribute("employeeId", empParam);
        } else {
            empParam = (String) session.getAttribute("employeeId");
        }

        if (monthParam != null && !monthParam.isEmpty()) {
            session.setAttribute("month", monthParam);
        } else {
            monthParam = (String) session.getAttribute("month");
        }

        if (empParam == null || monthParam == null) {
            throw new ServletException("employeeId または month が指定されていません");
        }

        try {
            int empId   = Integer.parseInt(empParam);
            String month = monthParam; // "YYYY-MM"

            // 1) 当月のレコード取得
            List<AttendanceRecord> recs = attendanceDAO.getRecordsByEmployeeAndMonth(empId, month);

            // 2) Map<"yyyy-MM-dd", record> を作成
            Map<String, AttendanceRecord> recordMap = new HashMap<>();
            for (AttendanceRecord r : recs) {
                recordMap.put(DATE_FMT.format(r.getDate()), r);
            }

            // 3) 当月の日付リストを作成
            String[] parts = month.split("-");
            int year     = Integer.parseInt(parts[0]);
            int monthNum = Integer.parseInt(parts[1]);
            List<Date> allDays = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthNum - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int d = 1; d <= maxDay; d++) {
                cal.set(Calendar.DAY_OF_MONTH, d);
                allDays.add(cal.getTime());
            }

            // 4) 社員名を取得
            String employeeName = employeeDAO.findEmployeeNameById(empId);

            // 5) リクエスト属性にセット
            request.setAttribute("employeeId", empId);
            request.setAttribute("employeeName", employeeName);
            request.setAttribute("month", month);
            request.setAttribute("allDaysInMonth", allDays);
            request.setAttribute("recordMap", recordMap);

            // 6) JSPへフォワード
            request.getRequestDispatcher("/WEB-INF/view/editAttendanceList.jsp")
                   .forward(request, response);

        } catch (NumberFormatException ne) {
            throw new ServletException("employeeIdの形式が不正です", ne);
        } catch (Exception e) {
            throw new ServletException("出勤データの取得に失敗しました", e);
        }
    }
}
