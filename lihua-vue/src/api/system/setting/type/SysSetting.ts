export interface SysSetting {
    /**
     * 主键
     */
    id?: string

    /**
     * 设置key（值为设置项组件名称）
     */
    settingKey?: string,

    /**
     * 设置json
     */
    json: string
}
