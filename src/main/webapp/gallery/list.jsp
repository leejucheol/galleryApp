<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.hexagon.galleryapp.dto.Gallery"%>
<%@page import="java.util.List"%>
<%@page import="com.hexagon.galleryapp.dao.GalleryDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%!	GalleryDAO galleryDAO = new GalleryDAO();%>
<%
	//Generic  을  선언하지 않아도 되지만, 꺼내서 쓸때 형변환해야 하는 불편함이 잇으므로, collection 을 사용할 경우 
	//제너릭을 생활화 하자!!!
	List<Gallery> list = galleryDAO.selectAll();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>

	<div class="container">
		<h2>Hover Rows</h2>
		<p>The .table-hover class enables a hover state on table rows:</p>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>No</th>
					<th>이미지</th>
					<th>제목</th>
					<th>작성자</th>
					<th>파일명</th>
					<th>등록일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="7">
						<button class="btn btn-primary" onClick="location.href='/gallery/write.jsp';">글등록</button>
					</td>
				</tr>
				
				<%for(Gallery gallery : list){%>
				<tr>
					<td>John</td>
					<!-- 웹 브라우저는 보안상 로컬 os 자원을 절대로 접근할 수 없다 -->
					<td><img src="\image\<%=gallery.getFilename() %>" width="45px"></td>
					<td><%=gallery.getTitle() %></td>
					<td><%=gallery.getWriter() %></td>
					<td><%=gallery.getOriginalName() %></td>
					<td><%=gallery.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) %></td>
					<td><%=gallery.getHit() %></td>
				</tr>
				<%} %>
			</tbody>
		</table>
	</div>

</body>
</html>