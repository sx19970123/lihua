// 当前版本更新记录
const activeUpdate = [
    {
        version: '1.2.2',
        record: [
            '1. 附件组件支持传入附件名称',
            '2. 修复简单属性选择组建设置默认全选后标签不回显的bug',
            '3. 后端新增允许跨域配置',
            '4. 更新axios版本到1.11.0',
            '5. 更新vite版本到6.3.4',
            '6. 移除jsx、vitest',
            '7. 更新ts版本到^5.9.2'
        ]
    }
]

/**
 * 首页展示版本日志
 * 项目开发可以将 views/index 中内容删除，在 router 中重新配置首页即可
 */
export const versionInfo = {
    springBootVersion: ' 3.3.10',
    vueVersion: ' 3.5.13',
    lihuaUpdateLog: [
        // 每次更新版本在集合头部新增一条
        {
            version: '1.2.2',
            updateDate: '2025-08-03',
            updateContent: [
                '1. 附件组件支持传入附件名称',
                '2. 修复简单属性选择组建设置默认全选后标签不回显的bug',
                '3. 后端新增允许跨域配置',
                '4. 更新axios版本到1.11.0',
                '5. 更新vite版本到6.3.4',
                '6. 移除jsx、vitest',
                '7. 更新ts版本到^5.9.2'
            ]
        },
        {
            version: '1.2.1',
            updateDate: '2025-04-12',
            updateContent: [
                '1. 修复日志如法正确处理ApiResponseModel的bug',
                '2. 修复附件组件无法正确回显的bug',
                '3. 修复岗位管理、部门管理去除手机号后保存报错的bug',
                '4. 优化table-setting组件',
                '5. 新增table-setting组件演示',
                '6. 新增table-setting组件文档',
            ]
        },
        {
            version: '1.2.0',
            updateDate: '2025-04-07',
            title: "1.2.0 版本支持knife4j接口文档，controller同一返回适配knife4j做出调整。前端新增【table-setting】组件，可自定义表格显示、顺序、宽度、固定。",
            updateContent: [
                '1. 新增集成【knife4j】接口文档',
                '2. 新增controller统一返回ApiResponse、ApiResponseController（兼容knife4j返回值），StrResponseController、StrResponse（knife4j返回值为string）',
                '3. 系统controller统一返回替换为ApiResponse、ApiResponseController',
                '4. 新增【table-setting】组件，可自定义表格显示、顺序、宽度、固定',
                '5. 新增在样式布局页面下，调整完主题后刷新页面触发保存',
                '6. 修复主题在不跟随系统的设置下，刷新页面依然与系统同步的bug',
                '7. tianai-captcha后端版本更新为1.5.2，点选验证码使用自定义字体',
                '8. tianai-captcha前端SDK更新为1.4版本',
                '9. 修复附件组件先加载组件时无法正确回显的bug',
                '10. 修复个人中心修改个人信息概率性输入框一直处于加载中状态的bug',
                '11. 为兼容【knife4j】spring boot 版本调整为3.3.10',
            ]
        },
        {
            version: '1.1.3',
            updateDate: '2025-03-15',
            updateContent: [
                '1. 主题设置新增跟随系统切换暗色/亮色模式',
                '2. 通知公告预览页面支持实时暗色/亮色切换',
                '3. 主题新增支持同步到小窗/其他标签页。亮色/暗色模式立即同步，其他主题在触发保存后同步',
                '4. TreeUtils工具类中增加反射缓存，减少递归中反射的调用',
                '5. 新增多数据源配置，使用dynamic-datasource实现，具体文档请参考：https://www.kancloud.cn/tracy5546/dynamic-datasource/2264611',
                '6. spring boot 版本升级至3.4.3，修复框架漏洞',
            ]
        },
        {
            version: '1.1.2',
            updateDate: '2025-03-11',
            updateContent: [
                '1. 新增数据脱敏相关注解：@Sensitive标记对应字段应用脱敏规则，实体类属性为对象是使用@DeepSensitive注解可解析对象中属性脱敏，使用@ApplySensitive应用数据脱敏',
                '2. 开发文档新增数据脱敏相关用法，地址为：http://doc.lihua.xyz/#/document/SERVER?id=数据脱敏',
                '3. 数据库脚本id字段由varchar修改为bigint',
                '4. vditor版本升级为3.10.9',
                '5. 其他细节优化调整',
            ]
        },
        {
            version: '1.1.1',
            updateDate: '2025-03-03',
            updateContent: [
                '1. 新增viewTab下可打开当前路由页面的小窗模式，不同模块间数据复制时使用 （仅在本地运行和https下可用，并限制浏览器版本）',
                '2. 修复生产环境附件导出调用接口错误导致附件无法下载的bug',
                '3. 优化分片上传附件合并，使用FileChannel零拷贝方式进行附件合并',
                '4. 系统限流key压缩切换为MD5实现',
                '5. 优化发布消息推送使用异步方式调用',
                '6. 修复附件上传中拖拽上传后附件管理查询数据异常的bug',
            ]
        },
        {
            version: '1.1.0',
            updateDate: '2025-02-21',
            title: "🎉🎉1.1.0 版本新增【附件管理】模块【attachment-upload】附件上传组件支持一般上传、分片上传、文件秒传、富文本编辑器支持粘贴URL自动上传，后端可通过配置文件切换附件服务，支持本地文件和MINIO（中途切换后附件需手动迁移），并预留接口可快速接入其他附件服务。",
            updateContent: [
                '1. 新增【附件管理】，在【系统管理】-【附件管理】支持查询、删除、下载、分享系统附件',
                '2. 新增【attachment-upload】附件上传，基于a-upload组件二次封装，支持一般上传、分片上传、文件秒传，支持按钮、图像预览、拖拽三种上传形式。',
                '3. 新增【附件上传】组件展示，展示部分attachment-upload效果',
                '4. 新增【AttachmentStorageStrategy】实现该接口后根据文档可快速扩展其他附件服务',
                '5. 新增菜单状态从停用切换到正常时，自动展开操作项',
                '6. 页面菜单操作栏固定到右边，当视口宽度小于menuToggleWidth中设置的值时取消固定（非响应式）',
                '7. 修复缓存监控中系统设置无法显示缓存内容的bug',
                '8. 修复通过时间范围筛选时不准的问题',
                '9. 优化日志清空倒计时实现方式',
                '10. 默认密码加密规则更换为ECB，无需VI向量',
                '11. 重构后端目录结构',
                '12. 其他细节优化调整',
            ]
        },
        {
            version: '1.0.3',
            updateDate: '2025-01-22',
            updateContent: [
                '1. 修复导航菜单1，顶部导航的header在table设置了固定列时被遮挡的问题',
                '2. redisCache工具类新增hash相关方法',
                '3. 系统设置缓存类型由list修改为hash，并优化相关代码',
                '4. 修改redisCache中list类型缓存设置逻辑',
                '5. 优化日志系统日志，由业务判断返回error情况下日志记录为执行失败',
            ]
        },
        {
            version: '1.0.2',
            updateDate: '2025-01-17',
            updateContent: [
                '1. sse新增重连次数限制，超出重连次数后关闭sse连接',
                '2. 个人中心页面入口通过卡片头像区域点击进入',
                '3. 修改个人中心、系统设置布局显示效果',
                '4. 新版个人中心，更加注重数据展示,同时可展示当前部门、岗位、角色信息',
                '5. 修复根节点下新建页面，路由重名的bug',
                '6. vue-router 版本升级到 4.5.0',
                '7. 修复用户管理导入用户是没有部门数据报错的bug',
                '8. 允许未分配角色的用户登录',
            ]
        },
        {
            version: '1.0.1',
            updateDate: '2025-01-07',
            updateContent: [
                '1. 优化自适应，在移动端有更好的体验',
                '2. 导航颜色选择/导航栏类型选中指示根据主题颜色变化',
                '3. 优化登陆后设置卡片，防止移动换滑动直接进入系统，同时仅存在一项配置，无法通过检擦元素显示后面内容，重写切换动画',
                '4. 修复JsonUtil调用toJsonIgnoreNulls时无法处理LocalDate/LocalDateTime的 bug',
                '5. 修复修改默认密码密钥后，删除默认密码数据也无法登录的bug',
                '如何修改密钥请参考：https://gitee.com/yukino_git/lihua/issues/IBFWG4#note_36173102_link',
            ]
        },
        {
            version: '1.0.0',
            updateDate: '2025-01-03',
            title: "2024年1月3日，狸花猫在 Gitee 创建了仓库。十个月后，上线了体验版。经过三个月的 bug 修复和功能优化，2025年1月3日，1.0.0 正式版上线🎉🎉🎉！未来将继续对功能进行维护和完善，感谢大家的 Star 和支持！",
            updateContent: [
                '1. 重构验证码类型，通过CaptchaTypeEnum进行统一维护',
                '2. 验证码图片替换为猫猫背景，并注释掉了【旋转】【拼接样式】，可在CaptchaTypeEnum中修改。自定义背景图片可在lihua-admin的resources下captcha-images文件夹修改',
                '3. 修复重置主题时背景虚化不生效的bug',
                '4. 修复移动端菜单logo颜色不同步的bug',
                '5. 修复header中通知公告触发tooltip后显示跳动的bug',
                '6. 对个人中心、系统设置、缓存监控进行响应式优化',
                '7. 修复linux中上传图片控制台报错的bug',
            ]
        },
        {
            version: '1.0.0-beta.18',
            updateDate: '2024-12-30',
            updateContent: [
                '1. 重构登录页面，将注册/登录业务逻辑分离，未来新增登录模式更具扩展性',
                '2. 注册完成后跳转到登录卡片自动带入用户名',
                '3. 修复侧边导航-风格1隐藏layout顶部不隐藏的bug',
                '4. 新增docker部署相关文件',
                '5. 其他细节优化调整',
            ]
        },
        {
            version: '1.0.0-beta.17',
            updateDate: '2024-12-22',
            updateContent: [
                '1. 新增组件展示页面',
                '2. 在线文档-内置组件上线',
                '3. 优化expandable-card组件在不传入middle插槽时使用detail过渡',
                '4. 新增expandable-card组件演示demo',
                '5. 静态路由中meta新增allowAnonymous是否允许在未登录的情况下访问路由属性，设置为true在未登录下也能访问',
                '6. 优化页面中layout中的tooltip组件绑定dom对象',
                '7. 修复dict-tag树形字典全路径无效的bug',
                '8. 调整优化代码结构',
                '9. spring boot版本升级为3.4.1',
                '10. xxl-job版本升级为2.4.2',
            ]
        },
        {
            version: '1.0.0-beta.16',
            updateDate: '2024-12-08',
            updateContent: [
                '1. 因spring boot 3.4.0兼容性问题，更换系统验证码实现，由aj替换为tianai',
                '2. 优化后端 获取登录用户信息、检查登录后设置、用户注册关联表保存 代码结构，进行功能扩展时添加对应接口的实现类即可',
                '3. spring boot版本更新到 3.4.0',
                '4. 其他细节优化调整'
            ]
        },
        {
            version: '1.0.0-beta.15',
            updateDate: '2024-12-01',
            updateContent: [
                '1. 优化mask组件隐藏滚动条',
                '2. 优化expandable-card组件更新小窗口滚动条，需插槽中只存在一个dom节点',
                '3. 修改顶部导航小窗口下layout显示逻辑，小窗口下将自动切换为【侧边导航-风格2】样式，窗口扩大后复原',
                '4. 顶部导航高度调整为48px，与其他布局header一致',
                '5. 前端Debounce.ts新增debounce（函数防抖）方法',
                '6. 前端新增easy-tree-select组件，更简单的处理树形结构选择',
                '7. 切换默认部门/角色菜单树/用户部门树/系统设置部门树 使用easy-tree-select实现，简化业务组件代码',
                '8. 修复【侧边导航-风格2】小窗口下出现滚动条的导航栏最后一个菜单点击不到的bug',
                '9. 其他bug修复',
                '10. vue版本升级为3.5.13',
                '11. spring boot版本升级为3.3.6',
            ]
        },
        {
            version: '1.0.0-beta.14',
            updateDate: '2024-11-17',
            updateContent: [
                '1. 调整flattenTree方法，并对调用的代码进行修改',
                '2. 调整遮罩组件的可设置z-index，修复了特定条件下没有覆盖住导航trigger的bug',
                '3. expandable-card组件支持详情插槽内添加多个元素（不推荐），当展开后视口高度不足时自动添加滚动条',
                '4. 优化小窗口下的layout布局',
                '5. 后端将数据库查询方法名中find前缀替换为query，前端将api获取数据函数名中find前缀换为query',
                '6. 后端JsonUtils新增deepCopy（对象深拷贝）、deepCopyList（集合深拷贝）方法',
                '7. 后端TreeUtils新增flattenTree（扁平化树）方法',
                '8. 前端Tree.ts新增buildTree（构建树）方法',
                '9. 更新myexcel版本4.5.6',
                '10. 更新ant-design-vue版本到4.2.6',
                '11. 更新vite版本到5.4.6'
            ]
        },
        {
            version: '1.0.0-beta.13',
            updateDate: '2024-11-10',
            updateContent: [
                '开发文档上线，地址：' + import.meta.env.VITE_APP_DOC_API + ' 目前尚未全部编写完成，后面会持续维护',
                '1. 优化卡片展示组件，移除cardKey属性，新增expandedHeight属性控制展开高度，支持根据视口自适应卡片显示大小及位置，通过minWindowSpace可指定视口小于卡片宽高时边缘间隙',
                '2. 在开启多任务栏的情况下，跳转路由新增支持query参数记忆。在业务中跳转路由携带query参数时，没有关闭对应tab页的情况下，再次切换到该路由，query参数不会丢失。对应issues：https://gitee.com/yukino_git/lihua/issues/IB37RW',
                '3. 视口缩小到设定阈值时，侧边菜单展开样式修改为抽屉形式，防止展开菜单后挤压内容变形',
                '4. 调整layout代码结构',
                '5. 未指定默认部门时，显示 设置默认部门 占位',
                '6. 登录页面布局优化，窗口缩小时登录表单居中',
                '7. 调整部门管理列表页查询条件布局',
                '8. 更新card-show组件名称为expandable-card，更新select-card名称为selectable-card',
                '9. 注册页面新增loading',
                '10. 更新spring boot到3.3.5版本',
                '11. 更新mybatis-plus到3.5.9版本'
            ]
        },
        {
            version: '1.0.0-beta.12',
            updateDate: '2024-10-27',
            updateContent: [
                '1. 优化图标选择组件，通过dom元素懒加载，大幅优化组件打开速度',
                '2. 修复用户选择组件刚加载时报错的bug',
                '3. 修复controller中直接使用Validated校验数据不返回自定义错误信息的bug',
                '4. 调整username校验规则，只允许出现大小写英文、数字、@、.',
                '5. 调整最大分页页码限制'
            ]
        },
        {
            version: '1.0.0-beta.11',
            updateDate: '2024-10-23',
            updateContent: [
                '1. 更新Vditor版本，修复代码高亮无效的bug',
                '2. 优化卡片展示组件，设置悬浮缩放倍率>1才出现悬浮效果',
                '3. 修复卡片悬浮组件，在展开动画开始前进行缩放，导致还原后卡片变大的bug',
                '4. 新增ResponseError,封装该类进行高效的catch类型判断',
                '5. 对系统中的异步调用进行重构，增加catch',
                '6. 升级mybatis-plus到3.5.8',
                '7. 记住我密码解密失败自动取消记住我'
            ]
        },
        {
            version: '1.0.0-beta.10',
            updateDate: '2024-10-20',
            updateContent: [
                '1. 用户通知卡片新增显示发布人昵称',
                '2. 修复查看消息通知一直是admin的bug',
                '3. 富文本编辑器初始化期间新增loading遮罩',
                '4. 获取当前时间修改为从工具类获取',
                '5. 提取获取默认密码到SettingService中',
                '6. excel导入添加后台判断上传文件格式',
                '7. axios响应拦截器新增处理nginx返回的错误',
                '8. 修復上传excel超过nginx限制后遮罩不关闭的bug',
            ]
        },
        {
            version: '1.0.0-beta.9',
            updateDate: '2024-10-15',
            updateContent: [
                '1. 修复批量插入时可能会出现的事务问题（修改用户@burningimlam）',
                '2. 优化了部分代码结构',
                '3. 升级spring boot版本为 3.3.4',
                '4. 修复静态路由刷新页面不好使的bug',
                '5. 当菜单未收起并且不为顶部导航时，菜单会自动展开',
                '6. 新增窗口宽度减少到一定阈值时，侧边栏完全隐藏',
            ]
        },
        {
            version: '1.0.0-beta.8',
            updateDate: '2024-10-14',
            updateContent: [
                '1. 优化新增用户用户名/手机号码/邮箱判重逻辑（修改用户@Yang）',
                '2. 优化页面表格，页面宽度缩小后显示滚动条',
                '3. 多任务栏右键新增刷新页面',
                '4. 多任务栏右键新增新页打开',
                '5. 限制组件缓存最大5个',
                '6. axios更新到1.7.4',
            ]
        },
        {
            version: '1.0.0-beta.7',
            updateDate: '2024-10-13',
            updateContent: [
                '1. 修复分页查询不传入分页参数空指针的bug（修改用户@burningimlam）',
                '2. 新增对分页参数大小的限制（修改用户@burningimlam）',
                '3. 新增在个人中心-样式布局下，开启隐藏Layout时，多任务栏开关隐藏',
                '4. 优化侧边导航栏修改暗色/浅色模式滚动条样式变化',
                '5. 顶部导航模式宽度增加到64px，同时页面宽度不足时出现滚动条',
                '6. 系统设置中权限不足时重置switch',
                '7. 添加注册时的验证码出现消失动画',
                '8. 用户注册密码限制长度6-30',
            ]
        },
        {
            version: '1.0.0-beta.6',
            updateDate: '2024-10-11',
            updateContent: [
                '1. 优化全局滚动条，跟随暗色模式改变颜色',
                '2. 修复登陆后配置信息发生异常时，可直接进入主页面的bug',
                '3. 整理了前端开发文档大致目录'
            ]
        },
        {
            version: '1.0.0-beta.5',
            updateDate: '2024-10-10',
            updateContent: [
                '更新SQL脚本文件，数据库中默认密码兼容数据加密'
            ]
        },
        {
            version: '1.0.0-beta.4',
            updateDate: '2024-10-08',
            updateContent: [
                '1. 用户登录、注册、修改密码、重置密码、管理员新增用户需要处理密码的场景对密码新增使用RAS非对称加密',
                '2. 管理员配置默认密码新增使用AES对称加密',
                '3. 系统设置提交按钮新增loading加载',
                '4. 更新redisTemplate序列化器，使用Jackson2JsonRedisSerializer进行序列化',
                '5. 缓存存入redis中忽略null值',
                '6. 修复登录用户ip地址始终为127.0.0.1的bug',
                '7. 修复表单自定义校验控制台报异常的问题',
                '8. 修复注册日志系统日志不记录的bug',
                '9. 修复操作日志排除参数失效的bug',
                '10. 优化操作日志参数记录，对象中null值不进行记录',
                '11. 记住账号修改默认保存30天',
            ]
        },
        {
            version: '1.0.0-beta.3',
            updateDate: '2024-10-06',
            updateContent: [
                '1. 修复登录操作日志生产环境获取ip地址始终为127.0.0.1的bug',
                '2. 修复用户选择组件切换不同部门时，用户丢失的bug',
                '3. 新增sse 心跳机制，防止nginx作用下SSE断连',
                '4. 树形选择父子关联默认设置为false',
                '5. 定时任务yml配置优化',
            ]
        },
        {
            version: '1.0.0-beta.2',
            updateDate: '2024-10-05',
            title: '狸花猫后台管理系统beta版正式上线🎉🎉🎉',
            updateContent: [
                '1. 基于角色的权限管理',
                '2. 部门岗位功能，适用于大部分业务细分权限',
                '3. 管理员系统设置，根据需求进行多种配置修改',
                '4. 后端封装SpringSecurity提供的用户上下文，可轻松获取登录用户所有信息',
                '5. 后端封装多种使用工具类：轻松构建树形结构、快速翻译字典、支持字典翻译的Excel导出等',
                '6. 前端提供丰富的封装组件，提高系统颜值，提升开发效率',
                '7. 前端支持完整的暗色模式',
                '8. 前端支持自定义主题样式风格',
                '9. 更多内容欢迎下载体验',
            ]
        },
        {
            version: '1.0.0-beta.1',
            updateDate: '2024-10-05',
            updateContent: [
                '发布后有bug又删了'
            ]
        }
    ]
}