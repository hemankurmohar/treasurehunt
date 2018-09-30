/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.profile_package;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 *
 * @author hemankurmohar
 */
public class assets extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
                int question_no = Integer.parseInt(request.getParameter("ques"));
                int image_no = Integer.parseInt(request.getParameter("file"));
                System.out.println("anhcjdc");
                HttpSession session=request.getSession(false);  
                System.out.println("anhcjdc1");
                
                Profile pobj=(Profile)session.getAttribute("fb_session");
                System.out.println("ssdsd"+pobj.getId());
                
                if(pobj==null){
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                String user_id = pobj.getId();
                try{
                    System.out.println("anhcjdc46");
                     Class.forName("com.mysql.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ignite","root","hemankur#9041");
                    System.out.println("anhcjdc5");
		    String query="select * from user_score where id = ?";
                    PreparedStatement pt1 = con.prepareStatement(query);
                      System.out.println("anhcjdc5");
		  
		    pt1.setString(1,user_id);
		    ResultSet rs=pt1.executeQuery();
                      System.out.println("anhcjdc5");
		  
                   // con.close();
                    if(rs.next()){
                        System.out.println("anhcjdc4");
                
                        int q_no = rs.getInt("q_id");
                        int hint_no = rs.getInt("hint_no");
                        String file_name = "";
                        if(q_no == question_no && hint_no >= image_no){
                            response.setContentType("image/jpg");
                            String pathToWeb = getServletContext().getRealPath(File.separator);
                            file_name = String.valueOf(question_no)+"_"+String.valueOf(image_no)+".jpg";
                             System.out.println(file_name);    
                        }
                        else if(q_no == question_no){
                           file_name = "wait.png";
                        }
                        else{
                            response.sendError(HttpServletResponse.SC_NOT_FOUND); 
                        }
                         System.out.println("anhcjdc4e3849");
                            File f = new File("/Users/hemankurmohar/NetBeansProjects/ignite_th/private_data/"+file_name);
                            BufferedImage bi = ImageIO.read(f);
                            OutputStream out = response.getOutputStream();
                            ImageIO.write(bi, "jpg", out);
                            out.close();
                    }
                }
                catch(Exception e){
                    
                }
                
    	    }
	 	
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet assets</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet assets at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
