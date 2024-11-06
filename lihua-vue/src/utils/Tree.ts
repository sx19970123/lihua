
/**
 * 将树型数据进行扁平化处理
 * @param tree 待处理的树
 * @param resultList 目标返回数据集合
 * @param children 定义子节点元素名
 */
export const flattenTreeData  = <T> (tree:Array<T>, resultList:Array<T> , children: string = 'children')=> {
  tree.forEach((item:any) => {
    if (item[children]) {
      flattenTreeData(item[children],resultList,children)
    }
    resultList.push(item)
  })
}
