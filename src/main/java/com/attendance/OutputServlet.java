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

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.EmployeeDAO;
import com.attendance.model.AttendanceRecord;

@WebServlet("/output")
public class OutputServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("employeeId");
        String month = req.getParameter("month");
        String action = req.getParameter("action"); // search, confirm, output

        // 入力値をフォームに残すためにセット
        req.setAttribute("employeeId", id);
        req.setAttribute("month", month);

        if ("search".equals(action)) {
            req.setAttribute("searched", true); // ← これがJSP側の条件に使える

            try {
                String name = employeeDAO.findEmployeeNameById(Integer.parseInt(id));
                if (name == null) {
                    req.setAttribute("errorMessage", "該当するスタッフが見つかりませんでした。");
                } else {
                    req.setAttribute("employeeName", name);
                }
            } catch (Exception e) {
                throw new ServletException("社員検索失敗", e);
            }

            req.getRequestDispatcher("/WEB-INF/view/outputSearch.jsp").forward(req, resp);
            return;
        }

        if ("confirm".equals(action)) {
            String name = (String) req.getAttribute("employeeName");
            if (name == null && id != null) {
                try {
                    name = employeeDAO.findEmployeeNameById(Integer.parseInt(id));
                } catch (Exception e) {
                    throw new ServletException("社員検索失敗", e);
                }
            }

            req.setAttribute("employeeName", name);
            String format = req.getParameter("format");
            req.setAttribute("format", format);

            List<AttendanceRecord> list;
            try {
                list = attendanceDAO.getRecordsByEmployeeAndMonth(Integer.parseInt(id), month);
            } catch (Exception e) {
                throw new ServletException("出力一覧取得失敗", e);
            }

            Map<String, AttendanceRecord> recordMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (AttendanceRecord r : list) {
                recordMap.put(sdf.format(r.getDate()), r);
            }
            req.setAttribute("recordMap", recordMap);

            String[] parts = month.split("-");
            int year = Integer.parseInt(parts[0]);
            int monthNum = Integer.parseInt(parts[1]) - 1;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthNum);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            List<Date> allDays = new ArrayList<>();
            for (int d = 1; d <= maxDay; d++) {
                cal.set(Calendar.DAY_OF_MONTH, d);
                allDays.add(cal.getTime());
            }
            req.setAttribute("allDaysInMonth", allDays);

            req.getRequestDispatcher("/WEB-INF/view/outputConfirm.jsp").forward(req, resp);
            return;
        }

        // 初回やパラメータ不足時
        req.getRequestDispatcher("/WEB-INF/view/outputSearch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action"); // print or back
        String id = req.getParameter("employeeId");
        String month = req.getParameter("month");
        String format = req.getParameter("format");

        if ("back".equals(action)) {
            resp.sendRedirect(req.getContextPath()
                    + "/output?employeeId=" + id
                    + "&month=" + month
                    + "&action=search");
            return;
        }

        if ("print".equals(action)) {
            try {
                List<AttendanceRecord> list =
                        attendanceDAO.getRecordsByEmployeeAndMonth(Integer.parseInt(id), month);

                if ("CSV".equals(format)) {
                    resp.setContentType("text/csv; charset=UTF-8");
                    resp.setHeader("Content-Disposition",
                            "attachment; filename=\"attendance_" + id + "_" + month + ".csv\"");
                    resp.getWriter().println("日付,出勤,退勤,休憩");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (AttendanceRecord r : list) {
                        String row =
                                sdf.format(r.getDate()) + ","
                                        + (r.getClockIn() != null ? r.getClockIn() : "") + ","
                                        + (r.getClockOut() != null ? r.getClockOut() : "") + ","
                                        + (r.getBreakDuration() != null ? r.getBreakDuration() : "");
                        resp.getWriter().println(row);
                    }
                } else {
                    resp.setContentType("application/pdf");
                    resp.setHeader("Content-Disposition",
                            "attachment; filename=\"attendance_" + id + "_" + month + ".pdf\"");
                    resp.getWriter().write("PDF生成ロジックをここに実装してください");
                }
            } catch (Exception e) {
                throw new ServletException("出力処理失敗", e);
            }
        }
    }
}
