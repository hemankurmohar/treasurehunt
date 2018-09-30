/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.profile_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author hemankurmohar
 */
public class Database {
  
    public Connection get_connection(){
         try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ignite","root","hemankur#9041");
            return con;
         }catch(Exception e){
            
        }
        return null;
    }
    
    void update_hints(){
        Connection con = this.get_connection();
        try{
              Statement st = con.createStatement();
              st.executeUpdate("update user_score U set hint_no = hint_no+1,start_time=NOW() where TIMESTAMPDIFF(MINUTE,U.start_time,NOW()) > 5 and U.hint_no < 4;");   
              con.close();
        }catch(Exception e){
            
        }
    }
    public static void main(String args[]){
        
        Database d  = new Database();
        d.update_hints();
    }
    
}
