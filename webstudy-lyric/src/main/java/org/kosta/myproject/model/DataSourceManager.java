package org.kosta.myproject.model;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/*
 	DataSourceManager :  DBCP 구현체(apache-tomcat dbcp)를 표준화된 javax.sql.DataSource Interface 타입으로 생성해서 관리하는 클래스 
 	
 	DBCP : DataBase Connection Pool 을 정의하는 객체 
 			db 컨넥션을 생성,소멸하는 방식이 아니라 
 			Pool 에 미리 생성해놓고 
 			빌려주고 반납받는 동작원리로 
 			시스템 성능 향상을 위해 사용하는 객체 
 
 
 */
public class DataSourceManager {
	private static DataSourceManager instance=new DataSourceManager();
	// 다양한 dbcp 구현체들의 상위 인터페이스:어플리케이션과 구체적인 dbcp구현체와의 결합도를 느슨하게 하기 위해 
	private DataSource dataSource;
	private DataSourceManager() {
		//apache tomcat dbcp 구현체를 이용한다 
		BasicDataSource dbcp=new BasicDataSource();
		dbcp.setDriverClassName("oracle.jdbc.OracleDriver");
		dbcp.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");
		dbcp.setUsername("scott");
		dbcp.setPassword("tiger");	
		dbcp.setMaxTotal(30);//자신의 환경에 맞게 컨넥션 수를 조정할 수 있다 
		//인스턴스 변수 DataSource 타입으로 지역변수 dbcp 객체를 참조시킨다 
		this.dataSource=dbcp;  // Player p=new Youtube(); -> Youtube implements Player 
	}
	public static DataSourceManager getInstance() {
		return instance;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
}
























