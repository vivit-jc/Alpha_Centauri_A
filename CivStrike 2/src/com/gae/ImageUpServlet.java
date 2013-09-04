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
//import com.gae.Imagedat;
//import com.gae.PMF;
//import com.gae.DirectBeans.ReturnValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
//import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

//import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
//import com.google.appengine.api.images.Transform;
//import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.datastore.Blob;

@SuppressWarnings("serial")
public class ImageUpServlet extends HttpServlet {
	/*
    String guestname = "";
    String email = "";
    String comment = "";
    String filetitle = "";
    Key  imgkey;
    String fileName = "";
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
														throws IOException {
    	resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		guestname = req.getParameter("guestname");
		email = req.getParameter("email");
		comment = req.getParameter("comment");
		filetitle = req.getParameter("filetitle");
		imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), filetitle);	
		resp.getWriter().println("OK:データセット "+filetitle);
    	
    } 
    */   
	
	String filetitle;
	String op;
	String kind;
	String skey;
	Key key;
	String image;
	Key imgkey;
	String[] id;
	String[] val;
	String filename = "";
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
														throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();

		op = req.getParameter("op");
		kind = req.getParameter("kind");
		skey = req.getParameter("skey");
		id = req.getParameter("id").split(",");
		val = req.getParameter("val").split("<p>");		
		//for(int i=0; i<id.length; i++ ){
		//	if(id[i].equals("filetitle")){
		//		//filetitle = val[i];
		//		imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), val[i]);	
		//	}
		//}
		//imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), filetitle);	
		out.println("データセット完了。");	
    }    		
	
	/*
    public class ReturnValue {
		public Entity entity;
		public String mdat;
	}	
    */
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
    	//DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	//ReturnValue value = new ReturnValue();
    	MemoryFileItemFactory factory = new MemoryFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	resp.setContentType("image/jpeg");
    	ServletOutputStream out = resp.getOutputStream();
    	try {
    		List<FileItem> list = upload.parseRequest(req);
    		//FileItem list = upload.parseRequest(req);
    		for (FileItem item : list) {
    			if (!(item.isFormField())) {
    				filename = item.getName();
    				if (filename != null && !"".equals(filename)) {    					
    					int size = (int) item.getSize();
    					byte[] data = new byte[size];
    					InputStream in = item.getInputStream();	
    					in.read(data);
    					ImagesService imagesService = ImagesServiceFactory.getImagesService();
    					Image newImage = ImagesServiceFactory.makeImage(data);
    					byte[] newImageData = newImage.getImageData();
    					
    					//imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), filename.split(".")[0]);	
    					//imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), filename.split(".")[0]);	
    					
    					out.write(newImageData);
    					out.flush();      
    
    					DatastoreService ds = DatastoreServiceFactory.getDatastoreService();		
    					Key key = KeyFactory.createKey(kind, skey);    					
    					Blob blobImage = new Blob(newImageData);    					    					
    					DirectBeans_textjson dbeans = new DirectBeans_textjson();
    					/*  イメージとDate以外のウップ項目をセット     */
    					//Entity entity = dbeans.setentity("add", kind, true, key, id, val);
    					
    					//ReturnValue value = dbeans.Called.setentity("add", kind, true, key, id, val);
    					//Entity entity = value.entity;
    					//DirectBeans.ReturnValue value = new DirectBeans.ReturnValue();
    					DirectBeans_textjson.entityVal eval = dbeans.setentity("add", kind, true, key, id, val);
    					Entity entity = eval.entity;
    					
    					/*  イメージとDateを追加セット                         */
    					//for(int i=0; i<id.length; i++ ){
    					//	if(id[i].equals("image")){
    					//		//filetitle = val[i];
    					//		//imgkey = KeyFactory.createKey(Imagedat.class.getSimpleName(), val[i]);	
    					//	}
    					//} 					
    					
    					entity.setProperty("image",blobImage);    					
    					Date date = new Date();
    					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日:HH時mm分ss秒");
    					sdf.setTimeZone(TimeZone.getTimeZone("JST"));	
    					entity.setProperty("moddate",sdf.format(date));
    					//DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    					ds.put(entity);
    					out.println("エンティティ登録成功 KEY:"+key);          
    				}
    			}
    		}
    	} catch (FileUploadException e) {
    		e.printStackTrace();
    	} finally {
    		if (out != null) {
    			out.close();
    		}
    	}    	
    }	    
    
    /*
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    													throws IOException {
       	MemoryFileItemFactory factory = new MemoryFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	resp.setContentType("image/jpeg");
    	ServletOutputStream out = resp.getOutputStream();
    	try {
    		List<FileItem> list = upload.parseRequest(req);
    		for (FileItem item : list) {
    			if (!(item.isFormField())) {
    				fileName = item.getName();
    				if (fileName != null && !"".equals(fileName)) {
    					int size = (int) item.getSize();
    					byte[] data = new byte[size];
    					InputStream in = item.getInputStream();
    					in.read(data);
    					ImagesService imagesService = ImagesServiceFactory.getImagesService();
    					
    					//Image oldImage = ImagesServiceFactory.makeImage(data);
    					//Transform resize = ImagesServiceFactory.makeResize(900, 150);
    					//Image newImage = imagesService.applyTransform(resize, oldImage, OutputEncoding.JPEG);
    					//byte[] newImageData = newImage.getImageData();
    					
    					//
    					Image newImage = ImagesServiceFactory.makeImage(data);
    					byte[] newImageData = newImage.getImageData();
    					//
    					out.write(newImageData);
    					out.flush();                        
                                                
    					Blob blobImage = new Blob(newImageData);
    					Date date = new Date();
    					Imagedat img = new Imagedat(imgkey,
    												guestname, 
    												email, 
    												filetitle,
    												comment,
    												fileName,
    												blobImage, 
    												date);
    					PersistenceManager pm = PMF.get().getPersistenceManager();
    					try {
    						pm.makePersistent(img);
    					} catch (Exception e) {
    						e.printStackTrace();                        
    					} finally {
    						pm.close();
    					}                        
    				}
    			}
    		}
    	} catch (FileUploadException e) {
    		e.printStackTrace();
    	} finally {
    		if (out != null) {
    			out.close();
    		}
    	}    	
    }
    */
      
}
