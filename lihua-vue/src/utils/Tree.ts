
/**
 * 将树型数据进行扁平化处理
 * @param tree 待处理的树
 * @param children 定义子节点元素名
 */
export const flattenTree = <T> (tree:Array<T>, children: string = 'children'): Array<T> => {
  const resultList: Array<T> = []
  handleFlatten(tree, resultList, children);
  return resultList;
}

// 处理将树形结构进行扁平化
const handleFlatten = <T> (tree:Array<T>, resultList:Array<T> , children: string = 'children')=> {
  tree.forEach((item:any) => {
    if (item[children]) {
      handleFlatten(item[children],resultList,children)
    }
    // 去除children属性
    item[children] = undefined
    resultList.push(item)
  })
}
