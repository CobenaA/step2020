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

/* Fetches stats from the servers and adds them to the DOM. */
function getDataJSON() {

    fetch('/data').then(response => response.json()).then((comments) => {
        console.log(comments);
        const commentsEl = document.getElementById('history');
        comments.forEach((comment) => {
            commentsEl.appendChild(createCommentElement(comment));
        })
    });
}

/* Creates an <li> element containing text. */
function createCommentElement(comment) {

    const commentEl = document.createElement('li');
    commentEl.className = 'comment container';

    const textElement = document.createElement('span');
    textElement.innerText = comment.text + "\n";

    const userElement = document.createElement('span');
    userElement.innerText = comment.name + "\t" + comment.time + "\n";

    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.innerText = 'Delete';
    deleteButtonElement.addEventListener('click', () => {
        deleteComment(comment);

        // Remove the task from the DOM.
        commentEl.remove();
    });

    commentEl.appendChild(userElement);
    commentEl.appendChild(textElement);
    commentEl.appendChild(deleteButtonElement);
    return commentEl;
}

/* Tells the server to delete the comment. */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}

/* Checks if user is logged in */
async function checkLogin() {
    const response = await fetch('/user');
    const data = await response.json();
    var loginStatus = data.status;

    if(loginStatus == "true"){
        return true;
    }

    return false;
}


/* Change text for login/ logout button. */
function navBar(loggedIn){
    console.log("navBar() " + loggedIn)
    const accButton = document.getElementById('accountbutton');

    if(loggedIn){
        accButton.text = "Logout";
    } else {
        accButton.text = "Login";
    }
}

/* Runs all functions when loading page. */
async function onLoad() {
    var statusCheck =  await checkLogin();
    navBar(statusCheck);
    getDataJSON();
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/* Fetches vote data and uses it to create a chart. */
function drawChart() {
  fetch('/vote-data').then(response => response.json())
  .then((foodVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Food');
    data.addColumn('number', 'Votes');
    Object.keys(foodVotes).forEach((food) => {
      data.addRow([food, foodVotes[food]]);
    });

    const options = {
      'title': 'Favorite Foods',
      'width':600,
      'height':500
    };

    const chart = new google.visualization.ColumnChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}