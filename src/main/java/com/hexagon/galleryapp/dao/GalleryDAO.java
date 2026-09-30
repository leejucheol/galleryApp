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
			pstmt=con.prepareStatement(sql);
            pstmt.setString(1, gallery.getTitle());
            pstmt.setString(2, gallery.getWriter());
            pstmt.setString(3, gallery.getContent());
            pstmt.setString(4, gallery.getOriginalName());
            pstmt.setString(5, gallery.getFilename());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public List selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List list = new ArrayList();
		
		String sql = "select * from gallery order by gallery_id desc";
		
		con = pool.getConnection();
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			// ResultSet을 대신할 List로 데이터를 옮겨담자
			while(rs.next()) {
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
		}finally {
			pool.release(con, pstmt, rs);
		}
		
		return list;
		
	}
}