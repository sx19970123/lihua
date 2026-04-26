<template>
	<view class="content">
		<sar-space direction="vertical" size="large">
			<sar-space direction="vertical">
				<view class="title">基础使用</view>
				<view class="model-val">双向绑定：{{attachmentValue1}}</view>
				<attachment-upload v-model="attachmentValue1" businessCode="app-basic" businessName="基础使用示例"/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">按钮形式</view>
				<view class="description">
					可通过属性指定按钮样式
				</view>
				<view class="model-val">双向绑定：{{attachmentValue2}}</view>
				<attachment-upload v-model="attachmentValue2" businessCode="app-basic" businessName="基础使用示例" mode="button" button-type="outline" buttonText="上传聊天文件" buttonIsRound buttonIcon="CloudUploadOutlined" buttonIconFamily="outlined"/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">从聊天框选择（仅微信小程序，仅按钮模式）</view>
				<view class="description">
					从微信聊天中选取附件，设置uploadType为all或file时生效
				</view>
				<view class="model-val">双向绑定：{{attachmentValue3}}</view>
				<attachment-upload v-model="attachmentValue3" businessCode="app-basic" businessName="基础使用示例" uploadType="file" :extension="['pdf']"/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">限制数量（最多上传2个）</view>
				<view class="description">
					文件可多个选择，但只会接收前2个
				</view>
				<view class="model-val">双向绑定：{{attachmentValue4}}</view>
				<attachment-upload v-model="attachmentValue4" businessCode="app-basic" businessName="基础使用示例" :max-count="2"/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">大小限制（最大上传10k）</view>
				<view class="model-val">双向绑定：{{attachmentValue5}}</view>
				<attachment-upload v-model="attachmentValue5" businessCode="app-basic" businessName="基础使用示例" :max-size="10 * 1024"/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">只读</view>
				<view class="model-val">双向绑定：{{attachmentValue6}}</view>
				<attachment-upload v-model="attachmentValue6" businessCode="app-basic" businessName="基础使用示例" readonly/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">禁用</view>
				<view class="model-val">双向绑定：{{attachmentValue7}}</view>
				<attachment-upload v-model="attachmentValue7" businessCode="app-basic" businessName="基础使用示例" disabled/>
			</sar-space>
			<sar-divider></sar-divider>
			<sar-space direction="vertical">
				<view class="title">不自动删除</view>
				<view class="description">
					防止在表单中，删除了附件，但表单没有提交，再回显时发生附件丢失的问题。
					设置 :auto-remove="false" 在删除时不进行后台接口调用，只更新双向绑定值。
					组件抛出了businessRemove()函数，在提交表单时调用，删除附件。
				</view>
				<view class="model-val">双向绑定：{{attachmentValue8}}</view>
				<attachment-upload v-model="attachmentValue8" businessCode="app-basic" businessName="基础使用示例" :auto-remove="false"/>
			</sar-space>
		</sar-space>
	</view>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import AttachmentUpload from "@/components/attachment-upload/index.vue"

const attachmentValue1 = ref<string>("2034101053368365057,2034102399739949057")
const attachmentValue2 = ref<string>()
const attachmentValue3 = ref<string>()
const attachmentValue4 = ref<string>()
const attachmentValue5 = ref<string>()
const attachmentValue6 = ref<string>("111,222")
const attachmentValue7 = ref<string>("111")
const attachmentValue8 = ref<string>()
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