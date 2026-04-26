// #ifdef APP
import { v4 as uuid } from 'uuid';
// #endif

// #ifndef APP
import uuidv4 from "@/utils/uuid/lib/uuidv4";
// #endif

/**
 * 获取uuid，兼容app和微信小程序
 */
export const getUUID = async () => {
	// #ifdef APP
		return uuid()
	// #endif
	
	// #ifndef APP
		return await uuidv4()
	// #endif
}