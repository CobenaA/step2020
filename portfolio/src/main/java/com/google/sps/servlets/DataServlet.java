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
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList; 
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    // private final ArrayList<String> history = new ArrayList<String>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String amount = request.getParameter("show");
        System.out.println(amount);

        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<Comment> comments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            String text = (String) entity.getProperty("text");
            long timestamp = (long) entity.getProperty("timestamp");
            String time = (String) entity.getProperty("time");

            Comment comment = new Comment(id, text, timestamp, time);
            comments.add(comment);
        }
        
        Gson gson = new Gson();

        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(comments));

    } 

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get the input from the form.
        String text = request.getParameter("text-input");
        String name = request.getParameter("name");
        String showAmt = request.getParameter("show");
        System.out.println(showAmt);
        if(text.isEmpty()){
            response.sendRedirect("/?show=" + showAmt);
            return;
        }
        long timestamp = System.currentTimeMillis();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = myDateObj.format(myFormatObj);

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("timestamp", timestamp);
        commentEntity.setProperty("time", time);
        if(name.isEmpty()){
            commentEntity.setProperty("name", "Anonymous");
        } else {
            commentEntity.setProperty("name", name);
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/?show=" + showAmt);
    }
}
