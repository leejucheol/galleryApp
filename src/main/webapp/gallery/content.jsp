<%@page import="com.hexagon.galleryapp.dao.MybatisGalleryDAO"%>
<%@page import="com.hexagon.galleryapp.dto.Gallery"%>
<%@page import="com.hexagon.galleryapp.dao.GalleryDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%!MybatisGalleryDAO galleryDAO = new MybatisGalleryDAO();%>
<%
long galleryId = Long.parseLong(request.getParameter("gid"));
// String sql = "select ** from gallery where gallery_id=" + galleryId;

Gallery gallery = galleryDAO.select(galleryId);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>


<!-- include summernote css/js -->
<link
	href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.js"></script>
</head>
<body>
	<div class="container">
		<!-- 게시판 글쓰기 폼을 부트스트랩 디자인으로 제작해보기  -->
		<form id="form1">
			<input type="hidden" name="gid" value="<%=gallery.getGalleryId()%>">
			<div class="form-group">
				<input type="text" value="<%=gallery.getTitle()%>"
					class="form-control" name="title">
			</div>

			<div class="form-group">
				<input type="text" value="<%=gallery.getWriter()%>"
					class="form-control" name="writer">
			</div>

			<div class="form-group">
				<input type="hidden" name="originalName"
					value="<%=gallery.getOriginalName()%>"> <input
					type="hidden" name="filename" value="<%=gallery.getFilename()%>">

				<img src="/image/<%=gallery.getFilename()%>" width="50px"> <input
					type="file" placeholder="choose Your file..." class="form-control"
					name="photo">
			</div>

			<div class="form-group">
				<textarea placeholder="Your Name..." class="form-control"
					id="content" name="content"></textarea>
			</div>

			<div class="form-group">
				<button type="button" class="btn btn-primary" id="bt_update">수정</button>
				<button type="button" class="btn btn-primary" id="bt_delete">삭제</button>
				<button type="button" class="btn btn-primary" id="bt_list">목록</button>
			</div>

		</form>
	</div>

	<script type="text/javascript">
		$(function(){
			$("#content").summernote({
				placeholder:"내용을 입력하세요",
				height:250
			});	
			
			$("#content").summernote("code", "<%=gallery.getContent()%>");
			
			//글 수정
			$("#bt_update").click(()=>{
				if(confirm("Do you want to ")){
					$("#form1").attr({
						action:"/gallery/update",
						method:"POST", // Post 는 HTTP 전송 방식 중 body에 데이터를 전송하겠다는 의미
						enctype:"multipart/form-data" // 전송 데이터가 텍스트 뿐만 아니라 바이너리 파일도 포함된 즉 복합된(multipart) 데이터를 전송하려면 반드시 지정
														// 만일 지정하지 않으면 텍스트만 전송
					});
					$("#form1").submit();
				}
			});
			
			// 삭제 버튼
			$("#bt_delete").click(()=>{
				if(confirm("Do you want to delete?")){
					// 삭제 요청, form  태그안에 hidden으로 전송할 파라미터 여러게 이믈이므로 post로 전송
					$("#form1").attr({
						action:"/gallery/delete",
						method:"POST"
					});
					$("#form1").submit();
				}
			});
			
			//글 목록 버튼 눌렀을때...
			$("#bt_list").click(function(){
				// location.href="/gallery/list.jsp"; // 바닐라 js
				$(location).attr("href", "/gallery/list.jsp");
			});
		});
	</script>

</body>
</html>