<template>
  <a-flex :gap="16" vertical>
    <a-row>
      <a-col :span="24">
        <a-badge-ribbon text="now time">
          <index_1></index_1>
        </a-badge-ribbon>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="6">
        <index_2></index_2>
      </a-col>
      <a-col :span="6">
        <index_3></index_3>
      </a-col>
      <a-col :span="6">
        <index_4></index_4>
      </a-col>
      <a-col :span="6">
        <index_5></index_5>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <index_6></index_6>
      </a-col>
      <a-col :span="12">
        <index_8></index_8>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="12">
        <index_7></index_7>
      </a-col>
      <a-col :span="12">
        <a-row :gutter="16">
          <a-col :span="12">
            <index_9></index_9>
          </a-col>
          <a-col :span="12">
            <index_10></index_10>
          </a-col>
        </a-row>
        <a-row :gutter="16" style="margin-top: 16px">
          <a-col :span="12">
            <index_11></index_11>
          </a-col>
          <a-col :span="12">
            <index_12></index_12>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </a-flex>

</template>

<script setup lang="ts">
import index_1 from './components/index_1.vue';
import index_2 from './components/index_2.vue';
import index_3 from './components/index_3.vue';
import index_4 from './components/index_4.vue';
import index_5 from './components/index_5.vue';
import index_6 from './components/index_6.vue';
import index_7 from './components/index_7.vue';
import index_8 from './components/index_8.vue';
import index_9 from './components/index_9.vue';
import index_10 from './components/index_10.vue';
import index_11 from './components/index_11.vue';
import index_12 from './components/index_12.vue';
</script>
