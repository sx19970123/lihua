import { createMD5 } from 'hash-wasm';

/**
 * 创建子线程进行文件 hash 计算
 * @param event
 */
self.onmessage = async (event) => {
    const chunks = event.data
    // 初始化md5计算工具
    const blake3 = await createMD5();
    // read读取函数
    const read = (i: number) => {
        // 读取完成后调用digest()返回哈希
        if (i >= chunks.length) {
            self.postMessage(blake3.digest("hex"))
            return
        }
        chunks[i].arrayBuffer().then((data: ArrayBuffer) => {
            blake3.update(new Uint8Array(data));
            self.postMessage(Math.trunc(i * 100 / chunks.length))
            read(i + 1)
        })
    }
    read(0)
}
