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
//import com.gae.PMF;
import java.io.IOException;
import java.io.OutputStream;
//import java.util.List;
//import javax.jdo.Query;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
//import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Blob;

@SuppressWarnings("serial")
public class ImageGetServlet extends HttpServlet {
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    												throws IOException {
    	resp.setContentType("text/plain");
    	resp.setCharacterEncoding("utf-8");
    	OutputStream out = resp.getOutputStream();
    	String skey = req.getParameter("skey");
    	try {     		
     		//Key key = KeyFactory.createKey(Imagedat.class.getSimpleName(), skey);	
     		Key key = KeyFactory.createKey("imagestore", skey);	
     		Entity e = ds.get(key); 
     		Blob img = (Blob)e.getProperty("image");
			byte[] imgbyte = img.getBytes();
			out.write(imgbyte);
            out.flush();  
     		
     		/*
     		Imagedat idat = pm.getObjectById(Imagedat.class, key);	
    		//List<imagedat> results = (List<imagedat>)pm.newQuery(query).execute(filetitle);
    		if (idat != null) {    			
    			Blob imgout = idat.getContent();
    			byte[] imgout2 = imgout.getBytes();
    			out.write(imgout2);
                out.flush();  
             */       
        }catch(EntityNotFoundException e){
        	resp.getWriter().write("Error = " + e);
        }	
    }	
}

