import {type App} from 'vue'
export default (app:App<Element>):void => {
  app.directive('hasRole',{
    created: (el, binding, vnode, prevVNode) => {
      console.log('el=',el)
      console.log('bind',binding)
    }
  })
}
