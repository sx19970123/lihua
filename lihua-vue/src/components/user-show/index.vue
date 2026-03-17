<template>
  <div>
    <div class="lihua-user-select">
      <a-flex align="center" :gap="4" wrap="nowrap">
<!--        头像-->
        <user-avatar
            :value="avatar.value"
            :type="avatar.type"
            :url="avatar.url"
            :background-color="avatar.backgroundColor"
        />
<!--        昵称-->
        <a-typography class="nickname" ellipsis v-if="props.nickname">{{props.nickname}}</a-typography>
      </a-flex>
    </div>
  </div>
</template>

<script setup lang="ts">
import UserAvatar from '@/components/user-avatar/index.vue'
import {useUserStore} from "@/stores/user.ts";
import type {AvatarType} from "@/api/system/profile/type/SysProfile.ts";
import {ref} from "vue";
import {publicAttachmentDownload} from "@/api/system/attachment/AttachmentStorage.ts";

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
      publicAttachmentDownload(avatar.value.value).then((resp:Blob) => {
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

<style scoped>
.lihua-user-select {
  border: 1px solid var(--lihua-border-color);
  border-radius: 20px;
  padding: 2px;
  display: inline-block;
  margin: var(--lihua-space-xs);
  box-shadow: var(--lihua-layout-box-shadow);
  user-select: none;
}
.nickname {
  padding-right: var(--lihua-space-sm);
  white-space: nowrap;
}
</style>