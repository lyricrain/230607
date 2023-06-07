package org.kosta.myproject.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;
/*
 *  MemberDAO 에서  DBCP 이용하기 
 * 
 *  Singleton Design Pattern 적용해서 
 *  시스템 상에서 MemberDAO 객체를 단 한번 생성해서 사용하도록 한다 
 *  1. private 생성자 
 *  2. private static 변수로 객체 생성해서 초기화 ( instance 변수명 ) 
 *  3. public static 메서드로 외부에 공유  ( getInstance ()  ) 
 */
public class MemberDAO {
	private static MemberDAO instance=new MemberDAO();
	private DataSource dataSource;
	private MemberDAO() {
		dataSource=DataSourceManager.getInstance().getDataSource();
	}
	public static MemberDAO getInstance() {
		return instance;
	}
	public void closeAll(PreparedStatement pstmt,Connection con) throws SQLException {
		if(pstmt!=null)
			pstmt.close();
		if(con!=null)
			con.close(); // 컨넥션을 DBCP(DataSource)로 반납한다 
	}
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection con) throws SQLException {
		if(rs!=null)
			rs.close();
		if(pstmt!=null)
			pstmt.close();
		if(con!=null)
			con.close();// 컨넥션을 DBCP(DataSource)로 반납한다 
	}
	public Connection getConnection() throws SQLException {
		//return DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "scott", "tiger");
		 return dataSource.getConnection();// 컨넥션을 DBCP(DataSource)로부터 빌려온다 
	}
	public int findMemberCount() throws SQLException {
		int count=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			String sql="select count(*) from member";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			count=rs.getInt(1);
		}finally {
			closeAll(rs, pstmt, con);
		}			
		return count;
	}
	public MemberVO findMemberById(String id) throws SQLException {
		MemberVO memberVO=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			String sql="select name,address from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
	    	memberVO=new MemberVO(id,null,rs.getString(1),rs.getString(2));
			}
		}finally {
			closeAll(rs, pstmt, con);
		}
		return memberVO;
	}
	public ArrayList<MemberVO> findMemberListByAddress(String address) throws SQLException {
		ArrayList<MemberVO> list=new ArrayList<>();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			String sql="select id,name from member where address=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, address);
			rs=pstmt.executeQuery();
			while(rs.next()) {
			 list.add(new MemberVO(rs.getString(1),null,rs.getString(2),address));
			}
		}finally {
			closeAll(rs, pstmt, con);
		}
		
		return list;
	}
	/*
	  
	 */
	public void registerMember(MemberVO vo) throws SQLException {
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=getConnection();
			String sql="insert into member(id,password,name,address) values(?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getAddress());
			pstmt.executeUpdate();
		}finally {
			closeAll(pstmt, con);
		}
	}
	public MemberVO login(String id, String password) throws SQLException{
		MemberVO memberVO=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			String sql="select name,address from member where id=? and password=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				memberVO=new MemberVO(id, password, rs.getString(1), rs.getString(2));
			}
		}finally {
			closeAll(rs, pstmt, con);
		}
		return memberVO;
	}
	public int updateMember(MemberVO memberVO) throws SQLException{
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=getConnection();
			String sql="update member set password=?,name=?,address=? where id=?";
			pstmt=con.prepareStatement(sql);			
			pstmt.setString(1, memberVO.getPassword());
			pstmt.setString(2, memberVO.getName());
			pstmt.setString(3, memberVO.getAddress());
			pstmt.setString(4, memberVO.getId());
			return pstmt.executeUpdate();
		}finally {
			closeAll(pstmt, con);
		}
	}
	public boolean checkId(String id) throws SQLException {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		boolean result=false;
		try {
			con=getConnection();
			String sql="select count(*) from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()&&rs.getInt(1)>0) {
				result=true;
			}
		}finally {
			closeAll(rs, pstmt, con);
		}
		return result;
	}
}


















