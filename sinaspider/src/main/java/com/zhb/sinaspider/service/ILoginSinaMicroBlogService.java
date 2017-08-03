package com.zhb.sinaspider.service;

/**
 *@author  zhanghaobo
 *@date    2017年7月29日下午7:50:43
 *@todo    TODO
*/


/**
 *登录接口
 */
public interface ILoginSinaMicroBlogService {	

	/**
	 * 得到新浪微博的cookie中的SUB的值
	 * @param username  新浪微博登录名
	 * @param pwd		新浪微博登陆密码
	 * @return			cookie的SUB值
	 * @throws Exception 
	 */
	public String getSubCookie() throws Exception;
}
