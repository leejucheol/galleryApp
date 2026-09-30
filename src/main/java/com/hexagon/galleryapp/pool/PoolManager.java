package com.hexagon.galleryapp.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
   커넥션풀을 얻어오기 위해서는 
   JNDI 검색을 선행해야 하는데, 만이 이 코드를 모든 java class와 jsp에 일일이 작성하게 되면 코드 중복, 즉 하드코딩에 의한
   유지보수성에 문제가 발생한다..
   따라서 외부의 변화(jndi의 이름이 바뀌는 등..)에 민감하지 않은 방식으로 개발을 해보자..
      
[ PoolManger 에서 구현할 내용 ]
	1) 검색 및 커넥션풀 얻기 
	2) Connection 필요한자 에게 제공하는 기능  
	3) 다 쓴 Connection 을 다시 풀에 돌려보내는 기능

	풀매니저 클래스를 singleton
*/ 

public class PoolManager {
	InitialContext ctx; //JNDI 검색 객체
	DataSource ds; //커넥션풀 구현체 
	
	private static PoolManager instance; // 모든 인스턴스가 공유할 수 있는 변수를 선언..인스턴스간 공유가 되어야, 이미 생성되었는지 여부를 따져볼 수 있기 때문
											// 즉 공유된 static 변수값이 채워져 있으면, 인스턴스의 생성을 하지 않게 하기 위해 
	//throws 란 해당 예외를 여기서 잡지 않겠다!!! 이 메서드를 호출한 者에게 부담시키는 예외 던지기(전달) 기법
	public PoolManager(){
		try {
			ctx = new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/mysql");//여기서 커넥션풀을 얻는 시점!!!
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public static PoolManager getInstance() {
		if(instance == null) {
			instance = new PoolManager();
		}
		return instance;
	}
	
	//Connection을 필요로 하는 자에게 이 메서드를 호출시 Connection 을 가져갈수 있도록 반환하자!! 
	public Connection getConnection() {
		Connection con=null; //메서드의 지역변수는 반드시 초기화하고 사용할수 있으므로, null로 초기화 하자 
		try {
			con=ds.getConnection(); //풀로부터 커넥션 1개 취득!!
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//사용했던 Connectio 을 반납시키자!! ( 다시 풀로 돌려보내기)
	//이 메서드를 호출하는 者는 자신이 사용한 Connection을 이 메서드 호출시 매개변수로 전달하면 된다..
	public void release(Connection con) {
		try {
			if(con !=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//이 메서드도 Connection 반납 메서드이지만, 특히 DML(insert,update, delete)을 수행한 자가 자원해제시 사용함 
	public void release(Connection con, PreparedStatement pstmt) {
		try {
			if(pstmt !=null)pstmt.close();
			if(con !=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//이 메서드도 Connection 반납 메서드이지만, 특히 select 문 수행한자 ResultSet도 해제시키기 위해 사용할 예정 
	public void release(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs !=null)rs.close();
			if(pstmt !=null)pstmt.close();
			if(con !=null)con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}