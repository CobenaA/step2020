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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Create ArrayList and add three messages
    ArrayList<String> messages = new ArrayList<String>();
    messages.add("First message");
    messages.add("Second message");
    messages.add("Third message");

    //Convert the ArrayList to a json string
    String json = "{";
    json += "\"one\":";
    json += "\"" + messages.get(0) + "\"";
    json += ",";
    json += "\"two\":";
    json += "\"" + messages.get(1) + "\"";
    json += ",";
    json += "\"three\":";
    json += "\"" + messages.get(2) + "\"";
    json += "}";

    // response.setContentType("text/html;");
    // response.getWriter().println("<h2>Hello Ariel!</h2>");

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
