package com.hexagon.galleryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hexagon.galleryapp.dto.Gallery;
import com.hexagon.galleryapp.pool.PoolManager;

/*
 
오직 gallery 테이블에 대한 CRUD 만을 처리하는 객체*/
public class GalleryDAO {
	PoolManager pool = PoolManager.getInstance();

	public int insert(Gallery gallery) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "insert into gallery(title, writer, content, original_name, filename) values(?,?,?,?,?)";

		con = pool.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, gallery.getTitle());
			pstmt.setString(2, gallery.getWriter());
			pstmt.setString(3, gallery.getContent());
			pstmt.setString(4, gallery.getOriginalName());
			pstmt.setString(5, gallery.getFilename());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pool.release(con, pstmt);
		}

		return result;
	}

	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Gallery> list = new ArrayList<Gallery>();

		String sql = "select * from gallery order by gallery_id desc";

		con = pool.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			// ResultSet을 대신할 List로 데이터를 옮겨담자
			while (rs.next()) {
				Gallery gallery = new Gallery();

				gallery.setGalleryId(rs.getLong("gallery_id"));
				gallery.setTitle(rs.getString("title"));
				gallery.setWriter(rs.getString("writer"));
				gallery.setContent(rs.getString("content"));
				gallery.setOriginalName(rs.getString("original_name"));
				gallery.setFilename(rs.getString("filename"));
				gallery.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
				gallery.setHit(rs.getLong("hit"));

				list.add(gallery);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pool.release(con, pstmt, rs);
		}

		return list;
	}

	public Gallery select(long galleryId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Gallery gallery = null; // rs를 대신할 대체제

		String sql = "select * from gallery where gallery_id=?";

		con = pool.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, galleryId);
			rs = pstmt.executeQuery(); // query 실행 및 표반환
			// 레코드가 존재할때만 query에 옮겨담자
			if (rs.next()) { // 커서가 다음으로 이동 시 true를 반환하면 record가 있다는 의미
				gallery = new Gallery();

				gallery.setGalleryId(rs.getLong("gallery_id"));
				gallery.setTitle(rs.getString("title"));
				gallery.setWriter(rs.getString("writer"));
				gallery.setContent(rs.getString("content"));
				gallery.setOriginalName(rs.getString("original_name"));
				gallery.setFilename(rs.getString("filename"));
				gallery.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
				gallery.setHit(rs.getLong("hit"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.release(con, pstmt, rs);
		}

		return gallery;
	}

	public int update(Gallery gallery) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "update gallery set title=?, writer=?, content=?, original_name=?,filename=? where gallery_id=?";

		con = pool.getConnection();

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, gallery.getTitle());
			pstmt.setString(2, gallery.getWriter());
			pstmt.setString(3, gallery.getContent());
			pstmt.setString(4, gallery.getOriginalName());
			pstmt.setString(5, gallery.getFilename());
			pstmt.setLong(6, gallery.getGalleryId());
			
			result = pstmt.executeUpdate(); // DML 이므로 0일 경우 실패
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.release(con, pstmt);
		}

		return result;
	}
	
	public int delete(long galleryId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		con = pool.getConnection();
		String sql="delete from gallery where gallery_id=?";
		try {
			pstmt= con.prepareStatement(sql);
			pstmt.setLong(1, galleryId);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			pool.release(con, pstmt);
		}
		return result;
	}
}