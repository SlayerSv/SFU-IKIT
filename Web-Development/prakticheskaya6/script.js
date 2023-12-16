"use strict";

const input = prompt("Введите последовательность чисел.\nИспользуйте точку для вещественных чисел."
  + "\nВсе нечисловые символы будут проигнорированы.") || "";
const nums = input.trim().split(/[^-\d\.]+/).filter(a => Number.isFinite(+a) && a !== "").map(s => +s);
if (!nums.length) {
  document.write("Неверная или пустая последовательность");
} else {
  const sum = calcSum(nums);
  const mean = calcMean(nums);
  const min = getMin(nums);
  const max = getMax(nums);
  document.write(
    "Введенные элементы: " + nums
    + "<br>Сумма всех элементов: " + sum.toFixed(2)
    + "<br>Среднее арифметическое всех элементов: " + mean.toFixed(2)
    + "<br>Минимальный элемент: " + min
    + "<br>Максимальный элемент: " + max
  );
}

function calcSum(arr) {
  let sum = 0;
  for (let i = 0; i < arr.length; i++) {
    sum += arr[i];
  }
  return sum;
}

function getMin(arr) {
  let min = arr[0];
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < min) {
      min = arr[i];
    }
  }
  return min;
}

function getMax(arr) {
  let max = arr[0];
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] > max) {
      max = arr[i];
    }
  }
  return max;
}

function calcMean(arr) {
  let sum = 0;
  for (let i = 0; i < arr.length; i++) {
    sum += arr[i];
  }
  return sum / arr.length;
}