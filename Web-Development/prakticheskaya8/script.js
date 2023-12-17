"use strict";

function PC(cpu, gpu, ram, disk) {
  this.cpu = cpu;
  this.gpu = gpu;
  this.ram = ram;
  this.disk = disk;
}

PC.prototype.info = function() {
  console.log("Processor: " + this.cpu);
  console.log("Video Card: " + this.gpu);
  console.log("RAM: " + this.ram);
  console.log("Storage: " + this.disk);
}

const pc1 = new PC("intel i7-1370", "GeForce RTX 3070", "Kingston DDR4-3200", "SSD KingSpec 512GB");

pc1.info();

const pc2 = {};
pc2.cpu = "intel i5-1170";
console.log(pc2.cpu);
const randomAccessMemory = "ram";
pc2[randomAccessMemory] = "Kingston DDR5-3200"
console.log(pc2[randomAccessMemory]);


const pc3 = new Object();
pc3["gpu"] = "GeForce GT-1050";
console.log(pc3["gpu"]);

Array.prototype.mean = function() {
  return this.reduce((total, curr) => total + curr, 0) / this.length;
}

const arr = [3, 7, 10, 1, 103];
console.log(arr.mean());