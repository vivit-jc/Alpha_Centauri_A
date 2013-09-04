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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
//import java.util.*;
//import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import org.json.simple.JSONObject; 

public class JsonBeans {
	Boolean putkvs = true;
	/*
	文字列		st
	バイト		by
	short		sh
	integer		in
	long		lo
	floating	fl
	double		do
	boolean		bo
	Text		te
	*/	
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	public String addDirect(String kind, String key, String[] id, String[] val) {
		Key ekey = null;
		try{
			ekey = KeyFactory.createKey(kind, key);			
			Entity imas = ds.get(ekey);
			return("重複エンティティ：登録できません");
		}catch(EntityNotFoundException e1){
			return doPut("add", ekey, kind, key, id, val);
		}		
	}
	
	public String revDirect(String kind, String key, String[] id) {
		String out = "";
		String prop[][] = getprop(id);		
		JSONObject jsonobj = new JSONObject();		
		boolean listp = false;
		try{
			Key itemkey = KeyFactory.createKey(kind, key);			
			Entity imas = ds.get(itemkey);
			String prop_st = "";
			Text prop_te = null;
			if(id.length == 1 && id[0].equals("*")){
				/* ***************************************** *
				 *                 全プロパティ検索
				 * ***************************************** */
				Map pmap = new HashMap();  
				pmap = imas.getProperties();
				Set keySet = pmap.keySet();  //すべてのキー値を取得  
				Iterator keyIte = keySet.iterator();  
				while(keyIte.hasNext()) {    //ループ。反復子iteratorによる　キー取得  
				   String ekey = keyIte.next().toString();  
				   String value = pmap.get(ekey).toString();    //キーよりvalueを取得
				   //out += ekey + "<k>" + value + "<p>";
				   jsonobj.put(ekey, value);
				}  
			}else{
				/* **************************************** *
				   *                          　　プロパティ指定で検索　
				 * **************************************** */
				for(int i = 0; i < id.length; i++){					
					String proplist = imas.getProperty(prop[i][1]).toString();
					if (proplist.indexOf(",")== -1){
						listp = false;
					}else{
						listp = true;
					}						
					if(prop[i][0].equals("st")){
						/* 文字列型     */
						if(imas.hasProperty(prop[i][1])){
							/*  プロパティ有り     */
							prop_st = imas.getProperty(prop[i][1]).toString();
							if (listp == false){	
								// 部分一致なし、Listプロパティでない
								out += prop_st+"<p>";
							} else {
								// 部分一致、Listプロパティ
								String prop_li = prop_st.replaceAll("[ \\[\\]]", "");
								out += prop_li + "<p>";
							}
						}					
					}else if(prop[i][0].equals("te")){
						if(imas.hasProperty(prop[i][1])){
							/*  プロパティ有り     */
							prop_te = (Text) imas.getProperty(prop[i][1]);
							out += prop_te.getValue() + "<p>";
						}
					}else{
						/*  その他のデータ型     */
						if(imas.hasProperty(prop[i][1])){
							/*  プロパティ有り     */
							prop_st = imas.getProperty(prop[i][1]).toString();
							if (prop_st.indexOf(",")== -1){
									// 部分一致なし、Listプロパティでない
									out += prop_st+"<p>";
							} else {
								// 部分一致、Listプロパティ
								String prop_li = prop_st.replaceAll("[ \\[\\]]", "");
								out += prop_li + "<p>";
							}
						}					
					}					
					/*  End  : Add on version1.1      */
				}					
			}
		}catch(EntityNotFoundException e){
			out = "NO:指定されたキーのエンティティは存在しません。";
		}
		return(out);
	}
		
	public String updDirect(String kind, String key, String[] id, String[] val) {
		Key ekey = null;
		try{
			ekey = KeyFactory.createKey(kind, key);			
			Entity imas = ds.get(ekey);	
			return doPut("upd", ekey, kind, key, id, val);
		}catch(EntityNotFoundException e2){
			return("更新対象エンティティが存在しません：　" + e2);			
		}
	}	
	
	public String delDirect(String kind, String key) {
		Key ekey = null;
		try{
			ekey = KeyFactory.createKey(kind, key);
			ds.delete(ekey);
			return("エンティティ削除成功");
		} catch(Exception e1){
			return("エンティティ削除不成功 := " + e1);
		}		
	}
	
	public String doPut(String mod, Key ekey, String kind, String key, String[] id, String[] val) {
		Entity entity = setentity(mod, ekey, kind, key, id, val);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日:HH時mm分ss秒");
		sdf.setTimeZone(TimeZone.getTimeZone("JST"));	
		entity.setProperty("moddate", date);
		ds.put(entity);
		if(mod.equals("add")){
			return("エンティティ登録成功 KEY:"+key);
		}else {
			return("エンティティ更新成功 KEY:"+key);
		}	
	}	
	
