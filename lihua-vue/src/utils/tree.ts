import {cloneDeep} from 'lodash-es';

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
  handleFlatten(cloneDeep(tree), resultList, children);
  return resultList;
}

/**
 * 将具有树形结构的集合转为树
 * @param originList 扁平化的具有树形结构的集合
 * @param rootValue root节点id值，默认 0
 * @param id 用于构建父级结构的 id 默认 id
 * @param parentId 用于构建父级结构的 parentId 默认 parentId
 * @param children 子集节点容器名称 默认 children
 */
export const buildTree = <T> (originList: Array<T>,
                              rootValue: string = DEFAULT_ROOT_VALUE,
                              id: string = DEFAULT_ID,
                              parentId: string = DEFAULT_PARENT_ID,
                              children: string = DEFAULT_CHILDREN) => {
  const map = new Map();
  const tree:Array<T> = [];
  const list = cloneDeep(originList)
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


/**
 * 树形结构遍历
 * @param tree 树形结构数组
 * @param callback 树形结构遍历回调函数，回调参数为节点元素，return true可终止递归
 * @param children 子集节点容器名称 默认 children
 */
export const traverse = <T> (tree: Array<T>, callback: (item: T) => void | boolean, children: string = DEFAULT_CHILDREN) => {
    for(const item of tree) {
        if (callback(item) === true) {
            return true
        }
        const itemChildren = (item as any)[children]
        if (itemChildren && itemChildren.length > 0) {
            if (traverse(itemChildren, callback, children)) {
                return true
            }
        }
    }
    return false
}

/**
 * 遍历树结构并回调每个节点，callback 返回值将被收集成数组。
 * @param tree 树形数组
 * @param callback 每个节点调用的回调函数，参数为 (path: T[])，包含从根到当前节点的完整路径
 * @param children 子节点字段名，默认 'children'
 */
export const traverseWithPath = <T> (tree: T[], callback: (path: T[]) => any, children: string = DEFAULT_CHILDREN) => {
  const result: any[] = [];

  const dfs = (nodes: T[], path: T[]) => {
    for (const node of nodes) {
      const newPath = [...path, node];
      // 收集 callback 返回值
      result.push(callback(newPath));

      const childList = (node as any)[children];
      if (Array.isArray(childList) && childList.length > 0) {
        dfs(childList, newPath);
      }
    }
  };

  dfs(tree, []);
  return result;
}

// 处理将树形结构进行扁平化
const handleFlatten = <T> (tree:Array<T>, resultList:Array<T> , children: string = DEFAULT_CHILDREN)=> {
  tree.forEach((item:any) => {
    if (item[children]) {
      handleFlatten(item[children],resultList,children)
    }
    // 去除children属性
    item[children] = undefined
    resultList.unshift(item)
  })
}
