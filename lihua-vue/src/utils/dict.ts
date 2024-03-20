// 初始化组件中需要的字典数据
import { useDictStore } from "@/stores/modules/dict";
import {getDictDataOption} from "@/api/system/dict/dictData";

export const initDict = async (...dictTypeCodes: string[]) => {
  return new Promise((resolve, reject) => {
    let resDictOption: any = {};
    const dictStore = useDictStore();
    const promises: Promise<void>[] = [];

    dictTypeCodes.forEach(code => {
      const dictOption = dictStore.getDict(code);

      if (dictOption && dictOption.length > 0) {
        resDictOption[code] = dictOption;
      } else {
        const promise = getDictDataOption(code).then(resp => {
          if (resp.code === 200) {
            console.log('data=',resp.data)
            resDictOption[code] = resp.data;
            dictStore.setDict(code, resp.data);
          }
        });
        promises.push(promise);
      }
    });

    Promise.all(promises).then(() => {
      console.log(resDictOption)
      resolve(resDictOption);
    }).catch(error => {
      reject(error);
    });
  });
};

