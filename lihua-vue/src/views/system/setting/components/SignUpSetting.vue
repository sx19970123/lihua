<template>
  <a-card>
    <a-form layout="vertical" :model="settingForm">
      <a-form-item label="自助注册">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              是否允许新用户注册
            </template>
            <QuestionCircleOutlined class="question-icon"/>
          </a-tooltip>
        </template>
        <a-switch v-model:checked="settingForm.enable" @change="handleChangeSwitch"></a-switch>
      </a-form-item>
      <transition :name="themeStore.routeTransition" mode="out-in">
        <div v-if="settingForm.enable">
          <a-flex :gap="16">
            <a-card>
              <a-typography-title :level="5">角色</a-typography-title>
              <a-form-item class="form-item-width" name="roleIds">
                <a-select
                    v-model:value="settingForm.roleIds"
                    placeholder="请选择用户角色"
                    :options="sysRoleList"
                    mode="multiple"
                    optionFilterProp="name"
                    :fieldNames="{label: 'name', value: 'id'}"/>
              </a-form-item>
            </a-card>
            <a-card>
              <a-typography-title :level="5">部门</a-typography-title>
              <a-form-item class="form-item-width">
                <easy-tree-select :tree-data="sysDeptList"
                                  defaultExpandAll
                                  v-model="settingForm.deptIds"
                                  :field-names="{children:'children', title:'name', key: 'id' }"
                                  @change="loadPost"
                />
              </a-form-item>
            </a-card>
            <a-card v-if="settingForm.deptIds && settingForm.deptIds.length > 0">
              <a-typography-title :level="5">岗位</a-typography-title>
              <a-form-item class="form-item-width" >
                <selectable-card
                    :data-source="sysPostList"
                    empty-description="请选择部门"
                    item-key="deptId"
                    v-model="settingForm.defaultDeptId"
                    :max-height="600"
                    :loading="postLoading"
                    vertical
                >
                  <template #content="{item, isSelected, color}">
                    <a-flex align="center" justify="space-between">
                      <a-typography-title :level="5" style="margin: 0">{{item?.deptName}}</a-typography-title>
                      <a-tag v-if="isSelected" :color="color">默认</a-tag>
                    </a-flex>
                    <div style="margin-top: var(--lihua-space-base);">
                      <div v-if="item?.postList && item?.postList.length > 0">
                        <a-checkable-tag v-for="post in item?.postList"
                                         @change="(checked: boolean) => handleSelectPostId(post.id, checked)"
                                         @click.stop="() => {}"
                                         :key="post.id"
                                         v-model:checked="post.checked">
                          {{post.name}}
                        </a-checkable-tag>
                      </div>
                      <div v-else>
                        <a-typography-text type="secondary">当前部门下暂无岗位数据</a-typography-text>
                      </div>
                    </div>
                  </template>
                </selectable-card>
              </a-form-item>
            </a-card>
          </a-flex>
          <a-form-item style="margin-top: var(--lihua-space-lg)">
            <a-button type="primary" html-type="submit" @click="handleSubmit" :loading="submitLoading">提 交</a-button>
          </a-form-item>
        </div>
      </transition>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme.ts";
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref} from "vue";
import type {SysSetting} from "@/api/system/setting/type/SysSetting.ts";
import type {SignUp} from "@/api/system/setting/type/SignUp.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import {getRoleOption} from "@/api/system/role/Role.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {traverse} from "@/utils/Tree.ts";
import {getPostOptionByDeptId} from "@/api/system/post/Post.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import SelectableCard from "@/components/selectable-card/index.vue";
import EasyTreeSelect from "@/components/easy-tree-select/index.vue"
import {message} from "ant-design-vue";
import {isAdmin} from "@/utils/Auth.ts";
import {save} from "@/api/system/setting/Setting.ts";

const componentName = getCurrentInstance()?.type.__name
const settingStore = useSettingStore();
const themeStore = useThemeStore();
const submitLoading = ref<boolean>(false);

// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  const settingData = await settingStore.getSettingInfo<SignUp>(componentName);
  if (settingData) {
    settingForm.value = settingData
    await loadPost()
  }
}

// 自助注册配置表单对象
const settingForm = ref<SignUp>({
  enable: false,
  deptIds: [],
  defaultDeptId: '',
  postIds:[],
  roleIds: []
})

// 保存到数据库中的对象
const setting = ref<SysSetting>({
  settingKey: componentName,
  json: JSON.stringify(settingForm.value)
})

