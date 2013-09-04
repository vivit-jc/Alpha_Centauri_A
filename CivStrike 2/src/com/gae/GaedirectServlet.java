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
import java.io.PrintWriter;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class GaedirectServlet extends HttpServlet {
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
													throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		String op = req.getParameter("op");
		String rv = req.getParameter("rv");
		String kind = req.getParameter("kind");
		String key = req.getParameter("key");
		String[] id = {};
		String[] val = {};
		if(!op.equals("del")){
			id = req.getParameter("id").split(",");
			val = req.getParameter("val").split("<p>");
		}
		//DirectBeans_textjson dbeans = new DirectBeans_textjson();
		DirectBeans dbeans = new DirectBeans();
		if(op.equals("add")){
			/*
			 *   CRUD 登録処理 
			 */
			out.println(dbeans.addDirect(kind, key, id, val, rv));	
		}else if(op.equals("upd")){
			/*
			 *   CRUD 更新処理
			 */
			out.println(dbeans.updDirect(kind, key, id, val, rv));			
			///////////////////////////
		}else if(op.equals("mod")){	
			out.println(dbeans.modDirect(kind, key, id, val, rv));	
		}else if(op.equals("del")){
			/*
			 *   CRUD 削除処理
			 */
			out.println(dbeans.delDirect(kind, key, rv));
		}
	}
	
	@Override	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
													throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		
		String rv = req.getParameter("rv");
		String kind = req.getParameter("kind");
		String key = req.getParameter("key");	
		String[] id = req.getParameter("id").split(",");
		
		//DirectBeans_textjson dbeans = new DirectBeans_textjson();
		DirectBeans dbeans = new DirectBeans();
		if(!req.getParameter("key").equals("none")){
			/*
			 *  CRUD 参照処理
			 */
			out.println(dbeans.revDirect(kind, key, id, rv));
		}else if(req.getParameter("key").equals("none")){
			/*
			 *   条件参照処理
			 */
			int offset = 0;
			int limit = 0;
			try{
				Query query = new Query(kind); 					//（１）	
				/*
				 * Start condition setting 
				 */
				if(req.getParameter("OFFSET").length() > 0){					
					offset = Integer.parseInt(req.getParameter("OFFSET"));	
				}
				if(req.getParameter("LIMIT").length() > 0){					
					limit = Integer.parseInt(req.getParameter("LIMIT"));	
				}				
				if(req.getParameter("SORT").indexOf(":") != -1){					
					String[] sort = req.getParameter("SORT").split(":");
					if(sort[1].equals("ASCENDING")){
						query.addSort(sort[0], SortDirection.ASCENDING);
					}else if(sort[1].equals("DESCENDING")){
						query.addSort(sort[0], SortDirection.DESCENDING);
					}					
				}
				if(req.getParameter("EQUAL").indexOf(":") != -1){
					String[] equal = req.getParameter("EQUAL").split(":");
					query.addFilter(equal[0], FilterOperator.EQUAL, equal[1]);
				}
				if(req.getParameter("NOT_EQUAL").indexOf(":") != -1){
					String[] equal = req.getParameter("NOT_EQUAL").split(":");
					query.addFilter(equal[0], FilterOperator.NOT_EQUAL, equal[1]);
				}
				if(req.getParameter("GREATER_THAN").indexOf(":") != -1){
					String[] parms = req.getParameter("GREATER_THAN").split(":");
					if(parms.length > 2){
						if(parms[0].equals("st")){
							String val2 = parms[2].trim();
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}else if(parms[0].equals("by")){
							byte[] val2 = parms[2].trim().getBytes("UTF-8");
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}else if(parms[0].equals("sh")){	
							short val2 = Short.parseShort(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}else if(parms[0].equals("in")){		
							int val2 = Integer.parseInt(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);						
						}else if(parms[0].equals("lo")){			
							long val2 = Long.parseLong(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}else if(parms[0].equals("fl")){				
							float val2 = Float.parseFloat(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}else if(parms[0].equals("do")){
							double val2 = Double.parseDouble(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN, val2);
						}	
					}else{
						query.addFilter(parms[0], FilterOperator.GREATER_THAN, parms[1]);
					}					
				}
				if(req.getParameter("GREATER_THAN_OR_EQUAL").indexOf(":") != -1){
					String[] parms = req.getParameter("GREATER_THAN_OR_EQUAL").split(":");
					if(parms.length > 2){
						if(parms[0].equals("st")){
							String val2 = parms[2].trim();
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("by")){
							byte[] val2 = parms[2].trim().getBytes("UTF-8");
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("sh")){	
							short val2 = Short.parseShort(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("in")){		
							int val2 = Integer.parseInt(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);						
						}else if(parms[0].equals("lo")){			
							long val2 = Long.parseLong(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("fl")){				
							float val2 = Float.parseFloat(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("do")){
							double val2 = Double.parseDouble(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.GREATER_THAN_OR_EQUAL, val2);
						}	
					}else{
						query.addFilter(parms[0], FilterOperator.GREATER_THAN_OR_EQUAL, parms[1]);
					}		
				}		
				if(req.getParameter("LESS_THAN").indexOf(":") != -1){
					String[] parms = req.getParameter("LESS_THAN").split(":");
					if(parms.length > 2){
						if(parms[0].equals("st")){
							String val2 = parms[2].trim();
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("by")){
							byte[] val2 = parms[2].trim().getBytes("UTF-8");
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("sh")){	
							short val2 = Short.parseShort(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("in")){		
							int val2 = Integer.parseInt(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);						
						}else if(parms[0].equals("lo")){			
							long val2 = Long.parseLong(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("fl")){				
							float val2 = Float.parseFloat(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("do")){
							double val2 = Double.parseDouble(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
						}else if(parms[0].equals("bo")){
							if(parms[2].toUpperCase().equals("TRUE")){
								boolean val2 = Boolean.parseBoolean("true"); 
							}else if(parms[2].toUpperCase().equals("FALSE")){
								boolean val2 = Boolean.parseBoolean("false"); 
							}
						}					
					}else{
						query.addFilter(parms[0], FilterOperator.LESS_THAN, parms[1]);
					}										
				}				
				if(req.getParameter("LESS_THAN_OR_EQUAL").indexOf(":") != -1){
					String[] parms = req.getParameter("LESS_THAN_OR_EQUAL").split(":");
					if(parms.length > 2){
						if(parms[0].equals("st")){
							String val2 = parms[2].trim();
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("by")){
							byte[] val2 = parms[2].trim().getBytes("UTF-8");
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("sh")){	
							short val2 = Short.parseShort(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("in")){		
							int val2 = Integer.parseInt(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);						
						}else if(parms[0].equals("lo")){			
							long val2 = Long.parseLong(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("fl")){				
							float val2 = Float.parseFloat(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("do")){
							double val2 = Double.parseDouble(parms[2].trim());
							query.addFilter(parms[1], FilterOperator.LESS_THAN_OR_EQUAL, val2);
						}else if(parms[0].equals("bo")){
							if(parms[2].toUpperCase().equals("TRUE")){
								boolean val2 = Boolean.parseBoolean("true"); 
							}else if(parms[2].toUpperCase().equals("FALSE")){
								boolean val2 = Boolean.parseBoolean("false"); 
							}
						}					
					}else{
						query.addFilter(parms[0], FilterOperator.LESS_THAN_OR_EQUAL, parms[1]);
					}
				}		
				if(req.getParameter("IN").indexOf(":") != -1){
					String[] prop = req.getParameter("IN").split(":");
					String[] args = prop[1].split(",");
					query.addFilter(prop[0], FilterOperator.IN, Arrays.asList(args));
				}				
				List<Entity> entities = null;
				if(limit <= 0){
					entities = ds.prepare(query).asList(FetchOptions.Builder.withOffset(offset));		
				}else {
					entities = ds.prepare(query).asList(FetchOptions.Builder.withLimit(limit).offset(offset));	
				}
				/////////////////////////////////////////////
				//      End condition setting
				//
				/////////////////////////////////////////////
				//      Start conditinal search
				//
				// Add for v30
				String outs = "";
				String jsonout = "{ \"entries\":[";
				for (Entity entity : entities) {	
					String skey = entity.getKey().toString();
					outs += skey + "<p>";
					// Add for v30
					String skey2 = skey.replace("\"", "'");
					jsonout += "{\"key\":\"" + skey2 + "\",";					
					String prop[][] = dbeans.getprop(id); 
					//jsonout += "{";
					for(int i = 0; i < id.length; i++){
						try{							
							if(entity.hasProperty(prop[i][1])){	
								if(prop[i][0].equals("st")){							//Data type String
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										String property = entity.getProperty(prop[i][1]).toString();
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1] + "\":\"" + property + "\",";										
										}else{
											outs += property + "<p>";
										}
									}else{			// List property										
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\""+ listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";
										}
									}
								}else if(prop[i][0].equals("te")){				//Data type Long text
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										Text property = (Text) entity.getProperty(prop[i][1]);
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() +  "\":\"" + property.getValue() + "\",";
										}else{
											outs += property.getValue() + "<p>";
										}
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";	
										}else{
											outs += property + "<p>";	
										}
									}
								}else if(prop[i][0].equals("by")){				//Data type Byte
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										Byte property = Byte.parseByte(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";	
										}
									}	
								}else if(prop[i][0].equals("sh")){				//Data type Short
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										Short property = Short.parseShort(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";	
										}
									}		
								}else if(prop[i][0].equals("in")){  			//Data type Integer
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){		//Not List property
										int property = Integer.parseInt(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";	
										}else{
											outs += property + "<p>";	
										}
									}									
								}else if(prop[i][0].equals("lo")){				//Data type Long
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}	
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";	
										}
									}			
								}else if(prop[i][0].equals("fl")){				//Data type Float
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										float property = Float.parseFloat(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}	
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";	
										}else{
											outs += property + "<p>";
										}
									}		
								}else if(prop[i][0].equals("do")){				//Data type Double
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										double property = Double.parseDouble(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}	
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";	
										}
									}			
								}else if(prop[i][0].equals("bo")){				//Data type Boolean
									if (entity.getProperty(prop[i][1]).toString().indexOf(",")== -1){	// Not list property
										boolean property = Boolean.valueOf(entity.getProperty(prop[i][1]).toString());
										if(rv.equals("json")){
											// Add for v30
											jsonout += "\"" + prop[i][1].toString() + "\":\"" + property + "\",";
										}else{
											outs += property + "<p>";
										}	
									}else{																// List property
										String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
										if(rv.equals("json")){
											// Add for v30
											String listpval = "";
											String[] listpstr = property.split(",");
											String jsonval = "[";
											for (int j = 0; j < listpstr.length; j++) {
												jsonval += "\"" + listpstr[j].toString().trim() + "\",";
											}
											jsonout += "\"" + prop[i][1].toString() + "\"" + ":" + jsonval.substring(0, jsonval.length()-1) + "],";											
										}else{
											outs += property + "<p>";					
										}
									}		
								}								
							}else{
								//???
								if(rv.equals("json")){
									
								}else{
									outs += "<p>";
								}	
							}
						}catch(Exception e){
							ArrayList<String> array = new ArrayList<String>();
							if(rv.equals("json")){
								
							}else{
								outs += array + "<p>";
							}	
						}						
					}
					// Add for v30
					jsonout = jsonout.substring(0, jsonout.length() - 1);
					jsonout +=  "},";
							
					//if(outs.length()>3){
					//	outs = outs.substring(0, outs.length()-3);
					//}
					outs += "<e>";
				}
				//if(outs.length() > 6){
					//out.println(outs.substring(0, outs.length()-6));
				//}else{
					//out.println(outs);
				//}
				if(rv.equals("json")){
					// Add for v30
					jsonout = jsonout.substring(0, jsonout.length() - 1);
					jsonout += "]}";
					out.println(jsonout);
				}else{
					if(outs.length()>6){
						//outs = outs.substring(0, outs.length()-3);
						outs = outs.substring(0, outs.length()-6);
					}
					out.println(outs);
				}
			}catch(Exception e){
				out.println("NO: Error = " + e);
			}
		}
	}
	
	public static Query setParms(Query query, String[] parms) throws Exception{
		if(parms.length > 2){
			if(parms[0].equals("st")){
				byte[] val2 = parms[2].trim().getBytes("UTF-8");
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}else if(parms[0].equals("by")){
				byte[] val2 = parms[2].trim().getBytes("UTF-8");
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}else if(parms[0].equals("sh")){	
				short val2 = Short.parseShort(parms[2].trim());
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}else if(parms[0].equals("in")){		
				int val2 = Integer.parseInt(parms[2].trim());
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);						
			}else if(parms[0].equals("lo")){			
				long val2 = Long.parseLong(parms[2].trim());
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}else if(parms[0].equals("fl")){				
				float val2 = Float.parseFloat(parms[2].trim());
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}else if(parms[0].equals("do")){
				double val2 = Double.parseDouble(parms[2].trim());
				query.addFilter(parms[1], FilterOperator.LESS_THAN, val2);
			}
		}else{
			query.addFilter(parms[0], FilterOperator.LESS_THAN, parms[1]);
		}
		return query;
	}	
	
	public String getJson(Entity entity, String[][] prop, int i) {
		String property = entity.getProperty(prop[i][1]).toString();
		//outs += property + "<p>";
		// Add for v30
		//jsonout += "\"" + prop[i][1] + "\":\"" + property + "\",";
		return "\"" + prop[i][1] + "\":\"" + property + "\",";
	}
	
	public String getJsonList(Entity entity, String[][] prop, int i) {
		String property = entity.getProperty(prop[i][1]).toString().replaceAll("[ \\[\\]]", "");
		//outs += property + "<p>";
		// Add for v30
      	String listpval = "";
       	String[] listpstr = property.split(",");
       	String jsonval = "[";
       	for (int j = 0; j < listpstr.length; j++) {
       	    jsonval += "\""+ listpstr[j].toString().trim() + "\",";
       	}
       	//jsonout += "\"" + prop[i][1] + "\":\"" + jsonval.substring(0, jsonval.length()-1) + "],";		
		//return jsonout;
      	return "\"" + prop[i][1] + "\":\"" + jsonval.substring(0, jsonval.length()-1) + "],";			
	}
}
