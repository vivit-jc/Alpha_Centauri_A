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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class upResp extends HttpServlet  {
    //private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();    
    public void doGet(HttpServletRequest req, HttpServletResponse res)
    								throws ServletException, IOException {
		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		//result=yes&blobname="+blobname
		String result = req.getParameter("result");		
		if(result.equals("ok")){
			String key = req.getParameter("key");
			//String blobskey = req.getParameter("blobskey");
			//res.getWriter().println("キー"+key+"  blobキー"+blobskey);
			res.getWriter().println("登録成功：　キー = "+key);
		}else{
			res.getWriter().println("エラー"+req.getParameter("error"));
		}
    }    
}
