"use strict";

console.log(document.forms);
console.log(document.links);

const links = document.querySelectorAll("a");
for (const link of links) {
  link.addEventListener("mouseover", (e) => onHover(e));
}

function onHover(e) {
  console.log("This link leads to: " + e.target.href);
}

// в html для формы также добавлен event onsubmit

const sprite = document.querySelector(".animation-container");
let x = 50;
let y = 50;
let xMax = 350;
let yMax = 100;
const step = 50;
const interval = setInterval(animate, 150);

function animate() {
  x = x + step;
  if (x > xMax) {
    if (y < yMax) {
      y = y + step;
      x = 50;
    } else {
      x = 50;
      y = 50;
    }
  }
  sprite.style.backgroundPosition = `right ${x}px bottom ${y}px`;
}