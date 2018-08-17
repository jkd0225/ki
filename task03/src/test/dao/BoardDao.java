package test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import test.db.DBConnection;
import test.vo.BoardVo;

public class BoardDao {
	public int getMaxNum() {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getConn();
			String sql="select NVL(max(num),0) maxnum from guestboard";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("maxnum");
			}else {
				return 0;
			}
		}catch(SQLException se) {
			System.out.println(se.getMessage());
			return -1;
		}finally {
			DBConnection.closeConn(rs, pstmt, con);
		}
	}
	public int getCount() {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getConn();
			String sql="select NVL(count(num),0) cnt from guestboard";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("cnt");
			}else {
				return 0;
			}
		}catch(SQLException se) {
			System.out.println(se.getMessage());
			return -1;
		}finally {
			DBConnection.closeConn(rs, pstmt, con);
		}
	}
	public int insert(BoardVo vo) {
		Connection con=null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmt1=null; // pstmt1은 답글의 update용
		try {
			con=DBConnection.getConn();
			int boardNum=getMaxNum()+1; // 글번호는 0부터 시작해서 1더함
			int num=vo.getNum();
			int ref=vo.getRef();
			int lev=vo.getLev();
			int step=vo.getStep();
			if(num==0) { // 신규 글은 0부터 시작
				ref=boardNum; // 신규 글의 ref는 글번호와 같아야함
			}else {
				String sql="update guestboard set step=step+1 where ref=? and step>?";
				pstmt1=con.prepareStatement(sql);
				pstmt1.setInt(1, ref);
				pstmt1.setInt(2, step);
				lev+=1; // 최근 답변이 먼저 보여야 하므로 나머지 step 1씩 증가
				step+=1; // 신규 글은 0이기 때문에 1씩 추가한다
			}
			String sql="insert into guestboard values(?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, vo.getWriter());
			pstmt.setString(3, vo.getTitle());
			pstmt.setString(4, vo.getContent());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, lev);
			pstmt.setInt(7, step);
			int n=pstmt.executeUpdate();
			return n;
		}catch(SQLException se) {
			System.out.println(se.getMessage());
			return -1;
		}finally {
			DBConnection.closeConn(pstmt1);
			DBConnection.closeConn(pstmt);
			DBConnection.closeConn(con);
		}
	}
	public ArrayList<BoardVo> list(int startRow,int endRow){
		String sql="SELECT * FROM " + 
				"( " + 
				"	SELECT AA.*,ROWNUM RNUM FROM " + 
				"	( " + 
				"		SELECT * FROM GUESTBOARD " + 
				"		ORDER BY REF DESC,STEP ASC " + 
				"	)AA " + 
				") " + 
				"WHERE RNUM>=? AND RNUM<=?";
		// 시작 글부터 마지막 글까지 목록에 보이기 
		// ROWNUM을 바로 얻어올 수 없어서 서브쿼리로 작성 
		// ref 내림차순으로 정렬 하고 step 오름차순으로 정렬
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getConn();
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs=pstmt.executeQuery();
			ArrayList<BoardVo> list=new ArrayList<>();
			while(rs.next()) {
				int num=rs.getInt("num");
				String writer=rs.getString("writer");
				String title=rs.getString("title");
				String content=rs.getString("content");
				int ref=rs.getInt("ref");
				int lev=rs.getInt("lev");
				int step=rs.getInt("step");
				BoardVo vo=new BoardVo(num, writer, title, content, ref, lev, step);
				list.add(vo);
			}
			return list;
		}catch(SQLException se) {
			System.out.println(se.getMessage());
			return null;
		}finally {
			DBConnection.closeConn(rs, pstmt, con);
		}
	}
	public BoardVo detail(int num) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=DBConnection.getConn();
			String sql="select * from guestboard where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				String writer=rs.getString("writer");
				String title=rs.getString("title");
				String content=rs.getString("content");
				int ref=rs.getInt("ref");
				int lev=rs.getInt("lev");
				int step=rs.getInt("step");
				BoardVo vo=new BoardVo(num, writer, title, content, ref, lev, step);
				return vo;
			}
		}catch(SQLException se) {
			System.out.println(se.getMessage());
		}finally {
			DBConnection.closeConn(rs, pstmt, con);
		}
		return null;
	}
}














