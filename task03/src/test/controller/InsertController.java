package test.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.dao.BoardDao;
import test.vo.BoardVo;
@WebServlet("/insert.do")
public class InsertController extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8"); //입력할때 한글이면 입력한다
		String snum=req.getParameter("num"); // 새로 등록하는 글인지 확인
		String writer=req.getParameter("writer");
		String title=req.getParameter("title");
		String content=req.getParameter("content");
		int num=0;
		int ref=0;
		int lev=0;
		int step=0; 
		// 새 글은 0으로 시작
		if(snum!=null && !snum.equals("")) {// 새글은 snum이 빈값으로 얻어온다
			num=Integer.parseInt(snum);
			ref=Integer.parseInt(req.getParameter("ref"));
			lev=Integer.parseInt(req.getParameter("lev"));
			step=Integer.parseInt(req.getParameter("step"));
		} // 답글에 값 설정하기
		BoardVo vo=new BoardVo(num, writer, title, content, ref, lev, step);
		BoardDao dao=new BoardDao();
		int n=dao.insert(vo);
		if(n>0) {
			req.setAttribute("code", "success");
		}else {
			req.setAttribute("code", "fail");
		}
		req.getRequestDispatcher("result.jsp").forward(req, resp);
	}
}







