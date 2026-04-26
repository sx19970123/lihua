<template>
	<view class="content">
		<sar-tabbar-pit />
		<sar-space direction="vertical" size="large">
			<!-- 提示栏 -->
			<sar-notice-bar>基于 uni-app 开发，仅适配 App（Android、iOS、鸿蒙）及微信小程序，其余平台请自行测试。</sar-notice-bar>
			<!-- title卡片 -->
			<sar-card>
			    <sar-space size="large">
					<sar-avatar src="/static/logo.png" shape="square"></sar-avatar>
					<sar-space direction="vertical">
						<h4>Hello 狸花猫 APP</h4>
						<text @click="showVersion = true" style="color: var(--sar-primary); font-size: var(--sar-text-base);">当前版本 {{versionInfo[0].version}}</text>
					</sar-space>
				</sar-space>
			</sar-card>
			<!-- 开源说明卡片 -->
			<sar-card>
			    <sar-space size="large">
					<text>📜 基于 MIT 协议开源，免费使用，支持商用</text>
				</sar-space>
			</sar-card>
			<!-- 功能介绍卡片 -->
			<sar-card>
				<sar-read-more keep-location>
					<sar-space direction="vertical">
						<text>
						  🧩 基于「狸花猫后台管理系统」的业务扩展方案，适配 App 与微信小程序，与 Web 端能力保持一致。
						</text>
						<text>✨ 功能概览</text>
						<text>🧠 验证码：集成 tianai验证码，Web 端统一配置启用状态</text>
						<text>👤 个人中心：头像、昵称等基础信息与后端保持一致</text>
						<text>🛡️ 权限体系：支持角色、权限、部门标识，user store 可获取</text>
						<text>🔔 通知公告：WebSocket 实时消息，App 原生通知提醒</text>
						<text>🌗 暗色模式：支持全局暗色主题，App 端可手动切换，微信小程序跟随系统设置</text>
					</sar-space>
				</sar-read-more>
			</sar-card>
			<!-- 项目依赖 -->
			<sar-card>
				<sar-read-more keep-location>
					<sar-space direction="vertical">
						<text>
						⚙️ 项目基于 Vue 3 + TypeScript 的 uni-app 开发，
						组件库采用 sard-uniapp，整体结构清晰、易扩展。
						</text>

						<text>🧩 技术特性</text>
						<text>🗂️ 状态管理：内置 Pinia 状态管理方案，统一管理全局状态，提升数据流可维护性</text>
						<text>🌐 网络请求：内置 Request 工具，支持统一的请求 / 响应拦截处理</text>
						<text>🧭 路由管理：基于 Router 实现路由跳转，支持前置拦截与权限校验</text>
						<text>🧱 全局能力：集成 uni-ku/root，模拟 Web 端 Vue 根组件，便于集中处理全局逻辑</text>
						</sar-space>
				</sar-read-more>
			</sar-card>
		</sar-space>
		
		<sar-popout
		  title="版本详情"
		  v-model:visible="showVersion"
		  :show-confirm="false"
		>
		  <scroll-view scroll-y style="max-height: 60vh;">
		    <sar-timeline>
		      <sar-timeline-item
		        v-for="(item, index) in versionInfo"
		        :key="item.version"
		        :title="`v${item.version}`"
		        :time="item.updateDate || '—'"
		        icon="circle"
				:icon-color="index === 0 ? 'var(--sar-primary)' : ''"
				
		      >
		        <view class="version-content">
		          <view
		            v-for="(content, i) in item.updateContent"
		            :key="i"
		            class="version-item"
		          >
		            <text>{{ content }}</text>
		          </view>
		        </view>
		      </sar-timeline-item>
		    </sar-timeline>
		  </scroll-view>
		</sar-popout>

	</view>
</template>

<script setup lang="ts">
import {ref} from 'vue'
const showVersion = ref<boolean>(false)


const versionInfo = [
	{
		version: '2.1.0',
		updateDate: '2026-xx-xx',
		updateContent: [
			'1. 针对后台controller接口调整进行适配',
			'2. 将utils包拆分出helpers用于存放业务相关工具代码',
			'3. 项目中ts文件修改为全小写命名',
			'4. 更新sard-uniapp版本到 1.29.3',
		]
	},
	{
		version: '2.0.1',
		updateDate: '2026-04-07',
		updateContent: [
			'1. 修复鸿蒙APP无法切换暗色｜亮色模式的bug',
			'2. 更新uniapp版本到 3.0.0-5000520260324001',
			'3. 更新sard-uniapp版本到 1.29.0',
			'4. 更新vue版本到 3.5.31',
		]
	},
	{
		version: '2.0.0',
		updateDate: '2026-03-19',
		updateContent: [
			'🎉🎉2.0系统版本更新',
			'1. 与后端版本保持同步',
			'2. 适配兼容鸿蒙APP'
		]
	}, {
		version: '1.0.0',
		updateDate: '2025-12-13',
		updateContent: [
			'🎉🎉狸花猫APP正式上线啦',
		]
	}
]
</script>
