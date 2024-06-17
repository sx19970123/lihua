export interface StarViewType {
    affix: boolean,
    icon: string | null,
    label: string,
    routerPathKey: string,
    star?: boolean | null,
    static?: boolean | null,
    viewTab?: boolean,
    query?: string,
    menuId?: string,
    menuType?: 'directory' | 'page' | 'link',
    linkOpenType?: 'inner' | 'new-page',
    link?: string,
    viewTabSort?: number
}

export interface RecentType {
    openTime: string,
    icon: string | null,
    label: string,
    path?: string | null
}

