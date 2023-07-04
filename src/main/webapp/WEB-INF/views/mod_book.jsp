<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <title><%=session.getAttribute("userid")%>님의 주소록 수정</title>
    </head>
    <c:if test="${not empty msg}">
        <script>
            var message = "${msg}";
            alert(message);
        </script>
    </c:if>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            <h3>검색된 주소록</h3>
            <table border="2" summary="addrbook">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>전화번호</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <!--원래 코드-->
                        <c:forEach items="${resultList}" var="row">
                        <td>${row[0]}</td>
                        <td>${row[1]}</td>
                        <td>${row[2]}</td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
            <br>
            <h3>검색된 주소록의 이름과 전화번호입니다.<br>수정하시고자하는 내용을 적고 수정 버튼을 눌러주세요.</h3>
            <table border="1" summary="addrbook">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>전화번호</th>
                    </tr>
                    <tr>
                <form action="update.do" method="post">
                    <!-- 반복문 안쓴 코드 -->
                    <c:forEach items="${resultList}" var="row">
                    <input type="hidden" name="email" value="${resultList[1]}"/>
                    <th><input type="text" name="name" value="${resultList[0]}"/></th>
                    <th><input type="text" name="phone" value="${resultList[2]}"/></th>
                    </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td colspan="3">
                                <button type="submit">수정</button></td>
                </form>
                </tr>
                </tbody>
            </table>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>