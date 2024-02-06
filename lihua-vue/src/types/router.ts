interface RouterType {
    children: Array<RouterType>,
    component: Function | string,
    id: string,
    key: string,
    meta: MetaType,
    name: string,
    parentId: string,
    path: string,
    perms: string,
    query: string | null,
    type: string
}

interface MetaType {
    icon: string,
    label: string,
    link: string | null,
    noCache: boolean | null,
    title: string
}

export {
    RouterType,
    MetaType
}
