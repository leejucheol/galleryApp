package com.hexagon.galleryapp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.hexagon.galleryapp.dto.Gallery;
import com.hexagon.galleryapp.mybatis.MybatisConfig;

// DAO의 존재는 어떠한 기술을 사용하던 변함없음. 즉 jdbc, mybatis, jpa하던 DAO를 사용해야함
// 즉 기술과 상관없이 데이터베이스 계층을 처리하는 역할을 DAO라하는 것임 (애플리케이션 설계에서의 역할을 의미함)
public class MybatisGalleryDAO {

	// 결국 개발자는 SqlSession이라는 객체를 통해 쿼리를 간접 수행하기 때문에 SqlSessionFactory로부터 SqlSession만 가져와서 일시키면 됨
	MybatisConfig mybatisConfig = MybatisConfig.getInstance();

	// SqlSession은 내부적으로 closable 인터페이스를 구현해놓았으므로 try~with~resource문에 의한 자원 자동 해제가 가능
	
	// 한건 넣기
	public int insert(Gallery gallery) {
		try (SqlSession sqlSession = mybatisConfig.getSqlSession()) {
			int result = 0;
			// 쿼리문을 대신 실행해주는 SqlSession얻기
			result = sqlSession.insert("com.hexagon.galleryapp.dto.Gallery.insert", gallery); // mybatis에게 부탁
			sqlSession.commit();
			return result;
		}
	}

	// 모든 레코드 가져오기
	public List selectAll() {

		try (SqlSession sqlSession = mybatisConfig.getSqlSession()) {
			List list = sqlSession.selectList("com.hexagon.galleryapp.dto.Gallery.selectAll");
			// select문은 테이블을 레코드를 변경하지 않아 transcation대상이 아님
			return list;
		}
	}
	
	public Gallery select(long galleryId) {
		try(SqlSession sqlSession = mybatisConfig.getSqlSession()){
			return sqlSession.selectOne("com.hexagon.galleryapp.dto.Gallery.select", galleryId);
		}
	}
	
	public Gallery update(Gallery gallery) {
		try(SqlSession sqlSession = mybatisConfig.getSqlSession()){
			sqlSession.update("com.hexagon.galleryapp.dto.Gallery.update", gallery);
		}
		return gallery;
	}
	
	public int delete(long galleryId) {
		try(SqlSession sqlSession = mybatisConfig.getSqlSession()){
			return sqlSession.delete("com.hexagon.galleryapp.dto.Gallery.delete", galleryId);
		}
	}
	
	
	
	
}
