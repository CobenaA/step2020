// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList; 
import java.util.*;

/* Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    int showAmt = -1;
     
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // Check if user is logged in
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()){
            System.out.println("Logged In: " + userService.getCurrentUser().getEmail());
        } else {
            System.out.println("Not Logged In");
        }

        // Create a query for comments in Datastore 
        String amount = request.getParameter("show");
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        // Create ArrayList to store comments and then add them
        List<Comment> comments = new ArrayList<>();
        int counter = 0;
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            String text = (String) entity.getProperty("text");
            long timestamp = (long) entity.getProperty("timestamp");
            String time = (String) entity.getProperty("time");
            String name = (String) entity.getProperty("name");
            Comment comment = new Comment(id, text, timestamp, time, name);
            comments.add(comment);
            counter++;
            System.out.println(counter + " " + showAmt);
            if(counter == showAmt){
                break;
            }
        }
        
        // Convert the ArrayList into a json and return
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(comments));

    } 

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get the input from the form.
        String text = request.getParameter("text-input");
        if(request.getParameter("show").equals("All")){
            showAmt = -1;
        } else { 
            showAmt = Integer.parseInt(request.getParameter("show"));
        }

        if(text.isEmpty()){
            response.sendRedirect("/");
            return;
        }
        long timestamp = System.currentTimeMillis();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = myDateObj.format(myFormatObj);
        
        // Get name if user is logged in or use Anonymous
        String name;
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()){
            name = userService.getCurrentUser().getEmail();
        } else {
            name = "Anonymous";
        }

        // Add entity to Datastore 
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("timestamp", timestamp);
        commentEntity.setProperty("time", time);
        commentEntity.setProperty("name", name);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/?show=" + showAmt);
    }
}
