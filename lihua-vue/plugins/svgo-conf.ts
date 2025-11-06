import {XastNode, XastRoot, PluginInfo} from 'svgo';

/**
 * 递归遍历 SVG 节点，并为设置了 'fill' 属性的元素设置为 'currentColor'。
 * @param node 要处理的当前节点
 */
const traverse = (node: XastNode): void => {
    if (node.type === 'element' && node.attributes) {
        node.attributes.fill = 'currentColor';
        // 如果节点有子节点，则递归处理子节点
        if ('children' in node && Array.isArray(node.children)) {
            node.children.forEach(child => {
                traverse(child);
            });
        }
    }
    if (('children' in node) && Array.isArray(node.children)) {
        node.children.forEach(child => {
            traverse(child as XastNode);
        });
    }
};

/**
 * 设置svg图标fill为CurrentColor的插件
 * 递归遍历节点，将所有节点 fill 均设置为 CurrentColor
 * 此时图标组件颜色将受css控制
 * @param excludePath 路径下的图标不进行处理，目的是保证彩色图标保持自己颜色
 */
const setSvgCurrentColorPlugin = (excludePath: string) => {
    return {
        name: 'setSvgCurrentColor',
        fn: <P> (root: XastRoot, param: unknown, info: PluginInfo) => {
            if (info.path && info.path.includes(excludePath)) {
                return;
            }
            // 递归处理svg节点，替换attributes.fill属性
            traverse(root);
        },
    } as any
};

export default setSvgCurrentColorPlugin