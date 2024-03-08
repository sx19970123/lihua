<template>
  <a-flex vertical :gap="16">
<!--    检索条件-->
    <a-card>
      <a-form>
        <a-flex :gap="8" align="center">
          <a-form-item class="form-item-single-line"  label="字典名称">
            <a-input placeholder="请输入字典名称"/>
          </a-form-item>
          <a-form-item class="form-item-single-line"  label="字典编码">
            <a-input placeholder="请输入字典编码"/>
          </a-form-item>
          <a-form-item class="form-item-single-line">
            <a-button type="primary" @click="getPageData"> <SearchOutlined /> 查 询</a-button>
          </a-form-item>
          <a-form-item class="form-item-single-line">
            <a-button> <RedoOutlined /> 重 置</a-button>
          </a-form-item>
        </a-flex>
      </a-form>
    </a-card>
<!--    列表页-->
    <a-table :data-source="dataSource" :columns="columns" :pagination="false">
      <template #title>
        <a-flex :gap="8">
          <a-button type="primary"> <PlusOutlined /> 新 增</a-button>
          <a-button danger><DeleteOutlined /> 删 除</a-button>
        </a-flex>
      </template>
      <template #footer style="padding: 0">
        <a-flex justify="flex-end">
          <a-pagination v-model:current="current" :total="500" />
        </a-flex>
      </template>
    </a-table>
  </a-flex>
</template>

<script setup lang="ts">
import {ref, type Ref} from "vue";
import type {SysDictType, SysDictTypeDTO} from "@/api/system/dict/type/SysDictType";
import type {ResponseType, PageResponseType } from "@/api/type"
import {findPage} from "@/api/system/dict/dict";

const init = () => {
  const query = ref<SysDictTypeDTO>({
    name: '',
    code: '',
    createTimeStart: '',
    createTimeEnd: '',
    pageNum: 0,
    pageSize: 0
  })
  const total = ref<number>()
  const data = ref<Array<SysDictType>>()
  return {
    query,
    total,
    data
  }
}
const {query,total,data} = init()

const getPageData = async () => {
  const resp:ResponseType<PageResponseType<SysDictType>> = await findPage(query.value)
  if (resp.code === 200) {
    data.value = resp.data.records
  }

}

getPageData()
</script>

<style>

</style>

