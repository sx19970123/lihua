const DEFAULT_ID = "id";
const DEFAULT_PARENT_ID = "parentId";
const DEFAULT_CHILDREN = "children";
const DEFAULT_ROOT_VALUE = "0";
/**
 * 将树型数据进行扁平化处理
 * @param tree 待处理的树
 * @param children 定义子节点元素名
 */
export const flattenTree = <T> (tree: Array<T>, children: string = DEFAULT_CHILDREN): Array<T> => {
  const resultList: Array<T> = []
  handleFlatten(tree, resultList, children);
  return resultList;
}

/**
 * 将具有树形结构的集合转为树
 * @param list 扁平化的具有树形结构的集合
 * @param rootValue root节点id值，默认 0
 * @param id 用于构建父级结构的 id 默认 id
 * @param parentId 用于构建父级结构的 parentId 默认 parentId
 * @param children 子集节点容器名称 默认 children
 */
export const buildTree = <T> (list: Array<T>,
                              rootValue: string = DEFAULT_ROOT_VALUE,
                              id: string = DEFAULT_ID,
                              parentId: string = DEFAULT_PARENT_ID,
                              children: string = DEFAULT_CHILDREN) => {
  const map = new Map();
  const tree:Array<T> = [];

  // 先将所有节点存入 Map，并初始化 `children`
  list.forEach((item: any) => {
    map.set(item[id], { ...item, [children]: [] });
  });

  list.forEach((item: any) => {
    const node = map.get(item[id]);
    if (rootValue ? item[parentId] === rootValue : !!item[parentId]) {
      tree.push(node);
    } else {
      const parent = map.get(item[parentId]);
      if (parent) {
        parent.children.push(node);
      }
    }
  });

  return tree;
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
