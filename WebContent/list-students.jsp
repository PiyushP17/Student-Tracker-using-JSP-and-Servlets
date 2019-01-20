<%-- <%@ page import="java.util.*, com.app.web.jdbc.*"%> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title> Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="css/style.css" />
</head>
	
<%--<%
	// get the students from the request object (sent by servlet)
	List<Student> theStudents = (List<Student>) request.getAttribute("STUDENT_LIST");
--%>
<body>
	<div id="wrapper">
		<div id="header">
			<h2> XYZ University </h2>
		</div>
	</div>
	
	<div id="container">
		<div id="content">
			<!-- Button  -->
			<input type="button" value="Add Student" 
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class = "add-student-button" />
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="temp" items="${STUDENT_LIST}">
				
					<!-- Setting up update link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet" >
						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${temp.id}" />
					</c:url>
					
					<!-- Setting up delete link for each student -->
					<c:url var="deleteLink" value="StudentControllerServlet" >
						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${temp.id}" />
					</c:url>
					<tr>
						<td>${temp.firstName}</td>
						<td>${temp.lastName}</td>
						<td>${temp.email}</td>
						<td> <a href="${tempLink}">Update</a> 
								|
							<a href="${deleteLink}" id="del" onclick="if (!(confirm('Are your sure you want to delete this student?'))) return false;">Delete</a>
						</td>
					</tr>
				</c:forEach>
						
				<%--  <% for(Student temp : theStudents) { %>
					<tr>
						<td> <%= temp.getFirstName() %></td>
						<td> <%= temp.getLastName() %></td>
						<td> <%= temp.getEmail() %></td>
					</tr>	
				<% } %>--%>
			</table>
		</div>
	</div>
</body>
</html>