// 角色
const initRoleData = () => {
  // 角色信息
  const sysRoleList = ref<Array<SysRole>>([])
  // 加载角色信息
  const initRole = async () => {
    const resp = await getRoleOption()
    if (resp.code === 200) {
      sysRoleList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }
  initRole()
  return {
    sysRoleList
  }
}
const {sysRoleList} = initRoleData()

// 部门
// 加载部门
const initDeptData = () => {
  // 部门信息
  const sysDeptList = ref<Array<SysDept>>([])

  // 加载部门信息
  const initDept = async () => {
    const resp = await getDeptOption()
    if (resp.code === 200) {
      // 单位树
      sysDeptList.value = resp.data
    } else {
      message.error(resp.msg)
    }
  }

  return {
    sysDeptList,
    initDept,
  }
}
const {sysDeptList,initDept} = initDeptData()

// 岗位/默认部门
const initPostData = () => {

  type PostOptional = {
    id?: string,
    name?: string,
    checked: boolean
  }

  type PostType = {
    deptName: string,
    deptId: string,
    postList: Array<PostOptional>,
  }

  // 岗位信息
  const sysPostList = ref<Array<PostType>>([])
  // 加载loading
  const postLoading = ref<boolean>(false)

  // 加载部门
  const loadPost = async () => {
    try {
      if (settingForm.value.deptIds) {
        postLoading.value = true
        await initPostByDeptIds(settingForm.value.deptIds)
      }
    } finally {
      postLoading.value = false
    }
  }

  // 根据部门id初始化岗位信息
  const initPostByDeptIds = async (deptIds: string[]) => {
    if (!deptIds.length) {
      sysPostList.value = []
      return
    }

    // 获取部门id｜名称集合
    const deptOptions = getDeptOptions(deptIds)

    await initPostByDeptIdOption(deptIds, deptOptions)
  }

  // 获取部门选项集合
  const getDeptOptions = (deptIds: string[]) => {
    const options: { value: string; label: string }[] = []

    deptIds.forEach(id => {
      traverse(sysDeptList.value, (dept) => {
        if (dept.id === id && dept.name) {
          options.push({
            value: dept.id,
            label: dept.name
          })
          return true
        }
      })
    })

    return options
  }

  const initPostByDeptIdOption = async (deptIds: string[], options: { label: string; value: string }[]) => {

    const originDeptIds = sysPostList.value.map(post => post.deptId)

    // 删除未选中的部门
    sysPostList.value = sysPostList.value.filter(item => deptIds.includes(item.deptId))
    // 找新增部门
    const newDeptIds = deptIds.filter(id => !originDeptIds.includes(id))

    if (!newDeptIds.length) return

    const resp = await getPostOptionByDeptId(newDeptIds)

    if (resp.code !== 200) {
      message.error(resp.msg)
      return
    }

    const data = resp.data

    newDeptIds.forEach(deptId => {
      const dept = options.find(item => item.value === deptId)

      sysPostList.value.push({
        deptId,
        deptName: dept?.label ?? '',
        postList: sysPostsToPostOptional(data[deptId])
      })
    })

    // 回显岗位
    if (settingForm.value.postIds?.length) {
      initPostTag(settingForm.value.postIds)
    }
  }

  // 岗位数据转为可选项
  const sysPostsToPostOptional = (postList?: SysPost[]): PostOptional[] => {
    if (!postList) return []

    return postList.map(post => ({
      id: post.id,
      name: post.name,
      checked: false
    }))
  }

  // 处理选中/取消选中 岗位标签
  const handleSelectPostId = (tag: string, checked: boolean) => {
    // 初始化 postIdList 为数组，如果它还没有被初始化
    if (!settingForm.value.postIds) {
      settingForm.value.postIds = [];
    }

    // 如果 checked 为 true，则添加 tag，否则删除它
    if (checked) {
      // 确保 tag 不会被重复添加
      if (!settingForm.value.postIds.includes(tag)) {
        settingForm.value.postIds.push(tag);
      }
    } else {
      // 找到 tag 的索引并将其删除
      const index = settingForm.value.postIds.indexOf(tag);
      if (index > -1) {
        settingForm.value.postIds.splice(index, 1);
      }
    }
  };

  // 回显岗位标签
  const initPostTag = (postIds: Array<String>) => {
    // 部门岗位中postId 与 postIds 相同时 checked 设置为true
    const postDeptOption = sysPostList.value
    postDeptOption.forEach(postDept => {
      if (postDept.postList && postDept.postList.length > 0) {
        postDept.postList.forEach(post => {
          if (post.id && postIds.includes(post.id)) {
            post.checked = true
          }
        })
      }
    })
  }

  return {
    sysPostList,
    postLoading,
    loadPost,
    handleSelectPostId,
    initPostByDeptIds
  }
}
const {sysPostList, postLoading, loadPost ,handleSelectPostId} = initPostData()

// 处理开关switch
const handleChangeSwitch = async () => {
  if (!isAdmin()) {
    await init()
    message.error("用户权限不足")
    return
  }

  if (!settingForm.value.enable) {
    await handleSubmit()
  }
}

// 提交保存
const handleSubmit = async () => {
  const form = settingForm.value
  // 关闭时清空配置项
  if (!form.enable) {
    settingForm.value = {
      enable: false,
      deptIds: [],
      defaultDeptId: '',
      postIds:[],
      roleIds: []
    }
  } else {
    if (form.roleIds?.length === 0) {
      message.error("请选择角色")
      return
    }
    if (form.deptIds?.length === 0) {
      message.error("请选择部门")
      return
    }
  }

  try {
    submitLoading.value = true
    setting.value.json = JSON.stringify(settingForm.value)
    const resp = await save(setting.value)

    if (resp.code === 200) {
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
  } finally {
    submitLoading.value = false
  }
}

onMounted(async () => {
  await initDept()
  await init()
})
</script>

<style scoped>
.form-item-width {
  width: 260px;
}
</style>
