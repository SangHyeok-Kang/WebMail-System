<%-- 
    Document   : relay_mail.jsp
    Author     : 20183109 강상혁
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
        </div>

        <div id="main">        
            <form enctype="multipart/form-data" method="POST" action="write_mail.do" >
                <table>   
                    <caption style="color: rgba( 255, 255, 255, 0 );">임시 메일 작성 화면</caption>
                   <tr>
                        <th colspan="2" style="background-color: rgba( 255, 255, 255, 0 ); border: 0px">
                            
                        </th>
                    </tr>
                 
                    <tr>
                        <th scope="col"> 수신 </th>
                        <td> <input type="text" name="to" size="80"> </td>
                    </tr>
                    <tr>
                        <th scope="col">참조</th>
                        <td> <input type="text" name="cc" size="80" value="${refer}">  </td>
                    </tr>
                    <tr>
                        <th scope="col"> 메일 제목 </th>
                        <td> <input type="text" name="subj" size="80" 
                                    value="${subj}" > </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">
                            <textarea rows="15" name="body" cols="80">${body}</textarea> 
                        </td>
                    </tr>
                    <tr>
                        <th scope="col">첨부 파일</th>
                        <td> <input type="file" name="file1"  size="80">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="메일 보내기">
                            <input type="reset" value="다시 입력">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <%@include file="../footer.jspf"%>
    </body>
</html>
