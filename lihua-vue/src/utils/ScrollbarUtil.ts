// 隐藏滚动条
export const hiddenOverflowY = () => {
    if (hasScrollbar()) {
        document.body.style.overflowY = 'hidden'
        // body滚动条默认宽度15px
        document.body.style.width = 'calc(100% - 15px)'
    }
}

// 显示滚动条
export const showOverflowY = () => {
    document.body.style.overflowY = ''
    document.body.style.removeProperty('width')
}

// 判断是否出现了滚动条
export const hasScrollbar = (): boolean => {
    // 获取页面的总高度和视口的高度
    const body = document.body;
    const html = document.documentElement;

    const scrollHeight = Math.max(body.scrollHeight, body.offsetHeight,
        html.clientHeight, html.scrollHeight, html.offsetHeight);
    const clientHeight = window.innerHeight;

    // 判断是否出现滚动条
    return scrollHeight > clientHeight;
}

