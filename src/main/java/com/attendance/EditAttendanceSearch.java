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
        // メニューから検索画面に戻ってきたときなど、常に古い検索条件をクリアする
        HttpSession session = request.getSession();
        session.removeAttribute("employeeId");
        session.removeAttribute("month");

        // 元のパラメータを取得（POST→GET時の転送用）
        String id     = request.getParameter("employeeId");
        String month  = request.getParameter("month");
        String action = request.getParameter("action");  // "search" or "edit"

        // 編集ボタン押下で一覧に飛ぶ場合は、セッションに保存してリダイレクト
        if ("edit".equals(action)) {
            session.setAttribute("employeeId", id);
            session.setAttribute("month", month);
            response.sendRedirect(request.getContextPath()
                    + "/editAttendanceList?employeeId=" + id
                    + "&month=" + month);
            return;
        }

        // 検索ボタン押下 or IDが直打ちされた場合
        if ("search".equals(action) || (id != null && !id.isEmpty())) {
            Employee emp = employeeDAO.findById(id);
            if (emp != null) {
                request.setAttribute("employeeName", emp.getName());
            } else {
                request.setAttribute("errorMessage", "該当する社員が見つかりません(ID=" + id + ")");
            }
        }

        // 入力内容をフォーム再表示用にセット
        request.setAttribute("employeeId", id);
        request.setAttribute("month", month);
        request.getRequestDispatcher("/WEB-INF/view/editAttendanceSearch.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // POSTはGETに委譲
        doGet(req, resp);
    }
}
