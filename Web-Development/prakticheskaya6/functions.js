const testArray = [1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10, 10, 9, -8, -7, -6, 5, 5, 5, 5, 4, 3, 2, 1];

// 1) Найти сумму последовательности
function calcSum(arr) {
  if (!arr || !Array.isArray(arr)) return;
  let sum = 0;
  for (let i = 0; i < arr.length; i++) {
    sum += arr[i];
  }
  return sum;
  //return arr.reduce((total, curr) => total + curr, 0);
}
console.log(calcSum(testArray));

// 2) Найти минимальный элемент в последовательности.
function getMin(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let min = arr[0];
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < min) {
      min = arr[i];
    }
  }
  return min;
  //return Math.min(...arr);
}
console.log(getMin(testArray));

// 3) Найти второй по величине элемент в последовательности
function getSecondSmallest(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 2) return;
  let first = Infinity;
  let second = Infinity;
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < first) {
      second = first;
      first = arr[i]
    } else if (arr[i] < second) {
      second = arr[i];
    }
  }
  return second;
  //return arr.slice().sort((a, b) => a - b)[1];
}
console.log(getSecondSmallest(testArray));

// 4) Сколько раз в последовательности встречается заданное число?
function countOccurance(arr, target) {
  if (!arr || !Array.isArray(arr)) return;
  let count = 0;
  for (const num of arr) {
    if (num === target) count++;
  }
  return count;
}
console.log(countOccurance(testArray, 4));

// 7) Найти сумму модулей элементов последовательности.
function calcAbsSum(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let sum = 0;
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < 0) sum -= arr[i];
    else sum += arr[i];
  }
  return sum;
  //return arr.reduce((total, curr) => total + Math.abs(curr), 0);
}
console.log(calcAbsSum(testArray));

// 8) Сколько соответствующих элементов двух последовательностей с
//одинаковым количеством элементов совпадают?
function countEqualElements(arr1, arr2) {
  if (!arr1 || !arr2 || !Array.isArray(arr1) || !Array.isArray(arr2)
  || arr1.length < 1 || arr1.length !== arr2.length) return;
  let equalCount = 0;
  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] === arr2[i]) equalCount++;
  }
  return equalCount;
}
console.log(countEqualElements(testArray, testArray.slice().reverse()));

// 10) Определить среднее арифметическое элементов последовательности.
function calcMean(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  return arr.reduce((total, curr) => total + curr) / arr.length
}
console.log(calcMean(testArray));

// 11) Определить среднее геометрическое элементов последовательности,
//содержащей положительные числа.
function calcGeomMean(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let prod = arr[0];
  for (let i = 1; i < arr.length; i++) {
    prod *= Math.abs(arr[i]);
  }
  return Math.sqrt(prod)
  // return Math.sqrt(arr.reduce((total, curr) => total * Math.abs(curr)))
}
console.log(calcGeomMean(testArray));

// 13) Определить, сколько раз встречается минимальный элемент в
//последовательности.
function countMinElementOccurance(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let min = Math.min(...arr);
  let minCount = 0;
  for (const num of arr) {
    if (num === min) minCount++
  }
  return minCount;
}
console.log(countMinElementOccurance(testArray));

// 17) Напечатать true, если элементы последовательности упорядочены по
//возрастанию, и false в противном случае.
function isAscending(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let curr = arr[0];
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < curr) return false;
    curr = arr[i];
  }
  return true;
}
console.log(isAscending(testArray));

// 18) В последовательности натуральных чисел подсчитать их количество,
//оканчивающихся заданной цифрой.
function countMod(arr, endNumber) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let count = 0;
  for (const num of arr) {
    if (num % 10 === endNumber) count++;
  }
  return count;
}
console.log(countMod(testArray, 8));

// 19.В заданной последовательности определить максимальное количество
//подряд идущих положительных чисел.
function countSequence(arr) {
  if (!arr || !Array.isArray(arr)) return;
  if (!arr.length) return 0;
  let count = 0;
  let maxCount = 0;
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < 0) continue
    if (arr[i] !== arr[i - 1]) {
      count = 1;
    } else {
      count++;
      maxCount = Math.max(maxCount, count);
    }
  }
  return maxCount;
}
console.log(countSequence(testArray));

// 22.Определить количество нечётных отрицательных элементов в
//последовательности целых чисел.
function countOddNegatives(arr) {
  if (!arr || !Array.isArray(arr)) return;
  let count = 0;
  for (let i = 1; i < arr.length; i = i + 2) {
    if (arr[i] < 0) count++;
  }
  return count;
}
console.log(countOddNegatives(testArray));

//24.Найти сумму номеров тех элементов последовательности, которые
//отрицательны. Нумерацию элементов начать с единицы.
function countNegativesNumbersSum(arr) {
  if (!arr || !Array.isArray(arr)) return;
  sum = 0;
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < 0) sum += i + 1;
  }
  return sum;
}
console.log(countNegativesNumbersSum(testArray));

// 26.Найти разность максимального и минимального элементов
//последовательности.
function getMinMaxDifference(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let min = arr[0];
  let max = arr[0];
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < min) min = arr[i];
    if (arr[i] >max) max = arr[i];
  }
  return max - min;
  //return Math.max(...arr) - Math.min(...arr);
}
console.log(getMinMaxDifference(testArray));

// 27.Между какими степенями двойки расположены все положительные
//элементы последовательности?
function getPowerOfTwoRange(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;
  let min = Infinity;
  let max = -Infinity;
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < 1) continue;
    if (arr[i] < min) min = arr[i];
    if (arr[i] > max) max = arr[i];
  }
  const minPower2 = Math.floor(Math.log2(min));
  const maxPower2 = Math.ceil(Math.log2(max));
  return [minPower2, maxPower2];
}
console.log(getPowerOfTwoRange(testArray));

//29.Найти наибольший общий делитель последовательности натуральных
//чисел
function calcGCD(arr) {
  if (!arr || !Array.isArray(arr) || arr.length < 1) return;

  const GCD = (a, b) => {
    if (a === 0) return b;
    return GCD(b % a, a);
  }

  let curr = arr[0];
  for (let i = 1; i < arr.length; i++) {
    curr = GCD(curr, arr[i])
  }
  return curr;
}
console.log(calcGCD([8, 16, 64]));