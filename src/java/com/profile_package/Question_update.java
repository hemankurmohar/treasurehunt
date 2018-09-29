package com.profile_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(description = "updating the images", urlPatterns = { "/Question_update" })
public class Question_update extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//variables
        String id=null;
        Connection con=null;
        String query1=null,query2=null,image=null,info=null; 
        PreparedStatement pt1=null,pt2=null;
        int q_id=0,hint_no=0,r_time=5;
        String ans1=null;
        private int total_question=15;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            System.out.println("in question_update_page");	
            HttpSession session=request.getSession(false);
		if(session==null){
                    System.out.println("Session not created");
                }
		info=(String) request.getAttribute("info");   // reading the response of page to check the answer and validate the page  from whom page request is coming
		Profile pobj=(Profile)session.getAttribute("fb_session");
		
		id=pobj.getId();  // reading the current user id
		try{
                    Class.forName("com.mysql.jdbc.Driver");
		    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ignite","root","hemankur#9041");
		}catch (ClassNotFoundException | SQLException e) {
				System.out.println("calss not found"+ e.getMessage());
		}
		try{
		    query1="select *from user_score where id=?";
		    pt1=con.prepareStatement(query1);
		    pt1.setString(1,id);
		    ResultSet rs=pt1.executeQuery();
		    while(rs.next()){
		    	q_id=rs.getInt(2);
		    	hint_no=rs.getInt(3);
		    	r_time=rs.getInt(4);
			}
		}catch(SQLException e) {
		    System.out.println("SQLException caught on reading the user_score table: " +e.getMessage());
		}
		
		// code for checking form which page request is coming and if it is form start_game page with submit ans then check the answer
		System.out.println("info information: "+info);
                if("from_read_question_page"==info){
			//System.out.println("From read_question page");
			info=null;
		}
		else if(info==null){
                    String resp_ans = request.getParameter("info");
                    if(check_ans_fun(resp_ans)){
			String query="UPDATE user_score SET q_id= ?,hint_no=?,remaning_time=? WHERE id=?";
			try {
				pt1=con.prepareStatement(query);
				pt1.setInt(1,q_id+1);
                                pt1.setInt(2,1);
                                pt1.setInt(3,r_time);
                                pt1.setString(4,id);
				int nr=pt1.executeUpdate();
				if(nr>0){
					System.out.println("Question number is updated after validatin of ans");
                                        RequestDispatcher next_page =request.getRequestDispatcher("/Read_question");
                                        next_page.forward(request,response);
				}
				else{
					System.out.println("Error in update of correst ansewer question");
					
				}
			} catch (SQLException e) {
				System.out.println("MYQL error in correct answer validation" +e.getMessage());
			}
                    }
                    else{
                        System.out.println("Wrong Answer");
                    }
			
		}
		// query for getting the image a/c to hint no
		switch(hint_no){
	    	case 1:
	    		 query2 ="Select image1 from questions where q_id= ?";
	    		 break;
	    	case 2:
	    		query2 ="Select image2 from questions where q_id= ?";
	    		break;
	    	case 3:
	    		query2 ="Select image3 from questions where q_id= ?";
	    		break;
	    	case 4:
	    		query2 ="Select image4 from questions where q_id= ?";
	    		break;
	    	default:
	    		// here i have to write that condition which make user to remain in same page till he do not answer;
                    System.out.println("No more hint");
                    RequestDispatcher rd = request.getRequestDispatcher("/studebt_profile.jsp");
                    rd.forward(request, response);
                         
                }
	try{
	    pt2=con.prepareStatement(query2);
	    pt2.setInt(1,q_id);
	    
	    ResultSet rs2=pt2.executeQuery();
	    while(rs2.next()){
	    	 image=rs2.getString(1);
	    }
	}catch(SQLException e){
		System.out.println("SQLException error on readig the image: " +e.getMessage());
	}
        System.out.println("image name: "+image);
    
  // sending the image to statr_game page
        if(image!=null){
            pobj.setCurrent_image(image);
            //update_fun(); // calling update function
            request.setAttribute("image",image);
            RequestDispatcher rd = request.getRequestDispatcher("/start_game.jsp");
            rd.forward(request, response);
	}
        else{
            System.out.println("image not found");
            RequestDispatcher rd = request.getRequestDispatcher("/start_game.jsp");
            rd.forward(request, response);
        }
    }
	
	// functions
	void update_fun(){
            String query=null;

            // updating the question hint no
            query="UPDATE user_score SET hint_no = ?, remaning_time = ? WHERE id=?";
            try {
                    PreparedStatement pt =con.prepareStatement(query);
                    pt.setInt(1,hint_no+1);
                    pt.setInt(2,r_time);
                    pt.setString(3,id);
                    int nr=pt.executeUpdate();
                    if(nr>0){
                            System.out.println("hint_number updated");
                    }
                    else {System.out.println("Error on updateing the hint_number score_table");}
            } catch (SQLException e) {
                    System.out.println("SQLException caught in update function " +e.getMessage());
            }
            return;
	}
	
	boolean check_ans_fun(String ans1){
            System.out.println("in answer check");
            query1="select ans from user_score where q_id=?";
            try {
                pt1=con.prepareStatement(query1);
                pt1.setInt(1,q_id);
                ResultSet rs=pt1.executeQuery();
                while(rs.next()){
                    if(ans1==rs.getString(1)) return true;;
                    }
            } catch (SQLException e) {
                    System.out.println("Sql error at in check_ans_fun"+e.getMessage());
            }
            return false;
	}
}
