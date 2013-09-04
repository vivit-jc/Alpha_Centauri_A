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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class accountServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            									throws IOException {    	
    	resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");
        UserService userService = UserServiceFactory.getUserService();
        String page  = req.getParameter("page");
        String thisURL = req.getRequestURI();
        if (req.getUserPrincipal() != null) {
            resp.getWriter().println("<p>今日は " +
            				req.getUserPrincipal().getName() +
                            "!  ここから <a href=\"" +
                            //userService.createLogoutURL(thisURL) +
                            userService.createLogoutURL(page) +
                            "\">サイン　アウト</a>できます。</p>");
        } else {
            resp.getWriter().println("<p>ここから <a href=\"" +
                            //userService.createLoginURL(thisURL) +
                            //userService.createLoginURL("../0.0-index.htm") +
                            userService.createLoginURL(page) +
                            "\">サイン　イン</a>して下さい。</p>");
        }
    }
}

