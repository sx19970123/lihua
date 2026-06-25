
type WxRandomValuesResult = {
    randomValues: ArrayBufferLike
}

declare const wx: {
    getRandomValues(options: {
        length: number,
        success: (res: WxRandomValuesResult) => void,
        fail: () => void
    }): void
}

export default function rng(): Promise<Uint8Array> {
    return new Promise((resolve) => {
        wx.getRandomValues({
            length: 16,
            success: (res: WxRandomValuesResult) => {
                resolve(new Uint8Array(res.randomValues));
            },
            fail: () => {
                throw new Error("Failed to get random values");
            }
        })
    });
}
