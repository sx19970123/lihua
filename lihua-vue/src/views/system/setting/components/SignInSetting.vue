<template>
  <div style="margin: 8px;padding-right: 20px">
    <a-form layout="vertical" :model="settingForm">
      <a-form-item label="自助注册">
        <template #tooltip>
          <a-tooltip>
            <template #title>
              是否允许新用户注册
            </template>
            <QuestionCircleOutlined style="margin-left: 4px"/>
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
                <div style="margin-top: 0">
                  <a-checkable-tag v-model:checked="deptTreeSetting.checked" @click="handleCheckedAllKeys">全选/全不选</a-checkable-tag>
                  <a-checkable-tag v-model:checked="deptTreeSetting.expand" @click="handleExpanded">展开/折叠</a-checkable-tag>
                  <a-checkable-tag v-model:checked="deptTreeSetting.checkStrictly" @click=" deptTreeSetting.checkStrictly ? settingForm.deptIds = [] : ''">父子关联</a-checkable-tag>
                </div>
                <!--              部门树-->
                <div class="dept-card">
                  <a-input placeholder="检索部门树" v-model:value="deptKeyword" allowClear style="margin-bottom: 8px; height: 28px"/>
                  <a-tree
                      :tree-data="sysDeptList"
                      :field-names="{children:'children', title:'name', key: 'id' }"
                      :check-strictly="!deptTreeSetting.checkStrictly"
                      v-model:checked-keys="settingForm.deptIds"
                      v-model:expanded-keys="deptTreeSetting.expandKeys"
                      :selectable="false"
                      checkable
                  >
                    <template  #title="{ name }">
                      <div v-if="name.indexOf(deptKeyword) > -1">
                        <span>{{name.substring(0,name.indexOf(deptKeyword))}}</span>
                        <span :style="{'color':  themeStore.getColorPrimary()}">{{deptKeyword}}</span>
                        <span>{{name.substring(name.indexOf(deptKeyword) + deptKeyword.length)}}</span>
                      </div>
                      <span v-else>{{ name }}</span>
                    </template>
                  </a-tree>
                </div>
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
                    <div style="margin-top: 16px;">
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
          <a-form-item style="margin-top: 24px">
            <a-button type="primary" html-type="submit" @click="handleSubmit" :loading="submitLoading">提 交</a-button>
          </a-form-item>
        </div>
      </transition>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {useThemeStore} from "@/stores/theme.ts";
import {useSettingStore} from "@/stores/setting.ts";
import {getCurrentInstance, onMounted, ref, watch} from "vue";
import type {SystemSetting} from "@/api/system/setting/type/SystemSetting.ts";
import type {SignIn} from "@/api/system/setting/type/SignIn.ts";
import type {SysRole} from "@/api/system/role/type/SysRole.ts";
import {getRoleOption} from "@/api/system/role/Role.ts";
import type {SysDept} from "@/api/system/dept/type/SysDept.ts";
import { cloneDeep } from 'lodash-es';
import {getDeptOption} from "@/api/system/dept/Dept.ts";
import {flattenTree} from "@/utils/Tree.ts";
import {getPostOptionByDeptId} from "@/api/system/post/Post.ts";
import type {SysPost} from "@/api/system/post/type/SysPost.ts";
import SelectableCard from "@/components/selectable-card/index.vue";
import {message} from "ant-design-vue";
import {isAdmin} from "@/utils/Auth.ts";
import {ResponseError} from "@/api/global/Type.ts";
const componentName = getCurrentInstance()?.type.__name
const settingStore = useSettingStore();
const themeStore = useThemeStore();
const submitLoading = ref<boolean>(false);

// 没有进行双向绑定的单位树
let originDeptTree: Array<SysDept> = ([])
// 加载配置，已保存的系统配置中没有当前配置的话会进行创建
const init = async () => {
  const resp = await settingStore.getSetting<SignIn>(componentName);
  if (!resp) {
    await settingStore.save(setting.value)
  } else {
    await initDept()
    settingForm.value = resp
  }
}

// 自助注册配置表单对象
const settingForm = ref<SignIn>({
  enable: false,
  deptIds: [],
  defaultDeptId: '',
  postIds:[],
  roleIds: []
})

