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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class DirectBeans_textjson {
	Boolean putkvs = true;
	/*
	文字列		st
	バイト			by
	short		sh
	integer		in
	long		lo
	floating	fl
	double		do
	boolean		bo
	Text		te
	*/		
	//DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	DatastoreService ds = null;
	//MemcacheService mcache = null;
	MemcacheService syncCache = null;
	AsyncMemcacheService asyncCache = null;
	public DirectBeans_textjson(){
		ds = DatastoreServiceFactory.getDatastoreService();
		//Add for 3.0
		syncCache = MemcacheServiceFactory.getMemcacheService();
		//asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	}	
	
	public String addDirect(String kind, String skey, String[] id, String[] val, String rv) {
		Key key = null;
		if(!skey.equals("")){
			try{
				key = KeyFactory.createKey(kind, skey);			
				Entity imas = ds.get(key);
				//return("重複エンティティ：登録できません");
				//return("{\"status\": \"重複キーで登録不可  Key: " + key.toString() + "\"}");	
				if(rv.equals("json")){
					return("{\"status\": \"NO 重複キーで登録不可  Key: " + skey + "\"}");
				}else{
					return("重複エンティティ：登録できません");
				}
			}catch(EntityNotFoundException e1){
				return doPut("add", kind, true, key, skey, id, val, rv);
			}
		}else{
			return doPut("add", kind, false, key, skey, id, val, rv);
		}	
	}
	
	public String revDirect(String kind, String key, String[] id, String rv) {
		String outs = "";	
		// Add v30 120419
		String jsonout = "{";	
		//
		//  for memcache
		//
		Map memdat = null;  
		if(rv.equals("json")){
			memdat = (Map) syncCache.get(key);
		}	
		if(rv.equals("json") && memdat != null){			
			//////////////////////////////////////////////////
			//      Memcache data exist use memcache data
			//
			// Start add 3.o
			String prop[][] = getprop(id);
			int pcnt = 0;
			// String propi0 = prop[1][0];
			// End add 3.0
			Iterator ite = memdat.keySet().iterator();
			while (ite.hasNext()) {
				Object propname = ite.next();
				String propval =  memdat.get(propname).toString();
				if(propval.indexOf('[') == 0){   
					// List property
					String listpval = "";
					listpval = propval.replaceAll("[ \\[\\]]", "");			        			        	
					String[] listpstr = listpval.split(",");
					String jsonval = "[";
					for (int i = 0; i < listpstr.length; i++) {
						jsonval += "\""+ listpstr[i].toString().trim() + "\",";
					}
					jsonout += "\"" + propname.toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";		        	
				}else{				
					// Not list property
					if(prop[pcnt][0].equals("te")){
						// Data type of property is text 
						Text prop_te = new Text(propval);
						jsonout += "\"" + propname.toString() + "\"" + ":\"" + prop_te + "\",";
					}else{
						// Data type of property is not Text
						jsonout += "\"" + propname.toString() + "\"" + ":\"" + propval.toString() + "\",";
					}	
				}
				++pcnt;
			}
		    jsonout += "\"status\"" + ":\"OK Cache>参照成功   キー: " + key + "\"}";
		}else{		
			//////////////////////////////////////////////////
			//     Memcache data not exist use bigtable
			//
			String prop[][] = getprop(id);
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
					if(rv.equals("json")){
						while(keyIte.hasNext()) {    //ループ、反復子iteratorによる　キー取得  
							String ekey = keyIte.next().toString();  
							String value = pmap.get(ekey).toString();    //キーよりvalueを取得						
							if (value.indexOf(",")== -1){				//Listプロパティでない	
								jsonout += "\"" + ekey + "\"" + ":\"" + value + "\",";
							}else{										//Listプロパティ
								String value2 = value.replaceAll("[ \\[\\]]", "");	
								String[] listpstr = value2.split(",");
								String jsonval = "[";
								for (int j = 0; j < listpstr.length; j++) {
									jsonval += "\""+ listpstr[j] + "\",";
								}
								jsonout += "\"" + ekey + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";
							}
						}
					}else{
						while(keyIte.hasNext()) {    //ループ。反復子iteratorによる　キー取得  
						   String ekey = keyIte.next().toString();  
						   String value = pmap.get(ekey).toString();    //キーよりvalueを取得
						   outs += ekey + "<k>" + value + "<p>";
						}
					}  
				}else{
					/* **************************************** *
					   *                          　　プロパティ指定で検索　
					 * **************************************** */
					for(int i = 0; i < id.length; i++){	
						if(imas.hasProperty(prop[i][1])){
							String proplist = imas.getProperty(prop[i][1]).toString();
							if (proplist.indexOf(",")== -1){
								listp = false;
							}else{
								listp = true;
							}
						}	
						if(prop[i][0].equals("st")){
							/* 文字列型     */
							if(imas.hasProperty(prop[i][1])){
								/*  プロパティ有り     */
								prop_st = imas.getProperty(prop[i][1]).toString();
								if (listp == false){	// 部分一致なし、Listプロパティでない									
									//out += prop_st+"<p>";
									// Add 30  
									if(rv.equals("json")){
										jsonout += "\"" + prop[i][1] + "\""+ ":\"" + prop_st + "\",";
									} else {
										// 部分一致なし、Listプロパティでない
										outs += prop_st+"<p>";
									}									
								} else {	// 部分一致、Listプロパティ									
									String prop_li = prop_st.replaceAll("[ \\[\\]]", "");	
									if(rv.equals("json")){
										String[] listpstr = prop_li.split(",");
										String jsonval = "[";
										for (int j = 0; j < listpstr.length; j++) {
											jsonval += "\""+ listpstr[j] + "\",";
										}
										jsonout += "\"" + prop[i][1] + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";
									}else{
										outs += prop_li + "<p>";
									}
								}
							}else{
								if(rv.equals("json")){
									jsonout += "\"" + prop[i][1] + "\""+ ":\"NA\",";
								}else{
									outs += "NA"+"<p>";
								}
							}
						}else if(prop[i][0].equals("te")){
							if(imas.hasProperty(prop[i][1])){
								/*  プロパティ有り     */
								prop_te = (Text) imas.getProperty(prop[i][1]);
								if(rv.equals("json")){
									// ???
									jsonout += "\"" + prop[i][1] + "\"" + ":\"" + prop_te.getValue() + "\",";
								}else{	
									// ???
									outs += prop_te.getValue() + "<p>";
								}	
							}else{
								Text NA = new Text("NA");
								if(rv.equals("json")){
									jsonout += "\"" + prop[i][1] + "\""+ ":\"NA\",";
								}else{
									outs += NA +"<p>";
								}
							}
						}else{
							/*  その他のデータ型     */
							if(imas.hasProperty(prop[i][1])){
								/*  プロパティ有り     */
								prop_st = imas.getProperty(prop[i][1]).toString();
								if (prop_st.indexOf(",")== -1){		// 部分一致なし、Listプロパティでない
									//out += prop_st+"<p>";
									if(rv.equals("json")){
										jsonout += "\"" + prop[i][1] + "\"" + ":\"" + prop_st + "\",";
									}else{
										outs += prop_st+"<p>";
									}
								} else {			// 部分一致、Listプロパティ									
									String prop_li = prop_st.replaceAll("[ \\[\\]]", "");
									if(rv.equals("json")){
										String[] listpstr = prop_li.split(",");
										String jsonval = "[";
										for (int j = 0; j < listpstr.length; j++) {
											jsonval += "\""+ listpstr[j] + "\",";
										}
										jsonout += "\"" + prop[i][1] + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";
									}else{
										outs += prop_li + "<p>";
									}	
								}
							}else{
								if(rv.equals("json")){
									jsonout += "\"" + prop[i][1] + "\"" + ":\"NA\",";
								}else{
									outs += "NA" +"<p>";
								}		
							}					
						}					
					}					
				}
				if(rv.equals("json")){
					jsonout += "\"status\"" + ":\"OK Bigtable>参照成功   キー: " + key + "\"}";
				}else{
					outs += "OK: Bigtable>参照成功 キー：　" + key + "<p>";
				}
			}catch(EntityNotFoundException e){
				if(rv.equals("json")){
					jsonout = "{\"status\": \"NO 指定されたキーのエンティティは存在しません。\"}";
				}else{
					outs = "NO:指定されたキーのエンティティは存在しません。";
				}
			}
		}
		if(rv.equals("json")){
			return(jsonout);
		}else{
			return(outs);
		}
	}
		
	public String updDirect(String kind, String skey, String[] id, String[] val, String rv) {
		Key key = null;
		try{
			key = KeyFactory.createKey(kind, skey);			
			Entity imas = ds.get(key);	
			return doPut("upd", kind, true, key, skey, id, val, rv);
		}catch(EntityNotFoundException e2){
			if(rv.equals("json")){
				return("{\"status\": \"NO 更新対象エンティティが存在しません： " + e2.toString() + "\"}");
			}else{
				return("更新対象エンティティが存在しません：　" + e2);	
			}
		}
	}	
	
	public String modDirect(String kind, String skey, String[] id, String[] val, String rv) {
		Key key = null;
		try{
			key = KeyFactory.createKey(kind, skey);			
			Entity imas = ds.get(key);	
			return doPut("upd", kind, true, key, skey, id, val, rv);
		}catch(Exception e2){
			return doPut("add", kind, true, key, skey, id, val, rv);
		}
	}	
	
	public String delDirect(String kind, String key, String rv) {
		Key ekey = null;
		try{
			ekey = KeyFactory.createKey(kind, key);
			ds.delete(ekey);
			
			// Start:Add for v30
			/*
			byte[] value = (byte[]) syncCache.get(ekey);
			if (value != null){
				syncCache.delete(ekey);
			}
			*/
			syncCache.delete(key);
			//　End: Add for v30
						
			if(rv.equals("json")){
				return("{\"status\": \"OK 削除成功 Key: " + key.toString() + "\"}");
			}else{
				return("エンティティ削除成功");
			}
		} catch(Exception e1){
			if(rv.equals("json")){
				return("{\"status\": \"NO 削除不成功:  " + e1.toString() + "\"}");
			}else{
				return("エンティティ削除不成功 := " + e1);
			}
		}		
	}	
	
	public String doPut(String mod, String kind, Boolean keyflag, Key key, String skey, String[] id, String[] val, String rv) {		
		try{
			entityVal entval = setentity(mod, kind, keyflag, key, id, val);
			syncCache.put(skey, entval.mdat);
			ds.put(entval.entity);
			if(mod.equals("add")){
				if(rv.equals("json")){
					return("{\"status\": \"OK 登録成功 Key: " + skey + "\"}");
				}else{
					return("登録成功 KEY:" + key);
				}
			} else if(mod.equals("upd")) {
				if(rv.equals("json")){
					return("{\"status\": \"OK 更新成功 Key: " + skey + "\"}");
				}else{
					return("更新成功 KEY:" + key);
				}
			} else{
				if(rv.equals("json")){
					return("{\"status\": \"NO 不正オペレーション Key: " + skey + "\"}");
				}else{
					return("不正オペレーション KEY:" + key);
				}
			}				
		}catch(Exception e){
			if(mod.equals("add")){
				if(rv.equals("json")){
					return("{\"status\": \"OK 登録不成功 : " + e.toString() + "\"}");
				}else{
					return("登録不成功 エラー:" + e);
				}
			} else if(mod.equals("upd")) {
				if(rv.equals("json")){
					return("{\"status\": \"NO 更新不成功 : " + e.toString() + "\"}");
				}else{
					return("更新不成功 エラー:" + e);
				}
			} else{
				if(rv.equals("json")){
					return("{\"status\": \"NO 不正オオペレーション: " + e.toString() + "\"}");
				}else{
					return("不正オペレーション エラー：" +e);
				}
			}
		}
	}	
	
	public class entityVal {
		public Entity entity;
		public Map mdat;
		//public String ecache;
	}	
	// Mod at 2011-10-19
	//public class Called {
	public entityVal setentity(String mod, String kind, Boolean keyflag, Key key, String[] id, String[] val) {
		entityVal entval = new entityVal();
		Map emap = new HashMap();
		try {			
			String prop[][] = getprop(id);
			// Start mod at 2011-10-19
			//Entity entity = new Entity(key);
			Entity entity;
			if(keyflag){
				entity = new Entity(key);
				///////////////////////////////
				//entity = new Entity(kind, key);
				///////////////////////////////
			}else{
				entity = new Entity(kind);
			}
			// End mod at 2011-10-19
			for(int i = 0; i < id.length; i++){
				if(prop[i][0].equals("st")){
					if (val[i].indexOf(",")== -1){		//Not List property
						entity.setProperty(prop[i][1], val[i]);
					}else{								//List property
						entity.setProperty(prop[i][1], Arrays.asList(val[i].split(",")));
					}
				}else if(prop[i][0].equals("te")){		//long text
					Text val1 = new Text(val[i].trim());
					entity.setProperty(prop[i][1], val1);
				}else if(prop[i][0].equals("by")){		//byte
					byte val1;
					if (val[i].indexOf(",")== -1){		//Not List property
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
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日:HH時mm分ss秒");
			sdf.setTimeZone(TimeZone.getTimeZone("JST"));	
			entity.setProperty("moddate", date);
			entval.entity = entity;
			emap =  entity.getProperties();
			entval.mdat = emap;	
			//return eval;
		} catch(Exception e2){
			if(mod.equals("add")){
				Entity entity_err = new Entity("");
				//return entity_err;
				entval.entity = entity_err;
				emap.put(key.toString(),"");
				entval.mdat = emap;	
				//return eval;
			}else {
				Entity entity_err = new Entity("");
				//return entity_err;
				entval.entity = entity_err;
				emap.put(key.toString(),"");
				entval.mdat = emap;	
				//return eval;	
			}
		}	
		return entval;
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