	public Entity setentity(String mod, Key ekey, String kind, String key, String[] id, String[] val) {
		try {
			String prop[][] = getprop(id);
			Entity entity = new Entity(ekey);
			for(int i = 0; i < id.length; i++){
				if(prop[i][0].equals("st")){
					if (val[i].indexOf(",")== -1){		// Not List property
						entity.setProperty(prop[i][1], val[i]);
					}else{								// List property
						entity.setProperty(prop[i][1], Arrays.asList(val[i].split(",")));
					}
				}else if(prop[i][0].equals("te")){		//long text
					Text val1 = new Text(val[i].trim());
					entity.setProperty(prop[i][1], val1);
				}else if(prop[i][0].equals("by")){		//byte
					byte val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						//Byte val1 = Byte.parseByte(val[i].trim());
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Byte.parseByte(val[i].trim());
						}
						entity.setProperty(prop[i][1], val1);
					}else{								//List property
						ArrayList<Byte> dlist = new ArrayList<Byte>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Byte.parseByte(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);		
					}
				}else if(prop[i][0].equals("sh")){		//short
					short val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						//Short val1 = Short.parseShort(val[i].trim());
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Short.parseShort(val[i].trim());
						}
						entity.setProperty(prop[i][1], val1);
					}else{								//List property
						ArrayList<Short> dlist = new ArrayList<Short>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Short.parseShort(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);		
					}
				}else if(prop[i][0].equals("in")){  	//integer
					int val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Integer.parseInt(val[i].trim());
						}
						entity.setProperty(prop[i][1], val1);
					}else{								//List property
						ArrayList<Integer> dlist = new ArrayList<Integer>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Integer.parseInt(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);								
					}	
				}else if(prop[i][0].equals("lo")){		//long
					long val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						//long val1 = Long.parseLong(val[i].trim());
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Long.parseLong(val[i].trim());
						}						
						entity.setProperty(prop[i][1], val1);							
					}else{								//List property
						ArrayList<Long> dlist = new ArrayList<Long>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Long.parseLong(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);			
					}
				}else if(prop[i][0].equals("fl")){		//float
					float val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						//float val1 = Float.parseFloat(val[i].trim());
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Float.parseFloat(val[i].trim());
						}						
						entity.setProperty(prop[i][1], val1);
					}else{								//List property
						ArrayList<Float> dlist = new ArrayList<Float>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Float.parseFloat(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);			
					}
				}else if(prop[i][0].equals("do")){		//double
					double val1;
					if (val[i].indexOf(",")== -1){		//Not List property
						//double val1 = Double.parseDouble(val[i].trim());
						if(val[i].trim().equals("na") || val[i].trim().equals("")){
							val1 = 0;
						}else{
							val1 = Double.parseDouble(val[i].trim());
						}		
						entity.setProperty(prop[i][1], val1);
					}else{								//List property
						ArrayList<Double> dlist = new ArrayList<Double>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Double.parseDouble(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);			
					}
				}else if(prop[i][0].equals("bo")){		//boolean
					if (val[i].indexOf(",")== -1){		//Not List property
						boolean val1 = Boolean.valueOf(val[i].trim()).booleanValue();
						entity.setProperty(prop[i][1], val1);
					}else{								//List Property
						ArrayList<Boolean> dlist = new ArrayList<Boolean>();
						String[] val2 = val[i].split(",");
						for(int j = 0; j < val2.length; j++){
							dlist.add(Boolean.parseBoolean(val2[j].trim()));								
						}
						entity.setProperty(prop[i][1], dlist);
					}	
				}

			}
			return entity;
		} catch(Exception e2){
			if(mod.equals("add")){
				//return("エンティティ登録不成功 := " + e2);
				//Entity entity_err = new Entity("エンティティ登録不成功 := " + e2);
				Entity entity_err = new Entity("");
				return entity_err;
			}else {
				//return("エンティティ更新不成功 := " + e2);
				//Entity entity_err = new Entity("エンティティ更新不成功 := " + e2);
				Entity entity_err = new Entity("");
				return entity_err;
			}
		}	
	}	
		
	public String[][] getprop(String[] id){	
		String[][] prop = new String[id.length][2];
		for(int i = 0; i < id.length; i++){
			if (id[i].indexOf(":") != -1) {
				// 一致有り: データ型指定有り
				prop[i][0] = id[i].substring(0,2).toLowerCase();
				prop[i][1] = id[i].substring(3);
			} else {
				// 一致なし：　データ型指定なし
				prop[i][0] = "st";
				prop[i][1] = id[i];
			}	
		}
		return prop;
	}	
}	
