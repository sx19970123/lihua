<template>
  <div class="card-container">
    <div class="card" :style="{ width: leftWidth + 'px' }">
      <!-- Left card content goes here -->
    </div>
    <div class="divider"
         :style="{ left: leftWidth + 'px' }"
         draggable="true"
         @dragstart="startDrag"
         @drag="handleDrag"
         @dragend="stopDrag">
    </div>
    <div class="card" :style="{ width: rightWidth + 'px' }">
      <!-- Right card content goes here -->
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const startX = ref(0);
const initialLeftWidth = 200;
const leftWidth = ref(initialLeftWidth);
const rightWidth = ref(200);

const startDrag = (event) => {
  startX.value = event.clientX;
};

const handleDrag = (event) => {
  event.dataTransfer.effectAllowed = 'move'
  const deltaX = event.clientX - startX.value;
  leftWidth.value += deltaX;
  rightWidth.value -= deltaX;
  startX.value = event.clientX;
};

const stopDrag = () => {
  // Optional: Add any cleanup logic here
};

</script>

<style>
.card-container {
  display: flex;
  position: relative;
}

.card {
  border: 1px solid #ccc;
  height: 300px;
  overflow: hidden;
}

.divider {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 10px;
  background-color: #f0f0f0;
  cursor: ew-resize;
}

</style>
