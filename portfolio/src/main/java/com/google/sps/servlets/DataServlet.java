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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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

    private final ArrayList<String> history = new ArrayList<String>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        // Convert to JSON
        String jsonStr = "{";
        for(int i = 0; i < history.size(); i++){
            jsonStr+= "\"m" + i + "\" :";
            jsonStr += "\"" + history.get(i) + "\"";
            if(i != (history.size() - 1)){
                jsonStr += ",";
            }   
        }
        jsonStr += "}";
        response.getWriter().println(jsonStr);
    } 

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get the input from the form.
        String text = request.getParameter("text-input");
        history.add(text);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");

        long timestamp = System.currentTimeMillis();

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("timestamp", timestamp);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        response.sendRedirect("/index.html");
    }
}
