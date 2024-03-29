// 将树形结构扁平化
export const flattenTreeData  = <T> (tree:Array<T>,resultList:Array<T> ,children: string = 'children')=> {
  tree.forEach((item:any) => {
    if (item[children]) {
      flattenTreeData(item[children],resultList,children)
    }
    resultList.push(item)
  })
}

export default {
  flattenTreeData
}
