"use strict";

const form = document.contact;
const submitBtn = document.contact.submit;
submitBtn.disabled = true;
form.addEventListener("input", onInput);
form.addEventListener("submit", (e) => onSubmit(e));

function onInput() {
  if (form.firstName.value.trim() !== ""
    && form.email.value.trim() !== ""
    && form.message.value.trim() !== "") {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
}

function onSubmit(e) {
  e.preventDefault();
  const data = {};
  for (const el of form.elements) {
    if (el.name !== "submit") {
      data[el.name] = el.value;
    }
    
  }
  console.log(data);
}


const questions = document.querySelectorAll(".question");
const answers = document.querySelectorAll(".answer");

for (const question of questions) {
  question.addEventListener("click", (e) => showAnswer(e));
}

function showAnswer(e) {
  const number = e.target.dataset.number;
  if (e.target.classList.contains("closed")) {
    e.target.classList.remove("closed");
    e.target.classList.add("opened")
  } else {
    e.target.classList.remove("opened");
    e.target.classList.add("closed")
  }
  
  answers[number].classList.toggle("hidden");
}


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