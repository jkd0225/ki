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
		PreparedStatement pstmt1=null; // pstmt1�� ����� update��
		try {
			con=DBConnection.getConn();
			int boardNum=getMaxNum()+1; // �۹�ȣ�� 0���� �����ؼ� 1����
			int num=vo.getNum();
			int ref=vo.getRef();
			int lev=vo.getLev();
			int step=vo.getStep();
			if(num==0) { // �ű� ���� 0���� ����
				ref=boardNum; // �ű� ���� ref�� �۹�ȣ�� ���ƾ���
			}else {
				String sql="update guestboard set step=step+1 where ref=? and step>?";
				pstmt1=con.prepareStatement(sql);
				pstmt1.setInt(1, ref);
				pstmt1.setInt(2, step);
				lev+=1; // �ֱ� �亯�� ���� ������ �ϹǷ� ������ step 1�� ����
				step+=1; // �ű� ���� 0�̱� ������ 1�� �߰��Ѵ�
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
		// ���� �ۺ��� ������ �۱��� ��Ͽ� ���̱� 
		// ROWNUM�� �ٷ� ���� �� ��� ���������� �ۼ� 
		// ref ������������ ���� �ϰ� step ������������ ����
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














