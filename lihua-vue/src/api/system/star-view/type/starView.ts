export interface StarViewType {
    affix: boolean,
    icon: string | null,
    label: string,
    routerPathKey: string,
    star: boolean | null,
    static?: boolean | null,
    skip?: boolean,
    query: string
}

export interface RecentType {
    openTime: string,
    icon: string | null,
    label: string,
    path?: string | null
}

