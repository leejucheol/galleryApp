package com.hexagon.galleryapp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//클라이언트의 요청을 받거나,    db에서 가져온 하나의 레코드를 담기 위한  dummy 한 클래스 
// 추후 Spring 으로 확장될 경우  db에서 가져온 하나의 레코드를 담기 위한 목적의 클래스는 Domain 객체로 분류될 예정..
// 이 경우 DTO는 클라이언트의 파라미터나, 응답 정보에 사용될 예정..
//아래와 같이 getter/setter 코드를 대신하여 어노테이션 설정만으로 자동으로 지원되는 라이브러리를 가리켜 Lombok 이라함
//사용하기 위한 방법 
// 1) Eclipse에 설치  2) 라이브러리 보유(maven) 
@Getter
@Setter
public class Gallery {
	private Long galleryId; //레코드가 존재하지 않는 다는 뜻의 null을 사용하기 위해 long 을 객체형(Wrapper)으로 선언
	private String title;
	private String writer;
	private String content;
	private String originalName;
	private String filename;
	private LocalDateTime createdAt;
	private long hit; 	
}