/**
 * 测量文本宽度
 */
export const measureTextWidth = (text: string, fontSize = 16, fontFamily = "sans-serif") => {
  const ctx = uni.createCanvasContext("textMeasureCanvas")
  ctx.setFontSize(fontSize)
  ctx.font = `${fontSize}px ${fontFamily}`
  return ctx.measureText(text).width
}

