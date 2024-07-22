<template>
  <div>
    <card-select :data-source="userOption" item-key="value">
      <template #content>
        <user-show avatar-json="" nickname="yukino"/>
      </template>
    </card-select>
  </div>
</template>
<script lang="ts" setup>
import CardSelect from "@/components/card-select/index.vue"
import UserShow from "@/components/user-show/index.vue"
import {ref} from "vue";
import type {CommonTree} from "@/api/global/Type.ts";
import {getUserOption} from "@/api/system/user/User.ts";
import {message} from "ant-design-vue";
// 用户选项
const userOption = ref<Array<CommonTree>>([])
const handleUserOption = async () => {
  const resp = await getUserOption()
  if (resp.code === 200) {
    userOption.value = resp.data
  } else {
    message.error(resp.msg)
  }
}
handleUserOption()
</script>

