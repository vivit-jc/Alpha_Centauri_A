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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOObjectNotFoundException;

@SuppressWarnings("serial")
public class Show extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    public void doGet(HttpServletRequest req, HttpServletResponse res)
    												throws IOException {
        //BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        //blobstoreService.serve(blobKey, res);
    	    	
		//res.setContentType("text/plain");
		//res.setCharacterEncoding("utf-8");
		String key = req.getParameter("key");
		PersistenceManager pm = PMF.get().getPersistenceManager();			//
		try {			
			blobinfo binfo = pm.getObjectById(blobinfo.class, key);	//
			String blobskey = binfo.getBlobskey();
			BlobKey blobkey = new BlobKey(blobskey);
	    	blobstoreService.serve(blobkey, res);
		}catch(JDOObjectNotFoundException e){
			res.getWriter().println("NO<i>:インスタンス不存在　：" + e);
		}catch(Exception e){
			res.getWriter().println("NO<i>:参照不成功　エラー：" + e);	
		} finally {
			pm.close();
		}
    }
}


