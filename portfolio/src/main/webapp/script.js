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


    fetch('/data').then(response => response.json()).then((msgs) => {
    // stats is an object, not a string, so we have to
    // reference its fields to create HTML content
    console.log(msgs);
    const statsListElement = document.getElementById('data-container');
    statsListElement.innerHTML = '';
    statsListElement.appendChild(
        createListElement('one: ' + msgs.one));
    statsListElement.appendChild(
        createListElement('two: ' + msgs.two));
    statsListElement.appendChild(
        createListElement('three: ' + msgs.three));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}