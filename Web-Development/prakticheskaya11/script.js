"use strict";

const form = document.contact;
const submitBtn = document.contact.submit;
submitBtn.disabled = true;
$(form).on("input", onInput);
$(form).on("submit", (e) => onSubmit(e));

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


const questions = $(".question");
const answers = $(".answer");

for (const question of questions) {
  $(question).on("click", (e) => showAnswer(e));
}

function showAnswer(e) {
  const number = e.target.dataset.number;
  const answer = $(e.target)
  if (answer.hasClass("closed")) {
    answer.removeClass("closed");
    answer.addClass("opened")
  } else {
    answer.removeClass("opened");
    answer.addClass("closed")
  }
  $(answers[number]).toggleClass("hidden");
}


let sprite = $(".animation-container");
sprite = $(sprite);
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
  sprite.css("backgroundPosition", `right ${x}px bottom ${y}px`);
}