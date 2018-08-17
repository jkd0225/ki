<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>상세글보기</h2>
<table border="1" width="500">
	<tr>
		<td>글번호</td>
		<td>${vo.num }</td>
	</tr>
	<tr>
		<td>작성자</td>
		<td>${vo.writer }</td>
	</tr>
	<tr>
		<td>제목</td>
		<td>${vo.title }</td>
	</tr>
	<tr>
		<td>내용</td>
		<td><textarea rows="5" cols="50" readonly="readonly">
		${vo.content }</textarea>
	</td>
	<tr>
		<td colspan="2">
			<a href="insert.jsp?num=${vo.num }&ref=${vo.ref}
			&lev=${vo.lev}&step=${vo.step}">답글</a>
		</td>	
	</tr>
</table>
</body>
</html>