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
//import com.google.appengine.api.channel.ChannelFailureException;
//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
//import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import javax.cache.Cache;
//import javax.cache.CacheException;
//import javax.cache.CacheFactory;
//import javax.cache.CacheManager;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.json.simple.JSONObject; 

@SuppressWarnings("serial")
public class UserAdminServlet extends HttpServlet {
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
											throws IOException { 
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		//PrintWriter out = resp.getWriter();
		String userid = req.getParameter("userid");
		String passwd = req.getParameter("passwd");
 		String rv = "";
        try{
            Entity useradmin = ds.get(KeyFactory.createKey("useradmin", userid));  	//（1）
            String passwd2 = useradmin.getProperty("passwd").toString();       		//（2）
            String uclass = useradmin.getProperty("uclass").toString(); 
            if (passwd.equals(passwd2)){
            	Entity userdirec = ds.get(KeyFactory.createKey("userdirec", uclass));  	//（1）
            	String udirec = userdirec.getProperty("udirec").toString();  
            	rv = "{\"status\": \"" + "OK" + "\", \"udirec\": \"" + udirec + "\"}";
            } else {
            	//rv = "ユーザ名またはパスワードが違っています。";
            	//rv = "{\"status\": \"" + "NO" + "\", \"reason\": \"" + "ユーザ名またはパスワードが違っています。" + "\"}";
            	rv = "{\"status\": \"" + "NO" + "\", \"comment\": \"ユーザ名またはパスワードが違っています。\"}";
            }
        }catch(EntityNotFoundException e){
                //rv = "Error = " + e;
        	rv = "{\"status\": \"" + "NO" + "\", \"comment\": \"" + e + "\"}";
        }catch(Exception e){
                //rv = "Error = " + e;
        	rv = "{\"status\": \"" + "NO" + "\", \"comment\": \"" + e + "\"}";
        }
        //out.println(rv);
        resp.getWriter().println(rv);
    }
}
