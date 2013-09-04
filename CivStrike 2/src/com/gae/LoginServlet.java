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
//import com.google.appengine.api.channel.ChannelFailureException;
//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
//import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import javax.cache.Cache;
//import javax.cache.CacheException;
//import javax.cache.CacheFactory;
//import javax.cache.CacheManager;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.json.simple.JSONObject; 

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String thisURL = req.getRequestURI();
        resp.setContentType("text/html");
        UserService userService = UserServiceFactory.getUserService();
        PrintWriter pw = resp.getWriter();
        if (req.getUserPrincipal() == null) {
            pw.println("<a href=\"" + userService.createLoginURL(thisURL) + "\">Login</a>");
        } else {
            User user = userService.getCurrentUser();
            pw.println("<a href=\"" + userService.createLogoutURL(thisURL) + "\">Logout</a><br/>");
            pw.println("name = " + req.getUserPrincipal().getName() + "<br/>");
            pw.println("isAdmin = " + userService.isUserAdmin() + "<br/>");
            pw.println("domain = " + user.getAuthDomain() + "<br/>");
            pw.println("email = " + user.getEmail() + "<br/>");
            pw.println("nickname = " + user.getNickname() + "<br/>");
        }
    }
}