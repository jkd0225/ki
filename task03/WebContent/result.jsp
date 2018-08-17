<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:choose>
	<c:when test="${requestScope.code=='success' }">
		<h2>글등록성공</h2>	
	</c:when>
	<c:otherwise>
		<h2>글등록실패</h2>
	</c:otherwise>
</c:choose>
</body>
</html>