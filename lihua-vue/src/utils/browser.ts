// 获取浏览器类型
export const getBrowserType = () => {
    const { browserName} = getBrowserInfo()
    return browserName
}
// 获取浏览器完整版本号
export const getBrowserVersion = () => {
    const { fullVersion} = getBrowserInfo()
    return fullVersion
}
// 获取浏览器大版本号
export const getBrowserMajorVersion = () => {
    const { majorVersion} = getBrowserInfo()
    return majorVersion
}

const getBrowserInfo = () => {
    const userAgent = navigator.userAgent;
    let browserName = '';
    let fullVersion = '';
    let majorVersion;

    // 扩展 Document 接口来包含 documentMode
    interface DocumentWithMode extends Document {
        documentMode?: number;
    }

    const doc = document as DocumentWithMode;

    if (doc.documentMode) { // Internet Explorer
        browserName = 'IE';
        fullVersion = userAgent.substring(userAgent.indexOf('MSIE') + 5);
        if (fullVersion.indexOf(';') !== -1) {
            fullVersion = fullVersion.substring(0, fullVersion.indexOf(';'));
        }
    } else if (userAgent.indexOf('Chrome') !== -1) {
        browserName = 'Chrome';
        fullVersion = userAgent.substring(userAgent.indexOf('Chrome') + 7);
    } else if (userAgent.indexOf('Firefox') !== -1) {
        browserName = 'Firefox';
        fullVersion = userAgent.substring(userAgent.indexOf('Firefox') + 8);
    } else if (userAgent.indexOf('Safari') !== -1) {
        browserName = 'Safari';
        fullVersion = userAgent.substring(userAgent.indexOf('Version') + 8);
        if (userAgent.indexOf('CriOS') !== -1) {
            browserName = 'Chrome on iOS';
            fullVersion = userAgent.substring(userAgent.indexOf('CriOS') + 6);
        }
    } else if ((userAgent.indexOf('Opera') !== -1) || (userAgent.indexOf('OPR') !== -1)) {
        browserName = 'Opera';
        fullVersion = userAgent.substring(userAgent.indexOf('Opera') + 6);
        if (userAgent.indexOf('OPR') !== -1) {
            fullVersion = userAgent.substring(userAgent.indexOf('OPR') + 4);
        }
    }

    if ((fullVersion.indexOf(';') !== -1)) {
        fullVersion = fullVersion.substring(0, fullVersion.indexOf(';'));
    }
    if ((fullVersion.indexOf(' ') !== -1)) {
        fullVersion = fullVersion.substring(0, fullVersion.indexOf(' '));
    }
    majorVersion = parseInt('' + fullVersion, 10);
    if (isNaN(majorVersion)) {
        fullVersion = '' + parseFloat(navigator.appVersion);
        majorVersion = parseInt(navigator.appVersion, 10);
    }

    return {
        browserName: browserName,
        fullVersion: fullVersion,
        majorVersion: majorVersion,
    };
}