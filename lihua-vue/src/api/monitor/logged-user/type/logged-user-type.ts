export interface LoggedUserType {
    cacheKey: string;

    username: string;

    nickname: string;

    ip: string;

    loginTime: Date;

    clientType: string;
}


export interface LoggedUserQueryParams {
    username?: string
    nickname?: string
    clientType?: string
}