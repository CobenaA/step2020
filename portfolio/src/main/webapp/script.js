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

/*
 * Adds a random greeting to the page.
 */
function addRandomGame() {
  const games =
      ['Valorant', 'Civilization VI', 'Rocket League', 'Minecraft'];

  // Pick a random greeting.
  const game = games[Math.floor(Math.random() * games.length)];

  // Add it to the page.
  const gamesContainer = document.getElementById('games-container');
  gamesContainer.innerText = game;
}

/*
 * Fetch data from the data servlet
 */
async function getData() {
  const response = await fetch('/data');
  const data = await response.text();
  document.getElementById('data-container').innerText = data;
}

/**
 * Fetches stats from the servers and adds them to the DOM.
 */
function getDataJSON() {

    fetch('/data').then(response => response.json()).then((comments) => {
        console.log(comments);
        const commentsEl = document.getElementById('history');
        comments.forEach((comment) => {
            commentsEl.appendChild(createCommentElement(comment));
        })
    });
}



/** Creates an <li> element containing text. */
function createCommentElement(comment) {

    const commentEl = document.createElement('li');
    commentEl.className = 'comment container';

    const textElement = document.createElement('span');
    textElement.innerText = comment.text + "\n" + comment.time + "\n";

    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.innerText = 'Delete';
    deleteButtonElement.addEventListener('click', () => {
        deleteComment(comment);

        // Remove the task from the DOM.
        commentEl.remove();
    });

    commentEl.appendChild(textElement);
    commentEl.appendChild(deleteButtonElement);
    return commentEl;
}

/** Tells the server to delete the comment. */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}