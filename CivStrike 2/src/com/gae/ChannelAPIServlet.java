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
import com.google.appengine.api.channel.ChannelFailureException;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.channel.ChannelMessage;
import java.io.IOException;
//import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject; 

@SuppressWarnings("serial")
public class ChannelAPIServlet extends HttpServlet {
	Cache cache;
	private ChannelService channelService;
	
	@Override 
	public void init() throws ServletException{
		try {
			channelService = ChannelServiceFactory.getChannelService();
		}catch(ChannelFailureException e){
			throw new RuntimeException(e);
		}		
		try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
	}
	
	@Override 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
											throws IOException {
		resp.setCharacterEncoding("utf-8");
		String clientInfo  = req.getParameter("clientId");
		String clientId = "";
		int durationMinutes = 0;
		if(clientInfo.indexOf(":") != -1){
			clientId  = (String)clientInfo.split(":")[0];
			String dminutes1 = (String)clientInfo.split(":")[1];
			durationMinutes = Integer.parseInt(dminutes1.substring(0, dminutes1.length()-1));
		}else {
			clientId  = new String(clientInfo);
			durationMinutes = 0;
		}
		String action  = req.getParameter("action");
		if(action.equals("open")){
			String token = "";
			try{
				if(durationMinutes > 0){
					token = channelService.createChannel(clientId, durationMinutes);
				}else {
					token = channelService.createChannel(clientId);
				}
			}catch(ChannelFailureException e){
				throw new RuntimeException(e);
			}
			String clientList = "";
			try{
				byte[] clientList_b = (byte[])cache.get("uidKey");
				clientList = new String(clientList_b, "UTF-8");
			}catch(Exception e){}
			clientList += clientId + ",";
			byte[] clientList_bn = clientList.getBytes("UTF-8");
			cache.put("uidKey", clientList_bn);
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("utf-8");
			resp.getWriter().write(token);	
		}else if(action.equals("close")){
			byte[] clientList_b = (byte[])cache.get("uidKey");
			String clientList = new String(clientList_b, "UTF-8");
			String[] clientList_a = clientList.split(",");
			String clientList_n = "";
			for (int i = 0; i < clientList_a.length; i++) {
				if(!clientId.equals(clientList_a[i])){
					clientList_n += clientList_a[i] + ",";
				}
			}
			byte[] clientList_bn = clientList_n.getBytes("UTF-8");
			cache.put("uidKey", clientList_bn);
			resp.getWriter().write(clientList_n);
		}else if(action.equals("cinit")){
			String init = clientId + ",";
			byte [] init_b = init.getBytes("UTF-8");
			cache.put("uidKey", init_b);
			resp.getWriter().write("ユーザID（clientId）のキャッシュ削除しました。");
		}
	}

	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
											throws IOException { 
		String rv = req.getParameter("rv");
		String idb = req.getParameter("id");
		String valb = req.getParameter("val");
		String[] id = idb.split(",");
		String[] val = valb.split("<p>");
		Date date1 = new Date();
 		SimpleDateFormat df = new SimpleDateFormat("yyyy'年'MM'月'dd'日'kk'時'mm'分'ss'秒'");
		String date = df.format(date1);
		//Add to detastore
		if(!req.getParameter("kind").equals("none")){
			/*
			 *   CRUD 登録処理 
			 */
			DirectBeans_textjson dbeans = new DirectBeans_textjson();
			String kind = req.getParameter("kind");
			String key = req.getParameter("clientId");
			dbeans.addDirect(kind, key, id, val, "deli");	
		}		
		
		JSONObject jsonobj=new JSONObject();
		for(int i = 0; i < id.length; i++ ){
			// Add 2011-12-24  for Text data
			if(id[i].charAt(2)==':'){
				id[i] = id[i].substring(3);
			}
			// Add 2011-12-24  for Text data
			jsonobj.put(id[i], val[i]);
		}
		jsonobj.put("date", date);		
		byte[] clientList_b = (byte[])cache.get("uidKey");
		String clientList = new String(clientList_b, "UTF-8");
		String[] clientList_a = clientList.split(",");
		try{			
			for (int i = 0; i < clientList_a.length; i++) {
				channelService.sendMessage(new ChannelMessage(clientList_a[i], jsonobj.toString()));				
			}
		}catch(ChannelFailureException e){
			throw new RuntimeException(e);
		}
		String clients = "";
		for (int i = 0; i < clientList_a.length; i++) {
			clients += clientList_a[i] + ","; 
		}
		clients = clients.substring(0, clients.length()-1);
		resp.getWriter().write(clients);
    }
}
