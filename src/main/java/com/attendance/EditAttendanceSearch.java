package com.attendance;

import java.io.IOException;
import java.text.Normalizer;

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
        String idParamRaw = request.getParameter("employeeId");
        String monthParam = request.getParameter("month");
        String action = request.getParameter("action");  // "search" or "edit"
        
     // IDを半角に変換（全角数字→半角数字）
        String idParam = null;
        if (idParamRaw != null) {
            idParam = Normalizer.normalize(idParamRaw, Normalizer.Form.NFKC);
        }

        // 編集ボタン → 一覧画面へ
        if ("edit".equals(action)) {
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

        // 検索ボタンが押された場合
        if ("search".equals(action)) {
            request.setAttribute("searched", true);  // 検索実行フラグを渡す

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
