import fs from 'fs'
import path from 'path'
let hasBuilt = false
/**
 * 加载图标目录
 * 在assets/icons下的css，生成icons.json文件
 * 组件中读取此文件，用于图标选择组件
 */
const buildIcons = () => {
	if (hasBuilt) {
		console.warn("图标目录文件（/src/subpackages/system/static/icons.json）已生成，如需重新生成，请重启项目")
		return
	}
	console.info("准备生成icons.json文件...")
	
	// 图标css目录
	const iconsDir = path.resolve(__dirname, '../src/static/icons')
	// svg 目录
	const svgiconsDir = path.resolve(__dirname, '../src/static/icons/svg')
	// 生成的文件
	const outFile = path.resolve(__dirname, '../src/subpackages/system/static/icons.json')
	// 文件名
	const iconNames : string[] = []
	// 读取到目标文件
	const files = fs.readdirSync(iconsDir)
	const svgFiles = fs.readdirSync(svgiconsDir)
	// 记录css文件
	for (const file of files) {
		if (file.endsWith('filled.css') || file.endsWith('outlined.css')) {
			const content = fs.readFileSync(path.join(iconsDir, file), 'utf-8')
			// 匹配 .filled-xxx|outlined-xxx 形式
			const matches = content.match(/\.(filled|outlined)-[a-zA-Z0-9-_]+/g)
			if (matches) {
				matches.forEach(m => {
					// 去除多余元素
					const name = m.replace('.', '').replace('filled-', '').replace('outlined-', '').replace('twotone-', '')
					if (!iconNames.includes(name)) {
						iconNames.push(name)
					}
				})
			}
		}
	}
	// 记录svg文件
	for (const file of svgFiles) {
		if (file.endsWith('.svg')) {
			const name = file.replace('.svg', '')
			if (!iconNames.includes(name)) {
				iconNames.push(name)
			}
		}
	}

	// 排序写入到文件
	iconNames.sort()
	fs.writeFileSync(outFile, JSON.stringify(iconNames, null, 2), 'utf-8')
	hasBuilt = true
	console.info(`icons.json 已生成，共 ${iconNames.length} 个图标`)
}

/**
 * 生成icons.json
 * 供组件内引用
 */ 
export const IconBuildPlugin = () => {
  return {
    name: 'auto-build-icons',
    buildStart() {
      buildIcons()
    }
  }
}