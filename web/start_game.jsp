<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.profile_package.Profile"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.profile_package.Database"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" session="true"%>
<%
                Profile pobj=(Profile)session.getAttribute("fb_session");
                out.println(session.getAttribute("fb_session"));
                int question_no = 1;
                if(pobj!=null){
                    Database d = new Database();
                    Connection con = d.get_connection();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select q_id from user_score where id = "+pobj.getId());
                    if(rs.next()){
                        question_no = rs.getInt("q_id");
                    }
                    else{
                        st.executeUpdate("insert into user_score(id,q_id,hint_no) values ('"+pobj.getId()+"',1,1)");
                    }
                }
               
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%-- <meta http-equiv="refresh" content="5" url="google.com"/> --%>
<title>Insert title here</title>

<style>
* {
    box-sizing: border-box;
}

.row {
    display: flex;
}

/* Create three equal columns that sits next to each other */
.column {
    flex: 33.33%;
    padding: 5px;
}
</style>
</head>
<body>
<center>
	Question no:  <%= question_no%>
		<div class="row">
			<h4>Base image :</h4>
                                    <img src="/ignite/assets?ques=<%= question_no%>&file=1" alt="wait" style="width:100%">
 	
		</div>
</center>
	<form action="Question_update" method="post">
	
	<div class="row">
  		<div class="column">
  			<h4> hint_image 1:</h4>
                        <img src="/ignite/assets?ques=<%= question_no%>&file=2" alt="wait" style="width:100%">
 		 </div>
  		<div class="column">
  			<h4> hint_image 1:</h4>
    		<img src="/ignite/assets?ques=<%= question_no%>&file=3" alt="wait" style="width:100%">
 		</div>
  		<div class="column">
  			<h4> hint_image 1:</h4>
    		<img src="/ignite/assets?ques=<%= question_no%>&file=4" alt="wait" style="width:100%">
 		</div>
	</div>
	<br>Enter your answer: <input type="text" name="answer">
        <input type="hidden" name="question_no" value="<%= question_no%>"/>
	 <button type="submit" value="Submit">Submit</button>
	</form>
</body>
<script>
    var urlParams = new URLSearchParams(window.location.search);
    if(urlParams.get('message')=="correct"){
        alert("Correct answer");
    }
    else if(urlParams.get('message')=="incorrect"){
        alert("incorrect answer");
    }
    
    
    setInterval(function(){ location.href="/ignite/start_game.jsp"; }, 30000);
</script>
</html>   