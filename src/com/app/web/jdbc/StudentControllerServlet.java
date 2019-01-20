package com.app.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author Piyush Pant
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException { //Overriden method used from Generic Servlet
		super.init();
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
				// read the command parameter
				String theCommand = request.getParameter("command");
				
				//if the command is null, then show students
				if(theCommand==null)
					theCommand="LIST";
				//direct the control to appropriate method based on theCommand
				switch(theCommand) {
				case "LIST":
					listStudents(request,response);
					break;
				case "ADD":
					addStudent(request,response);
					break;
				case "LOAD":
					loadStudents(request,response);
					break;
				case "UPDATE":
					updateStudent(request,response);
					break;
				case "DELETE":
					deleteStudent(request,response);
					break;
				default:
					listStudents(request,response);
				}		
		}
		catch(Exception ex2) {
			throw new ServletException(ex2);
		}
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		// reading student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//delete student from dB
		studentDbUtil.deleteStudent(theStudentId);
		
		// Control back to list-student.jsp
		listStudents(request,response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(id,firstName,lastName,email);
		
		// updating the database with updated values
		studentDbUtil.updateStudent(theStudent);
		
		// sending back to the liststudent
		listStudents(request,response);
		
	}

	private void loadStudents(HttpServletRequest request, HttpServletResponse response)
	    throws Exception{
			
			// read the student id from form data
			String theStudentId = request.getParameter("studentId");
			
			// get the student from db
			Student theStudent = studentDbUtil.getStudent(theStudentId);
			
			//place student in req attr.
			request.setAttribute("THE_STUDENT", theStudent);
			// sent to new update jsp page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
			dispatcher.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		 
		//create a new student object
		Student theStudent = new Student(firstName,lastName,email);
		
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		// send back the info to main page
		listStudents(request,response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		// Get students from database
		List<Student> students = studentDbUtil.getStudents();
		
		// add students to the request attribute
		request.setAttribute("STUDENT_LIST",students);
		
		// send to jsp (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
