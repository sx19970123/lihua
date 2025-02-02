import SparkMD5 from "spark-md5";

/**
 * 创建子线程进行文件 hash 计算
 * @param event
 */
self.onmessage = (event) => {
    const chunks = event.data
    // 初始化md5计算工具
    const spark = new SparkMD5()
    // read读取函数
    const read = (i: number) => {
        // 读取完成后调用end()返回哈希
        if (i >= chunks.length) {
            self.postMessage(spark.end())
            return
        }
        // 文件读取器读取字节数组通过spark分段计算hash
        const reader = new FileReader();
        reader.readAsArrayBuffer(chunks[i])
        reader.onload = (e) => {
            const bytes = e.target?.result
            if (bytes) {
                spark.append(bytes)
            }
            return read(i + 1)
        }
    }
    read(0)
}