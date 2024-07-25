<template>
  <div>
    <div class="lihua-user-select">
      <a-flex align="center" :gap="4">
<!--        头像-->
        <user-avatar
            :value="avatar.value"
            :type="avatar.type"
            :url="avatar.url"
            :background-color="avatar.backgroundColor"
        />
<!--        昵称-->
        <a-typography style="padding-right: 8px;" v-if="props.nickname">{{props.nickname}}</a-typography>
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import UserAvatar from '@/components/user-avatar/index.vue'
import {useUserStore} from "@/stores/modules/user.ts";
import type {AvatarType} from "@/api/system/profile/type/SysProfile.ts";
import {ref} from "vue";
import {getAvatar} from "@/api/system/profile/Profile.ts";
const userStore = useUserStore();

const props = defineProps<{
  avatarJson?: string,
  nickname?: string
}>()
// 回显头像
const avatar = ref<AvatarType>({})
try {
  if (props.avatarJson) {
    avatar.value = JSON.parse(props.avatarJson)
    // 处理图片类型头像
    if (avatar.value.value && avatar.value.type === 'image') {
      getAvatar(avatar.value.value).then((resp:Blob) => {
        avatar.value.url = URL.createObjectURL(resp)
      })
    }
  } else {
    avatar.value = userStore.getDefaultAvatar()
    avatar.value.value = props.nickname
  }
} catch (e) {
  console.error("头像获取异常，重置为默认头像")
  avatar.value = userStore.getDefaultAvatar()
  avatar.value.value = props.nickname
}

</script>

<style>
.lihua-user-select {
  border: 1px solid #d9d9d9;
  border-radius: 16px;
  display: inline-block;
  margin: 4px;
  box-shadow: var(--lihua-layout-light-box-shadow);
  user-select: none;
}
[data-theme="dark"] {
  .lihua-user-select {
    border: 1px solid #424242;
    box-shadow: var(--lihua-layout-dark-box-shadow);
  }
}
</style>