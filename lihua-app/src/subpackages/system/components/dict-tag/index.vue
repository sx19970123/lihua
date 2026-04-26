<template>
	<view class="content">
		<sar-space direction="vertical">
			<view class="title">基础使用</view>
			<sar-space>
				<dict-tag dict-data-value="0" :dict-data-option="sys_status"/>
				<dict-tag dict-data-value="1" :dict-data-option="sys_status"/>
			</sar-space>
			<view class="title">树型字典</view>
			<sar-space wrap>
				<dict-tag dict-data-value="2-2" :dict-data-option="test_tree" plain/>
				<dict-tag dict-data-value="2-2-1" :dict-data-option="test_tree" full-tree-node/>
				<dict-tag dict-data-value="2-2-1" :dict-data-option="test_tree" full-tree-node root-tree-node-prefix="~"/>
			</sar-space>
			<view class="description">
				树的样式
			</view>
			<sar-tree :data="test_tree" :node-keys="{ title: 'label', key: 'value' }" default-expand-all/>
		</sar-space>
	</view>
</template>

<script lang="ts" setup>
import DictTag from "@/components/dict-tag/index.vue"
import {initDict} from "@/helpers/dict"

const {sys_status, test_tree} = initDict("sys_status", "test_tree")
</script>

<style scoped lang="scss">
.title {
	font-size: var(--sar-text-lg);
	font-weight: var(--sar-font-bold);
}
.model-val {
	max-width: 100vw;
	overflow-wrap: break-word;
	font-size: var(--sar-text-sm);
}
.description {
	font-size: var(--sar-text-sm);
	color: var(--sar-secondary-color);
}
</style>