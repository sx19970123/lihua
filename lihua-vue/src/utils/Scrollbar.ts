let isOverflowHidden = false;

// 隐藏滚动条
export const hiddenOverflowY = () => {
    if (isOverflowHidden) return;
    if (hasScrollbar()) {
        const scrollbarWidth = getScrollbarWidth();
        document.body.style.overflowY = 'hidden';
        document.body.style.width = `calc(100% - ${scrollbarWidth}px)`;
        isOverflowHidden = true;
    }
};

// 显示滚动条
export const showOverflowY = () => {
    if (!isOverflowHidden) return;
    document.body.style.overflowY = '';
    document.body.style.removeProperty('width');
    isOverflowHidden = false;
};

// 判断是否出现滚动条
export const hasScrollbar = (): boolean => {
    return document.documentElement.scrollHeight > window.innerHeight;
};

// 获取滚动条宽度
const getScrollbarWidth = (): number => {
    const div = document.createElement('div');
    div.style.position = 'absolute';
    div.style.top = '-9999px';
    div.style.width = '100px';
    div.style.height = '100px';
    div.style.overflow = 'scroll';
    document.body.appendChild(div);
    const scrollbarWidth = div.offsetWidth - div.clientWidth;
    document.body.removeChild(div);
    return scrollbarWidth;
};