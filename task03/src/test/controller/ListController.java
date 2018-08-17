package test.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.dao.BoardDao;
import test.vo.BoardVo;
@WebServlet("/list.do")
public class ListController extends HttpServlet{
	@Override
	public void service(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		String spageNum=req.getParameter("pageNum");
		int pageNum=1;
		if(spageNum!=null) {
			pageNum=Integer.parseInt(spageNum);
		}
		int startRow=(pageNum-1)*10+1;
		int endRow=startRow+9; // ���������� 1-10���� �� ���̱�
		
		BoardDao dao=new BoardDao();
		ArrayList<BoardVo> list=dao.list(startRow, endRow);
		int pageCount=(int)Math.ceil(dao.getCount()/10.0);
		//�Ǽ��� ������ �Ҽ������� ������
		int startPage=((pageNum-1)/10*10)+1;
		int endPage=startPage+9;
		if(endPage>pageCount) {
			endPage=pageCount;
		}
		req.setAttribute("list", list);
		req.setAttribute("pageCount", pageCount);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage", endPage);
		req.setAttribute("pageNum", pageNum);
		req.getRequestDispatcher("/list.jsp").forward(req, resp);
	}
}














