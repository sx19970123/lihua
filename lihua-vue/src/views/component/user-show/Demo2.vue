<template>
  <div>
    <a-typography-title :level="4">多用户</a-typography-title>
      <a-row>
        <a-col :span="6">
          <a-card v-if="userList.length > 0">
            <a-flex wrap="wrap" gap="small">
              <user-show
                  v-for="user in userList"
                  :avatar-json="user.avatar"
                  :nickname="user.nickname"/>
            </a-flex>
          </a-card>
        </a-col>
      </a-row>
  </div>
</template>

<script setup lang="ts">
import UserShow from "@/components/user-show/index.vue"
import {queryPage} from "@/api/system/user/User.ts";
import {onMounted, ref} from "vue";
import type {SysUserVO} from "@/api/system/user/type/SysUser.ts";
const userList = ref<SysUserVO[]>([])
onMounted(async () => {
  const resp = await queryPage({pageNum: 1, pageSize: 20})
  if (resp.code === 200) {
    userList.value = resp.data.records
  }
})

</script>
