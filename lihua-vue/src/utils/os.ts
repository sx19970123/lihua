export const osType = (): 'Windows' | 'MacOS' | 'Linux' | 'Android' | 'iOS' | 'Unknown' => {
    const userAgent = window.navigator.userAgent.toLowerCase();

    if (userAgent.indexOf('win') !== -1) return 'Windows';
    if (userAgent.indexOf('mac') !== -1) return 'MacOS';
    if (userAgent.indexOf('x11') !== -1 || userAgent.indexOf('linux') !== -1)
        return 'Linux';
    if (/android/.test(userAgent)) return 'Android';
    if (/iphone|ipad|ipod/.test(userAgent)) return 'iOS';
    return 'Unknown';
}