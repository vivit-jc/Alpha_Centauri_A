/*!
* gaedirect v3.2.0
* *
* Copyright 2012, Katsuyuki Seino
* Licensed under the GPL Version 2 licenses.
* http://jquery.org/license
*
* Date: Mon May 29 2012
*/
package com.gae;
import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;												//（１）
import com.google.appengine.api.users.UserService;										//（１）
import com.google.appengine.api.users.UserServiceFactory;								//（１）

public class UserServiceServlet extends HttpServlet {
   public void doGet(HttpServletRequest req, HttpServletResponse resp)
		  													throws IOException {
	  UserService userService = UserServiceFactory.getUserService();					
      User user = userService.getCurrentUser();    
      if (user != null) {
         resp.setContentType("text/html; charset=utf-8");
         //resp.getWriter().println("サインイン認証されました。　" + user.getNickname());			
         //resp.getWriter().println("<a href='http://news.google.com/'>今日のニュース</a>");
         try{
    	     DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
             Entity ent = ds.get(KeyFactory.createKey("signurl", "signkey"));
             String signdirec = ent.getProperty("signdirec").toString();
             resp.getWriter().println("サインイン認証されました。　" + user.getNickname());		
             resp.getWriter().println("<a href="+signdirec+">メニュー表示</a>");
            // resp.getWriter().println("<a href='http://schwarzschild-gaedirect30.swsgaejpgm10.appspot.com/user1-aghbc/0.0-signin.htm'>メニュー表示</a>");
             //rv = "{\"status\": \"参照成功 \"}";
         }catch(EntityNotFoundException e){
    	     //resp.getWriter().println("{\"status\": \"参照不成功: " + e.toString() + "\"}");
    	     resp.getWriter().println("参照不成功: " + e.toString());
         }catch(Exception e){
             //rv = "Error = " + e;
    	    //resp.getWriter().println("{\"status\": \"参照不成功: " + e.toString() + "\"}");
    	    resp.getWriter().println("参照不成功: " + e.toString());
         }  
      } else {
        resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));			
      }
   }
}
