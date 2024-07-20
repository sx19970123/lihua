import type { SpinProps } from 'ant-design-vue';
import { createSpinComponent } from './index';

/** @name 通过交叉类型(&)扩展已有的类型声明 **/
export type SpinConfig = SpinProps & {
  target?: HTMLElement | string;
};

/** 简单Spin配置 **/
export type SimpleSpinConfig = {
  tip?: string;
  target?: HTMLElement | string;
}

/** @name 获取createSpinComponent函数的返回值类型 **/
export type SpinInstance = ReturnType<typeof createSpinComponent>;
