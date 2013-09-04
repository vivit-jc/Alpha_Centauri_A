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
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import javax.jdo.PersistenceManager;

@SuppressWarnings("serial")
public class Upload extends HttpServlet  {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    //String blobname;
    //String blobskey;
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    								throws ServletException, IOException {

		String key = req.getParameter("key");
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobkey = blobs.get("blobkey");
        String blobskey = blobkey.getKeyString();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		blobinfo binfo = new blobinfo(key, blobskey);
		try {
			pm.makePersistent(binfo);						
			//res.sendRedirect("/upresp?result=ok&key="+key+"&blobskey="+blobskey);
			res.sendRedirect("/upresp?result=ok&key="+key);
		} catch(Exception e){
			res.sendRedirect("/upresp?result=no&error="+e);
		} finally {
			pm.close();										
		}        
    }
}
