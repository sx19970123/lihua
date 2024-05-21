<template>
  <div>
    <a-flex :gap="16" vertical>
      <!--      筛选条件-->
      <a-card :style="{border: 'none'}">
        <a-form :colon="false" :label-col="{span: 6}" :wrapper-col="{span: 18}">
          <a-row :gutter="8">
            <a-col>
              <a-form-item label="部门">
                <a-tree-select style="width: 180px"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item class="form-item-single-line" label="昵称">
                <a-input/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item class="form-item-single-line" label="用户名">
                <a-input/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item class="form-item-single-line" label="手机号码">
                <a-input/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item class="form-item-single-line" label="状态">
                <a-select style="width: 180px;"/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item  class="form-item-single-line" label="创建时间">
                <a-range-picker/>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item class="form-item-single-line">
                <a-space size="small">
                  <a-button type="primary">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button >
                    <template #icon>
                      <RedoOutlined />
                    </template>
                    重 置
                  </a-button>
                </a-space>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <a-table :columns="userColumn">

      </a-table>
    </a-flex>
  </div>
</template>

<script setup lang="ts">

// 列表查询
import type {ColumnsType} from "ant-design-vue/es/table/interface";

const initSearch = () => {
  const userColumn: ColumnsType = [
    {
      title: '用户名',
      key: 'username',
      dataIndex: 'username'
    },
    {
      title: '昵称',
      key: 'nickname',
      dataIndex: 'nickname'
    },
    {
      title: '所属部门',
      key: 'deptList',
      dataIndex: 'deptList'
    },
    {
      title: '手机号码',
      key: 'phoneNumber',
      dataIndex: 'phoneNumber',
      align: 'center'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      align: 'center'
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      align: 'center'
    },
    {
      title: '操作',
      key: 'action',
      dataIndex: 'action',
      align: 'center'
    }
  ]

  return {
    userColumn
  }
}

const {userColumn} = initSearch()
//
</script>

<style scoped>

</style>