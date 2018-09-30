package com.profile_package;

import java.io.IOException;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(description = "updating the images", urlPatterns = { "/Question_update" })
public class Question_update extends HttpServlet {

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            Database d = new Database();
            Connection con = d.get_connection();
            HttpSession session=request.getSession(false);
            if(session==null){
                //RequestDispatcher next_page =request.getRequestDispatcher("/login.jsp");
                  //      next_page.forward(request,response);
                  response.sendRedirect("/student_profile.jsp");
            }else{
            Profile pobj=(Profile)session.getAttribute("fb_session");
            String msg = "incorrect";
            if(pobj!=null){
                int question_no=Integer.parseInt(request.getParameter("question_no"));
                String answer=request.getParameter("answer");
            try {
                Statement st= con.createStatement();
                ResultSet rs = st.executeQuery("select ans from questions where ans='"+answer+"' and q_id="+question_no);
                if(rs.next()){
                    int a = 0;
                    a=st.executeUpdate("update user_score set q_id = q_id + 1, hint_no = 1,start_time = NOW()  where id="+pobj.getId());
                     if(a!=0){   
                        msg = "correct";
                     }
                }
                 response.sendRedirect("/ignite/start_game.jsp?message="+msg);
            } catch (SQLException ex) {
                Logger.getLogger(Question_update.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
            }
        }
    }
}
