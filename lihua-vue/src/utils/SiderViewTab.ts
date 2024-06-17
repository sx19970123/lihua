
/**
 * 判断路由中指定的component 和 目标类型是否相同
 * @param obj1
 * @param obj2
 */
export const isComponentTypeEq = (obj1: any, obj2: any) => {
    return obj1?.__file === obj2?.__file;
}