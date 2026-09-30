package com.hexagon.galleryapp.util;

import javax.servlet.http.Part;

// 개발자가 파일과 관련되어 재사용 가능성이 높은 코드들을 모아놓을 파일관련 util class
public class FileManager {
	
	// HTTP 헤더값으로부터 파일명만 추출해내는 메서드 정의
	public static String getFilenameFromHeader(Part filePart) {
		String result = null;
		String[] disposition = filePart.getHeader("content-disposition").split(";"); //세미콜론을 기준으로 문자열을 분리
		
		System.out.print(disposition);
		
		// 배열을 대상으로 반복문을 돌리면서 filename= 으로 시작하는 문자열만 사용
		for(String str: disposition) {
			if(str.trim().startsWith("filename")) { // filename 앞에 공백문자가 표함되어있어, 공백을 제거
				System.out.print(str + "<br>");
				System.out.print("Total = 위치한 index는 "+ str.indexOf("=") + "<br>");
				String filename=str.substring(str.indexOf("=")+1); //시작 index만 명시하면 문자열의 끝까지를 의미함
				System.out.print(filename + "<br>");
				result=filename.replace("\"", "");
			}
		}
		return result;	
	}

	
	// 파일명 중 확장자만 추출하기
	public static String getExt(String filename) {
		// ex) dfa....dsaf...asdfasd.png 
		
		return filename.substring(filename.lastIndexOf(".")+1);
	}
}
