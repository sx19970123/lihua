/**
 * 将时间字符串或时间戳处理为今天/昨天/前天格式
 * @param time 传入时间，可以是字符串或时间戳
 * @returns 格式化后的时间
 */
export const handleTime = (time?: string | number | Date) => {
  if (!time) return ''

  const date = new Date(time)
  if (isNaN(date.getTime())) return ''

  const now = new Date()
  
  // 将日期部分格式化为 YYYY-MM-DD
  const formatDate = (d: Date) => {
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }

  const targetDate = formatDate(date)
  const todayDate = formatDate(now)

  const yesterday = new Date(now)
  yesterday.setDate(now.getDate() - 1)
  const yesterdayDate = formatDate(yesterday)

  const dayBeforeYesterday = new Date(now)
  dayBeforeYesterday.setDate(now.getDate() - 2)
  const dayBeforeYesterdayDate = formatDate(dayBeforeYesterday)

  // 获取时间部分 HH:mm
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const timePart = `${hours}:${minutes}`

  if (targetDate === todayDate) return `今天 ${timePart}`
  if (targetDate === yesterdayDate) return `昨天 ${timePart}`
  if (targetDate === dayBeforeYesterdayDate) return `前天 ${timePart}`

  // 默认返回完整日期时间
  return `${targetDate} ${timePart}`
}
