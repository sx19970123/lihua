<template>
  <expandable-card
             style="width: 100%"
             :hover-scale="1"
             :expanded-width="600"
             :middle-style="{'background':'#fff','background-size': 'contain','border-radius':' 8px'}"
             :is-detail-visible="false"
             :expanded-height="600"
  >
    <template #overview>
      <a-card>
        <a-flex justify="space-between">
          <a-flex :gap="16">
            <user-avatar
                :value="userStore.avatar.value"
                :type="userStore.avatar.type"
                :url="userStore.avatar.url"
                :background-color="userStore.avatar.backgroundColor"
                :size="70"
            />
            <a-flex align="flex-start" vertical>
              <a-row>
                <a-typography-title :level="3" style="margin: 4px 0 0;">欢迎回来</a-typography-title>
                <a-typography-title :level="3" style="margin: 4px 0 0;" :style="{color: themeStore.getColorPrimary()}">&nbsp;{{userStore.$state.nickname}}</a-typography-title>
              </a-row>
              <a-typography-text style="margin-top: 4px" type="secondary">{{userStore.defaultDeptPosts.map(item => item.name).join(" ")}}</a-typography-text>
            </a-flex>
          </a-flex>
          <a-flex vertical style="margin-top: 10px">
            <a-typography-title :level="4" style="margin: 4px 0 0;">{{dayjs(new Date()).format('YYYY-MM-DD HH:mm')}}</a-typography-title>
            <a-typography-text :level="4" style="margin-top: 4px;" type="secondary">{{daysOfWeek[new Date().getDay()]}}</a-typography-text>
          </a-flex>
        </a-flex>
      </a-card>
    </template>
    <template #detail>
      <a-card class="scrollbar" id="test" style="border: none;">

      </a-card>
    </template>
  </expandable-card>
</template>
<script setup lang="ts">
import ExpandableCard from "@/components/expandable-card/index.vue";
import UserAvatar from "@/components/user-avatar/index.vue"
import {useUserStore} from "@/stores/user.ts";
import {useThemeStore} from "@/stores/theme.ts";
import dayjs from "dayjs";
const userStore = useUserStore();
const themeStore = useThemeStore();
const daysOfWeek = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];


</script>
<style scoped>

</style>