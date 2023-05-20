<%-- 
    Document   : login_fail
    Created on : 2022. 6. 10., 오후 3:28:28
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>




<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 실패</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script type="text/javascript">
            function gohome() {
                window.location = "${pageContext.request.contextPath}"
            }
        </script>
    </head>
    <body onload="setTimeout('gohome()', 5000)">

        <%@include file="header.jspf"%>

        <p id="login_fail">

            세션이 만료되어 접근 권한이 없습니다. <br>

            다시 로그인하시기 바랍니다. <br>

            5초 뒤 자동으로 초기 화면으로 돌아갑니다.

            자동으로 화면 전환이 일어나지 않을 경우
            <!-- <a href="/WebMailSystem/" title="초기 화면">초기 화면</a>을 선택해 주세요.-->
            <a href="${pageContext.request.contextPath}" title="초기 화면">초기 화면</a>을 선택해 주세요.
        </p>

        <%@include file="footer.jspf"%>

    </body>
</html>
