
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

/**
 * 传入节点key，获取该节点全部祖先节点集合
 * @param tree 待处理的树
 * @param resultList 目标返回数据集合
 * @param key 待匹配的节点key
 * @param children 定义子节点元素名，默认 children
 * @param id 定义key元素名，默认 id
 * @param pId 定义pkey元素名，默认 parentId
 */
export const findAncestorsOfNodeInTree = <T> (tree: Array<T>,
                                              resultList:Array<T>,
                                              key: string,
                                              children: string = 'children',
                                              id: string = 'id',
                                              pId:string = 'parentId') => {
  for (let i = 0; i < tree.length; i++) {
    const item:any = tree[i]
    if (item[id] !== key) {
      if (item[children] && item[children].length > 0) {
        const pKey = findAncestorsOfNodeInTree(item[children],resultList,key,children,id,pId)
        if (pKey && pKey === item[id]) {
          resultList.push(item)
        }
        return item[pId]
      }
    } else {
      resultList.push(item)
      return item[pId]
    }
  }
}


export default {
  flattenTreeData,
  findAncestorsOfNodeInTree
}
