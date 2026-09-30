package com.hexagon.galleryapp.controller.gallery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.hexagon.galleryapp.dao.GalleryDAO;
import com.hexagon.galleryapp.dto.Gallery;
import com.hexagon.galleryapp.util.FileManager;

@WebServlet("/gallery/update")

//아래의 어노테이션을 선언해야 클라이언트가 전송한 복합데이터를 받을 수 있다.. 즉 클라이언트가 multipart/form-data 속성으로 전송하면
//기존 서블릿의 request 만으로는 파라미터를 받을 수 없다..

@MultipartConfig(
	fileSizeThreshold = 2*1024*1024,   // 2MB 까지는 메모리에 기억하지만, 그 초과일 경우 메모리에 저장하기엔 너무 크므로 서버의 디렉토리에 임시보관하여 메모리를 대신함  
	maxFileSize = 10*1024*1024, // 10MB 파일 1개당 최대 크기 제한
	maxRequestSize = 50*1024*1024 // 파일이 여러개일 경우 모든 파일의 합산 최대 크기 제한
)

public class UpdateServlet extends HttpServlet {
	GalleryDAO galleryDAO = new GalleryDAO();
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 
		*/
		
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		String originalName = request.getParameter("originalName");
		String filename = request.getParameter("filename");
		long galleryId = Long.parseLong(request.getParameter("gid"));
		
		System.out.println("title" +title);
		System.out.println("writer" +writer);
		System.out.println("content" +content);
		System.out.println("originale_name" +originalName);
		System.out.println("filename" +filename);
		System.out.println("galleryId" +galleryId);
		
		Gallery gallery = new Gallery(); // empty 상태
		gallery.setTitle(title);
		gallery.setWriter(writer);
		gallery.setContent(content);
		gallery.setOriginalName(originalName);
		gallery.setFilename(filename);
		gallery.setGalleryId(galleryId);
		
		
		// part는 업로드된 파일 정보가 담겨진 객체
		Part filePart = request.getPart("photo");
		if(filePart!= null && filePart.getSize()>0) { // 사용자가 새로운 파일을 업로드하면
			// 위의 Gallery DTO 의 값들중 original_name과 filename은 교체해놓아야 아래쪽 update가 새롭게 변경된 내용을 바탕으로 진행됨
			String userFilename=FileManager.getFilenameFromHeader(filePart); // 유저가 업로드한 파일명 추출(헤더로 부터)
			
			// 개발자가 원하는 파일명으로 전환
			String newName = UUID.randomUUID().toString()+"."+FileManager.getExt(userFilename);
			
			// 메모리에 올라온 이미지파일을 읽어서, c:~~/upload 디렉토리에 출력
			// Files는 nio 패키지에서 지원하는 객체인데 이 객체를 이용하면 개발자가 일일이 Inputstream과 OutputStream을 제어할 필요가 없다
			try(InputStream is=filePart.getInputStream()){ // try 소괄호 안의 스트림을 자동으로 닫아줌(따라서 개발자가 close()할 필요가 없다 java7에서 추가됨)
															// try with resurce문
				Files.copy(is, Paths.get("C:" + File.separator + "Users" + File.separator + "jclle" + File.separator + "upload", newName), StandardCopyOption.REPLACE_EXISTING);
			}
			
			gallery.setOriginalName(userFilename);
			gallery.setFilename(newName);
			
			// 기존 이미지는 삭제되어야 하므로, java에서 파일을 제어해야함
			File file = new File("C:" + File.separator + "Users" + File.separator + "jclle" + File.separator + "upload");
			file.delete(); // 파일삭제;
		}
		// 만일 유저가 새로운 이미지를 업로드하면, 위의 Gallery DTO의 갋들중 origianl_name과 filename은 교체해놓아야 아래쪽 update가 새롭게 변경된 내용을 바탕으로 진행됨
		
		int result = galleryDAO.update(gallery);
		
		response.setContentType("text/html; charset=UTF-8");
 		PrintWriter out = response.getWriter();
		
		out.print("<script>");
		if(result!=0) {
			out.print("alert('update success');");
			out.print("location.href='/gallery/content.jsp?gid="+ galleryId +"';");			
		}else {
			out.print("alert('update fail');");
			out.print("history.back();");
		}
		out.print("</script>");
	}
}
