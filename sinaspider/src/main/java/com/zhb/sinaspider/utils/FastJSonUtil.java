package com.zhb.sinaspider.utils;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @author zhanghaobo
 * @date 2017年7月29日下午7:39:37
 * @todo TODO
 */

/**
 * FastJSon的工具类
 * @author zhanghaobo
 *
 */
public class FastJSonUtil {

	/**
	 * 将Map类型的字符串转换为Map
	 * @param text 需要转换为Map的字符串
	 * @return	 Map对象
	 */
	public static Map<String, Object> toMap(String text) {
		Map<String, Object> map = JSON.parseObject(
				text, new TypeReference<Map<String, Object>>(){} );
		return map;
	}
}
