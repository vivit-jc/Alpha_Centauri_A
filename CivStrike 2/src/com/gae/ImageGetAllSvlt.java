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
//import images.image;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
//import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
//import org.json.simple.JSONObject; 

@SuppressWarnings("serial")
public class ImageGetAllSvlt extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();		
    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    												throws IOException {
    	resp.setContentType("text/plain");
    	resp.setCharacterEncoding("utf-8");
    	/*
    	String out = "";
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	Query query = pm.newQuery(com.gae.Imagedat.class);
        try {
    		List<Imagedat> results = (List<Imagedat>)pm.newQuery(query).execute();
    		if (results.iterator().hasNext()) {    			
    			for (Imagedat img : results) {
    		   		out += img.getGuestname()+"<i>";
    		   		out += img.getEmail()+"<i>";
    		   		out += img.getFiletitle()+"<i>";
    		   		out += img.getComment()+"<i>";
    		   		out += img.getFilename()+"<i>";
    		   		//out += img.getContent().getBytes()+"<i>";   		   		
    		   		out += img.getDate()+"<r>";	
    		   	}
    		   	resp.getWriter().println(out);    			
    	   } else {
    	   	  resp.getWriter().println("NO:参照データなし"); 
    	   }
    	} finally {
    	   pm.close();
    	}
    	*/
    	
    	//String out = "[";
    	String out = "";
        Query query = new Query("imagestore"); //（1）
        List<Entity> entity = ds.prepare(query).asList(FetchOptions.Builder.withOffset(0));
        for (Entity e : entity) {
          String skey = e.getKey().toString();
          out += skey + "<p>";
          String guestname = (String) e.getProperty("guestname");
          out += guestname + "<p>";
          String email = (String) e.getProperty("email");
          out += email + "<p>";
          String comment = (String) e.getProperty("comment");
          out += comment + "<p>";
          String image = (String) e.getProperty("image");
          out += image + "<e>";
          //moddate
          //String moddate = (String) e.getProperty("moddate");
          //out += moddate + "<e>";
          //String os = (String) o.getProperty("os");
          //String memory = (String) o.getProperty("memory");
          //String price = o.getProperty("price").toString();
          //String orddate = o.getProperty("orddate").toString();
          //out += "{\"skey\": \"" + skey + "\", \"guestname\": \"" + guestname + "\", \"email\": \"" + email + "\", \"comment\": \"" + comment + "\"},";
     
        }
        //out += "]";
        resp.getWriter().println(out.substring(0, out.length()-3));
    	
    	/*
    	JSONObject jsonobj=new JSONObject();
    	Query query = new Query("imagestore"); //（1）
        List<Entity> entity = ds.prepare(query).asList(FetchOptions.Builder.withOffset(0));
        int i = 0;
        for (Entity e : entity) {
        	 String skey = e.getKey().toString();
        	 jsonobj.put("skey", skey);
             String guestname = (String) e.getProperty("guestname");
             jsonobj.put("guestname", guestname);
             String email = (String) e.getProperty("email");
             jsonobj.put("email", email);
             String comment = (String) e.getProperty("comment");
             jsonobj.put("comment", comment);        	
        }
        resp.getWriter().println(jsonobj);
        */    	
    }	
}

