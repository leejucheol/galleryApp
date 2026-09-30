package com.hexagon.galleryapp.controller.gallery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 브라우저가 톰켓에게 정적자원(html, css, image 등)을 요청하면 개발자가 정의해놓은 서블릿이 관여할 필요없이
 * 톰켓 스스로가 해당 정적자원을 읽어들여 응답정보로 보낸다
 * ex) 브라우저가 http://localhost:8888/gallery/a.png와 같은 정적 자원을 요청하면 Tomcat이 해당 이미지를 응답 정보로 보내준다
 * 
 * 하지만 우리의 경우 Tomcat이 upload 디렉토리를 보유하고 있지 않으므로, 자동으로 이미지를 보내주는 기능이 없다
 * 해결하기 위해 servlet java이므로 tomcat 외부의 os 차원의 디렉토리를 접근할 수 있다. 따라서 서블릿에서 해당 이미지를 스트림으로 읽어들여
 * 응답 정보에 담아두면, Tomcat이 평소처럼 웹브라우저에 해당 이미지 정보를 보내준다
 * 
 * */
@WebServlet("/image/*") // ex) localhost:8888/image/a.png
public class ImageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 웹 브라우저에서 요청한 자원 알아맞추기 ex) localhost:8888/text.png <-- test.png 를 알아맞추기
		String filename = request.getPathInfo();
		System.out.println("requested static resource name is " + filename);
		
		// 외부 디렉토리에 접근하여 이미지를 읽어오자
		// 파일을 대상으로 한 입력 스트림
		File file = new File("C:\\Users\\jclle\\upload", filename);
		// 고양이가 응답정보로 보낼 컨텐츠는 오늘은 html이 아니라 우리가 읽어들일 이미지이다.
		response.setContentType(getServletContext().getMimeType(file.getName()));
		response.setContentLength((int)file.length());
		
		try(FileInputStream fls = new FileInputStream(file)){
			OutputStream os = response.getOutputStream(); // 고양기가 response 객체에 들어있는 출력스트림을 이용하여 컨텐츠를 응답정보로 만들기 때문에
			int data=-1;
			while(true) {
				data = fls.read();
				if(data==-1)break;
				// 어디에 저장?
				os.write(data);
			}
		}
	}
}
