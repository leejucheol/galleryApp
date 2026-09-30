package com.hexagon.galleryapp.controller.gallery;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hexagon.galleryapp.dao.GalleryDAO;

@WebServlet("/gallery/delete")
public class DeleteServlet extends HttpServlet {
	GalleryDAO galleryDAO = new GalleryDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		long galleryId = Long.parseLong(request.getParameter("gid"));
		String filename = request.getParameter("filename");

		// 이미지도 삭제
		File file = new File("C:" + File.separator + "Users" + File.separator + "jclle" + File.separator + "upload"+File.separator+filename);  
		System.out.println(filename);
		
		if (file.delete()) {
			System.out.println("파일 삭제됨");
			int result = galleryDAO.delete(galleryId); // DB에서 삭제
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

			out.print("<script>");
			if (result == 1) {
				out.print("alert('success');");
				out.print("location.href='/gallery/list.jsp';");
			} else {
				out.print("alert('fail');");
				out.print("history.back();");
			}
			out.print("</script>");
		}
	}
}
