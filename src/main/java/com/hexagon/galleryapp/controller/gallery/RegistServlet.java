package com.hexagon.galleryapp.controller.gallery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
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
import com.hexagon.galleryapp.dao.MybatisGalleryDAO;
import com.hexagon.galleryapp.dto.Gallery;
import com.hexagon.galleryapp.util.FileManager;

//겔러리의 글쓰기 요청을 처리하는 서블릿
@WebServlet("/gallery/regist")
//원래 파일업로드를 구현하려면, apache에서 제공하는 Fileupload 컴포넌트(라이브러리) 나 외부 라이브러리를 이용해야 했으나, 
//서블릿 3.0 부터는 자체적으로도 업로드 api를 지원함 
@MultipartConfig(
	fileSizeThreshold = 2*1024*1024, //임계값 지정, 파일의 용량이 지정된 값 이상일 경우 메모리가 아닌 임시 디렉토리에 저장
														//일반적으로 서버는 유저가 파일을 업로드하면 임시적으로 메모리에 보관하고난 후, 하드에 저장한다.
														//하지만, 유저가 업로드한 파일의 용량이 너무 클 경우 메모리가 낭비되어버리므로, 메모리대신 임시 디렉토리가 보관해놓고, 하드에 
														//에 확정적으로 저장한다..
	maxFileSize = 10*1024*1024, 		//유저가 업로드 시 파일 1개당 최대 크기 제한
	maxRequestSize =  50*1024*1024 //모든 업로드한 파일의 합산 최대 크기 제한 (업로드 한 파일의 수가 여러개 일 경우 그 합산 크기)
)
public class RegistServlet extends HttpServlet{
	
	// 앞으로 이미지가 저장될 디렉토리
	private final String UPLOAD_DIR="C:" + File.separator+ "Users" +File.separator + "jclle"+ File.separator +"upload" + File.separator;
	
	MybatisGalleryDAO galleryDAO = new MybatisGalleryDAO();
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//클라이언트의 전송  encoding type 이  multipart/form-data 인 경우 그냥 request 객체로는 파라미터를 추출할 수 없고 반드시 
		//파일업로드 컴포넌트를 사용해야 하는데, 우리의 경우 @MultipartConfig 을 사용했기 때문에 파일 업로드 컴포넌트를 사용하지 않아도 파라미터를 추출할수있게 됨..
		
		//파라미터 받기 ( 한글 인코딩 처리는 이미 필터가 처리했음..) 
		String title			= request.getParameter("title");
		String writer		= request.getParameter("writer");
		String content	= request.getParameter("content");
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out	= response.getWriter();
		
		out.print(title+"<br>");
		out.print(writer+"<br>");
		out.print(content+"<br>");
		
		// 일단 우리 os에 upload 디렉토리가 있는지 조사하고 만일 없으면 프로그래밍적으로 만들기
		File uploadDir = new File(UPLOAD_DIR);
		
		if(!uploadDir.exists()) {
			uploadDir.mkdir(); // 디렉토리 만들기
		}
		
		// 생성 또는 존재하는 upload 디렉토리에, 업로드된 이미지파일의 데이터를 읽어서(input) upload에 output
		Part filePart = request.getPart("photo"); // 파일 파라미터 받기 주의) string이 아닌 바이너리 파일이다
		
		// filePart가 존재하거나, 그 사이즈가 0보다 클때만 업로드 처리하자 (OS의 upload 디렉토리로 저장)
		if(filePart!=null&&filePart.getSize() > 0) {
			// 사용자가 업로드한 파일명을 알아 맞추기(헤더값에 실려서 날아온다)
			String filename = FileManager.getFilenameFromHeader(filePart);
			//out.print(filePart.getHeader("content-disposition"));
			out.print("uploaded filename is " + filename + "<br>");
			// 사용자가 업로드한 파일명은 mysql table
			
			/*
			 * OS에 저장 시 사용자가 업로드한 파일명을 그대로 사용해서는 안된다 이유는 한글.jpg
			 * 사용자가 업로드한 사진의 이름은 db에 보관만 해두고 실제 사용자는 개발자가 임의로 부여한 이름을 사용하자
			 * 숫자, 영문자, 날짜
			 * 프로그래밍 분야에서 유일한 id를 사용할 일이 있다면 UUID를 고려해보아도 된다 (Universally)(Unique)ID
			 * 모든 os 및 시스템 뿐 아니라 모든 프로그래밍 언어에서는 UUID를 추출해내는 라이브러리가 이미 탑재되어있다.
			 * */
			String uuid = UUID.randomUUID().toString();
			out.print("extracted my uuid in java is " + uuid + "<br>");
			
			String newName=uuid+"."+FileManager.getExt(filename);
			out.print(newName);
			
			// 파일명이 정해졌으므로, 디스크에 저장
			// 인수1 - 현재 메모리에 떠 있는 원본 파일에 대한 입력스트림
			// 인수2 - 저장할 경로(Path라는 객체를 이욯)
			// 인수3 - 저장시 옵션 StandardCopyOption.REPLACE_EXISTING 동일한 이름의 파일이 있는 경우, 에러를 일으키지 말고 덮어써라
			try(InputStream is=filePart.getInputStream()){ // java7부터 나온 try witgh resouce문임 finally에서 stream등을 자동으로 닫아줌
				// try~with~resorce 문의 사용은 필수가 아니라 기존에 내가 알고있던 try~catch~finally 를 사용해도된다
				// try() 소괄호 안의 스트림을 자동으로 닫아주므로..
				// Path 란 경로를 제어해주는 객체, 그냥 String만으로 경로를 사용하는 것보다 훨씬 더 많은 처리를 지원해줌
				Files.copy(is, Paths.get(UPLOAD_DIR, newName), StandardCopyOption.REPLACE_EXISTING);
			}
			
			// 업로드가 성공되었으므로, DB에 insert하자
			Gallery gallery = new Gallery(); // empty 상태
            gallery.setTitle(title);
            gallery.setWriter(writer);
            gallery.setContent(content);
            gallery.setOriginalName(filename); // 유저가 원래 업로드한 파일명
            gallery.setFilename(newName); // 개발자에 의해 변경된 파일명

            int result = galleryDAO.insert(gallery);
            out.print("<script>");
            if (result==0) {
                out.print("alert('등록 실패');");
                out.print("history.back();");
            }else {
                out.print("alert('등록 성공');");
                out.print("location.href='/gallery/list.jsp';");
            }
            out.print("</script>");
		}
	}
			
}