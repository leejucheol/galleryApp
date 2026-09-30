package com.hexagon.galleryapp.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/*
 * mybatis 설정 파일을(mybatis-config.xml) 읽기 위한 java class
 * */
public class MybatisConfig {

//mybatis 의 공식 사이트에서도 SqlSessionFactory(SqlSession 을 모아놓은..)는 메모리에 오직 1개만 올려서 쓰기를 권장하고 있으므로,
// 현재 우리 MybatisConfig 클래스의 인스턴스를 오직 한개만 만들 수 있도록 SingleTon 패턴으로 정의해서 SqlSessionFactory 가 우리 애플리케이션 내에서
// 오직 하나의 인스턴스만 생성되도록 안전장치를 마련하자!!

// SingleTon 에서 변수를 static 으로 가는 이유는, 모든 인스턴스가 static 영역을 쳐다볼 수 있기 때문에, 이 변수에 레퍼런스 주소값이 채워져있는지 여부를 
// 알 수 있으므로..
	private static MybatisConfig instance;
	/*
	 * mybatis는 개발자가 직접 jdbc api를 다루지 않고도 쿼리문에 집중할 수 있도록하는 Database분야 (Persistence
	 * 영역) 프레임워크이다 따라서 앞으로 개발자가 java 코드에서 쿼리를 수행하려면 직접 PreparedStatement를 쓰지 않고,
	 * Mybatis가 제공해주는 SqlSession이라는 객체를 이용한다 그리고 Sqlsession을 얻기 위해서는 SqlSession이
	 * 모여있는 SqlSessionFactory로부터 가져오면 된다
	 */
	private SqlSessionFactory sqlSessionFactory;

	public MybatisConfig() {
		String resource = "com/hexagon/galleryapp/mybatis/mybatis-config.xml";

		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 이렇게 정의해놓으면, 생성자에 의한 객체 생성을 막아놓고 아래의 getter 메서드에 의해서만 인스턴스를 가져갈 수 있도록 함
	// 이 메서드를 최초로 호출하지만 인스턴스를 생성하고 나머지 후발로 호출한 자들은 이미 존재하는 인스턴스를 가져가게 됨
	public static MybatisConfig getInstance() {
		if(instance==null) {
			instance = new MybatisConfig();
		}
		return instance;
	}

	// 개발자가 SqlSession 하나를 가져갈 수 있도록 반환해주는 메서드,
	public SqlSession getSqlSession() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return sqlSession;
	}
}
