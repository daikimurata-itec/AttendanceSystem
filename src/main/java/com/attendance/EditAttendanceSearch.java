package com.attendance;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.attendance.dao.EmployeeDAO;
import com.attendance.model.Employee;

@WebServlet("/editAttendanceSearch")
public class EditAttendanceSearch extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 古い検索条件をクリア
        HttpSession session = request.getSession();
        session.removeAttribute("employeeId");
        session.removeAttribute("month");

        // パラメータ取得
        String idParam    = request.getParameter("employeeId");
        String monthParam = request.getParameter("month");
        String action     = request.getParameter("action");  // "search" or "edit"

        // 編集ボタン → 一覧画面へ
        if ("edit".equals(action)) {
            // employeeId と month が null/空なら一覧へ戻す
            if (idParam == null || idParam.isEmpty() ||
                monthParam == null || monthParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/editAttendanceList");
                return;
            }
            session.setAttribute("employeeId", idParam);
            session.setAttribute("month", monthParam);
            response.sendRedirect(request.getContextPath()
                    + "/editAttendanceList?employeeId=" + idParam
                    + "&month=" + monthParam);
            return;
        }

        // 検索ボタン押下または直接 ID 指定された場合
        if ("search".equals(action) || (idParam != null && !idParam.isEmpty())) {
            // ID が数値かチェック
            if (idParam != null && idParam.matches("\\d+")) {
                Employee emp = null;
                try {
                    emp = employeeDAO.findById(idParam);
                } catch (Exception e) {
                    throw new ServletException("スタッフ情報の取得に失敗しました", e);
                }
                if (emp != null) {
                    request.setAttribute("employeeName", emp.getName());
                } else {
                    request.setAttribute("errorMessage", "該当するスタッフが見つかりませんでした。");
                }
            } else {
                request.setAttribute("errorMessage", "スタッフIDは数値で指定してください。");
            }
        }

        // フォーム再表示用に入力値セット
        request.setAttribute("employeeId", idParam);
        request.setAttribute("month", monthParam);
        request.getRequestDispatcher("/WEB-INF/view/editAttendanceSearch.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
