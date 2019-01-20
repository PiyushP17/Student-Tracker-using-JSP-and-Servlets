package com.app.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	//private Statement sql=null;
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		Connection conn = null;
		Statement smt = null;
		ResultSet rst = null;
		try {
			//Getting a connection;
			conn = dataSource.getConnection();
			
			//Creating sql statements'
			String sql = "select * from student order by last_name";
			smt = conn.createStatement();
			
			//Executing query
			rst = smt.executeQuery(sql);
			
			//Processing Result
			while(rst.next()) {
				// retrieve data from result
				String first_name = rst.getString("first_name");
				String last_name = rst.getString("last_name");
				String email = rst.getString("email");
				int id = rst.getInt("id");
			 
				//Create new Student object
				Student tempStudent = new Student(id, first_name, last_name, email);
				
				//Add it to the arraylist
				students.add(tempStudent);
			}
			return students;
		}
		finally {
			// closing the connection
			close(conn,smt,rst);
		}
	}
			
		     
	private void close(Connection conn, Statement smt, ResultSet rst) {
		try {
			if(rst!=null)
				rst.close();
			if(smt!=null)
				smt.close();
			if(conn!=null)
				conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void addStudent(Student theStudent) throws Exception {
		Connection insConn = null;
		PreparedStatement insSmt = null;
		
		try {
			// get the database connection
			insConn = dataSource.getConnection();
			// write sql for insert
			String insSql = " insert into student "
							+"(first_name,last_name,email) "
							+"values(?, ?, ?)";
			insSmt = insConn.prepareStatement(insSql);
			// Set the param values i.e ?
			insSmt.setString(1, theStudent.getFirstName());
			insSmt.setString(2, theStudent.getLastName());
			insSmt.setString(3, theStudent.getEmail());
			
			//execute sql
			insSmt.execute();
		}
		finally {
			close(insConn,insSmt,null);
		}
		
	}
	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection uConn = null;
		PreparedStatement uSmt = null;
		ResultSet uRst = null;
		int studentId;
		
		try {
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to db
			uConn = dataSource.getConnection();
			
			//create sql to get selected student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			uSmt = uConn.prepareStatement(sql);
			
			// set params ?
			uSmt.setInt(1, studentId);
			
			//execute statement
			uRst=uSmt.executeQuery();
			
			//retrieve data from set row
			if(uRst.next()) {
				String firstName = uRst.getString("first_name");
				String lastName = uRst.getString("last_name");
				String email = uRst.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id : "+studentId);
			}
			return theStudent;
		}
		finally {
			close(uConn,uSmt,uRst);
		}
	}
	public void updateStudent(Student theStudent) throws Exception {
		Connection conn = null;
		PreparedStatement smt = null;
		try {
		//get db connection
		conn = dataSource.getConnection();
		// Create sql update statement
		String sql = "update student set first_name=?, last_name=?, email=?"
					+ "where id =?";
		//prepare statement
		smt = conn.prepareStatement(sql);
		//set parameters ? 
		smt.setString(1,  theStudent.getFirstName());
		smt.setString(2,  theStudent.getLastName());
		smt.setString(3,  theStudent.getEmail());
		smt.setInt(4,  theStudent.getId());
		// Execute query
		smt.execute();
		}
		finally {
			close(conn,smt,null);
		}
	}
	public void deleteStudent(String theStudentId) throws Exception {
		Connection delConn = null;
		PreparedStatement delSmt = null;
		
		try {
			int id = Integer.parseInt(theStudentId);
			// Getting a connection to a database
			delConn = dataSource.getConnection();
			
			// Create Delete Sql
			String sql = "delete from student where id=?";
			
			//Prepare statement
			delSmt = delConn.prepareStatement(sql);
			
			//set Parameters
			delSmt.setInt(1,id);
			
			//execute statement
			delSmt.execute();
			
		}
		finally {
			close(delConn,delSmt,null);
		}
		
	}
}