// 保存到数据库中的对象
const setting = ref<SystemSetting>({
  settingName: '自助注册',
  settingComponentName: componentName,
  settingJson: JSON.stringify(settingForm.value)
})

// 角色
const initRoleData = () => {
  // 角色信息
  const sysRoleList = ref<Array<SysRole>>([])
  // 加载角色信息
  const initRole = async () => {
    try {
      const resp = await getRoleOption()
      if (resp.code === 200) {
        sysRoleList.value = resp.data
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
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
  // 扁平化部门树
  const flattenDeptList = ref<Array<SysDept>>([])
  // 部门树检索关键字
  const deptKeyword = ref<string>('')
  // 所有树型节点id
  const deptIds: string[] = []

  type DeptTreeSettingType = {
    // 展开的节点
    expandKeys: string[],
    // 是否全部展开
    expand: boolean,
    // 父子联动
    checkStrictly: boolean
    // 是否全部选中
    checked: boolean,
  }
  // 部门树配置信息
  const deptTreeSetting = ref<DeptTreeSettingType>({
    expandKeys: [],
    expand: false,
    checkStrictly: false,
    checked: false
  })

  // 处理展开折叠
  const handleExpanded = () => {
    // 全部展开
    deptTreeSetting.value.expandKeys = []
    if (deptTreeSetting.value.expand) {
      deptTreeSetting.value.expandKeys.push(... deptIds)
    }
  }

  // 处理全选
  const handleCheckedAllKeys = () => {
    settingForm.value.deptIds = []
    if (deptTreeSetting.value.checked) {
        settingForm.value.deptIds.push(... deptIds)
    }
  }

  // 处理关键词过滤
  const filterTreeByLabel = (tree: Array<SysDept>, keyword: string): Array<SysDept> => {
    const cloneTree = cloneDeep(tree);

    const filterNode = (node: SysDept): SysDept | null => {
      if (node.children) {
        node.children = node.children.map(filterNode).filter((child): child is SysDept => child !== null);
      }
      return node.name?.includes(keyword) || (node.children && node.children.length > 0) ? node : null;
    };

    return cloneTree.map(filterNode).filter((node: SysDept) => node !== null);
  };


  // 加载部门信息
  const initDept = async () => {
    try {
      const resp = await getDeptOption()
      if (resp.code === 200) {
        // 单位树
        sysDeptList.value = resp.data
        // 未双向绑定的单位树
        originDeptTree = resp.data
        // 处理为扁平化数据
        flattenDeptList.value = flattenTree(resp.data)
        // 获取全部部门id
        const mapIds = flattenDeptList.value.filter(item => item.id).map(item => item.id)
        deptIds.push(... (mapIds as string[]))
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }

  return {
    sysDeptList,
    deptTreeSetting,
    deptKeyword,
    flattenDeptList,
    deptIds,
    initDept,
    handleExpanded,
    handleCheckedAllKeys,
    filterTreeByLabel
  }
}
const {sysDeptList,deptTreeSetting,deptKeyword,flattenDeptList,initDept,handleExpanded, handleCheckedAllKeys,filterTreeByLabel} = initDeptData()
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
      postLoading.value = true
      await initPostByDeptIds(handleDeptIdList())
    } catch (error) {
      console.error(error)
    } finally {
      postLoading.value = false
    }
  }

  // 将 {checked: string[]} 或 string[] 数据处理为统一 数组
  const handleDeptIdList = (): string[] => {
    const obj = settingForm.value.deptIds as {checked: string[]} | string[]
    let ids = []
    if ((obj as {checked: string[]}).checked) {
      ids = (obj as {checked: string[]}).checked
    } else {
      ids = (obj as string[])
    }
    return ids
  }

  // 通过部门id获取部门名称用于回显
  const initPostByDeptIds = async (value: Array<string>) => {
    const option:Array<{
      value: string,
      label: string
    }> = []
    // 组合option
    value.forEach(item => {
      flattenDeptList.value.forEach(dept => {
        if (dept.id === item && dept.name) {
          option.push({
            value: dept.id,
            label: dept.name
          })
        }
      })
    })

    if (value.length > 0) {
      await initPostByDeptIdOption(value, option)
    } else {
      sysPostList.value = []
    }
  }

  // 加载岗位信息，通过 选中部门/表单回显写入部门id 进行加载
  const initPostByDeptIdOption = async (deptIds: string[], option: Array<{label: string, value: string}>) => {
    // 记录原始部门ID集合
    const originDeptIds = sysPostList.value.map(post => post.deptId)
    // 删除没有被选中的id
    originDeptIds.forEach(deptId => {
      if (!deptIds.includes(deptId)) {
        const index = sysPostList.value.findIndex(item => item.deptId === deptId)
        if (index > -1) {
          sysPostList.value.splice(index, 1)
        }
      }
    })

    // 如果新旧ID集合长度相同，直接返回
    if (deptIds.length === sysPostList.value.length) {
      return;
    }

    // 新选中的部门id集合
    const newDeptIds = deptIds.filter(item => !originDeptIds.includes(item))
    // 后端查询新部门及岗位数据
    try {
      const resp = await getPostOptionByDeptId(newDeptIds)
      if (resp.code === 200) {
        const data = resp.data
        newDeptIds.forEach(deptId => {
          sysPostList.value.push({
            deptId: deptId,
            deptName: cloneDeep(option).find((item:{label: string, value: string}) => item.value === deptId).label,
            postList: sysPostsToPostOptional(data[deptId])
          })
        })
        // 回显部门
        if (settingForm.value.postIds) {
          initPostTag(settingForm.value.postIds)
        }
      } else {
        message.error(resp.msg)
      }
    } catch (e) {
      if (e instanceof ResponseError) {
        message.error(e.msg)
      } else {
        console.error(e)
      }
    }
  }

  // 将 SysPost 集合  转换为 PostOptional 集合
  const sysPostsToPostOptional = (postList: Array<SysPost>): Array<PostOptional> => {
    const resp: Array<PostOptional> = []
    if (postList) {
      postList.forEach(post => {
        resp.push({
          id: post.id,
          name: post.name,
          checked: false
        })
      })
    }
    return resp
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
    initPostByDeptIds,
    handleDeptIdList
  }
}
const {sysPostList, postLoading, loadPost ,handleSelectPostId,} = initPostData()

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
  submitLoading.value = true
  setting.value.settingJson = JSON.stringify(settingForm.value)
  try {
    const resp = await settingStore.save(setting.value)

    if (resp.code === 200) {
      message.success(resp.msg)
    } else {
      message.error(resp.msg)
    }
  } finally {
    submitLoading.value = false
  }

}

// 监听部门关键字变化
watch(() => deptKeyword.value, (value) => {
  if (value) {
    // 关键词输入时全部展开
    if (!deptTreeSetting.value.expand) {
      deptTreeSetting.value.expand = true
      handleExpanded()
    }
    // 对树型结构进行过滤
    sysDeptList.value = filterTreeByLabel(originDeptTree, value)
  } else {
    // value 为空时，还原树
    sysDeptList.value = cloneDeep(originDeptTree)
  }
})

// 监听反向选中标签，显示岗位
watch(() => settingForm.value.deptIds, (value) => {
  let ids: string[] = [];
  if (Array.isArray(value)) {
    // 如果 value 是数组
    ids = value;
    // 加载岗位
    loadPost()
    // 更新 deptTreeSetting 的 checked 状态
    deptTreeSetting.value.checked = ids.length === flattenDeptList.value.length;
  } else if (value && typeof value === 'object' && 'checked' in value) {
    // 如果 value 是包含 checked 属性的对象
    ids = (value as { checked: string[] }).checked;
    // 当settingForm.value.deptIds为对象时，提取值重新赋值，这时会再次触发watch，再次触发时执行后续逻辑
    settingForm.value.deptIds = ids
  }

})
onMounted(() => {
  init()
})
</script>

<style scoped>
.form-item-width {
  width: 260px;
}
.dept-card {
  border-radius: 8px;
  padding: 16px;
  margin-top: 4px;
}
</style>

<style>
[data-theme="light"] {
  .dept-card {
    border: 1px solid #d9d9d9;
  }
}
[data-theme="dark"] {
  .dept-card {
    border: 1px solid #424242;
  }
}
</style>

