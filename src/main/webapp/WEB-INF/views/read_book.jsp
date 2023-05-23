<%-- 
    Document   : index
    Created on : 2023. 5. 23., 오전 12:32:25
    Author     : wwwhyuk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="mytags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <title><%=session.getAttribute("userid")%>님의 주소록 조회</title>
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
            <c:catch var="errorReason">
                <mytags:addrbook user="root" password="1q2w3e4r" schema="mail" table="addrbook" />
            </c:catch>
            ${empty errorReason ? "<noerror/>" : errorReason}
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>
