"use strict";

const input = prompt("Введите последовательность чисел.\nИспользуйте точку для вещественных чисел."
  + "\nВсе нечисловые символы будут проигнорированы.");
const nums = input.trim().split(/[^-\d\.]+/).filter(a => Number.isFinite(+a)).map(s => +s);
const sum = nums.reduce((total, curr) => total + curr, 0);
const mean = sum / nums.length;
const min = Math.min(...nums);
const max = Math.max(...nums);
document.write(
  "Введенные элементы: " + nums
  + "<br>Сумма всех элементов: " + sum.toFixed(2)
  + "<br>Среднее арифметическое всех элементов: " + mean.toFixed(2)
  + "<br>Минимальный элемент: " + min
  + "<br>Максимальный элемент: " + max
);