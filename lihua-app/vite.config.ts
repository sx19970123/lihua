import { defineConfig } from "vite";
import uni from "@dcloudio/vite-plugin-uni";
import UniKuRoot from '@uni-ku/root'
import { IconBuildPlugin } from "./plugins/buildIcons";

export default defineConfig({
	plugins: [
		// 若存在改变 pages.json 的插件，请将 UniKuRoot 放置其后
		UniKuRoot({
			rootFileName: "AppRoot",
			enabledGlobalRef: true
		}),
		uni(), 
		IconBuildPlugin()],
	optimizeDeps: {
		exclude: ['sard-uniapp'],
	},
	css: {
		preprocessorOptions: {
			scss: {
				silenceDeprecations: ['import', 'legacy-js-api']
			},
		},
	},
});