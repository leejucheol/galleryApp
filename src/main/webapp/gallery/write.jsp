<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>


<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.js"></script>
</head>
<body>
	<div class="container">
		<!-- 게시판 글쓰기 폼을 부트스트랩 디자인으로 제작해보기  -->
		<form id="form1">
			<div class="form-group">
				<input type="text" placeholder="Your title..." class="form-control" name="title">	
			</div>
			
			<div class="form-group">
				<input type="text" placeholder="Your Name..." class="form-control" name="writer">	
			</div>
			
			<div class="form-group">
				<input type="file" placeholder="choose Your file..." class="form-control" name="photo">	
			</div>
			
			<div class="form-group">
				<textarea placeholder="Your Name..." class="form-control" id="content" name="content"></textarea>	
			</div>
	
			<div class="form-group">
				<button type="button" class="btn btn-primary" id="bt_regist">등록</button>
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
			
			//글 등록버튼 눌렀을때..
			$("#bt_regist").click(function(){
				//서버에 입력한 텍스트파라미터 뿐 아니라, 바이너리 파일도 함께 전송...	
				$("#form1").attr({
					enctype:"multipart/form-data",  //폼으로 전송할 대상 데이터가 텍스트와  바이너리가 복합되어 있을때는 반드시 이 속성 지정
					action:"/gallery/regist", //이 요청을 받을 서블릿 매핑 url
					method:"POST" //textarea, 바이너리 등의 덩치 큰 데이터가 전송되므로 반드시 POST 로 전송  
				});
				$("#form1").submit(); //전송이 발생!!				
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