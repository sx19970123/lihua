interface StarViewType {
    affix: boolean,
    icon: string | null,
    label: string,
    routerPathKey?: string | null,
    star: boolean | null,
    static?: boolean | null
}

interface RecentType {
    openTime: string,
    icon: string,
    label: string,
    path: string
}

export {
    StarViewType,
    RecentType
}
