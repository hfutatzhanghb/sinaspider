package com.zhb.sinaspider.service;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

/**
 * @author zhanghaobo
 * @date 2017年7月30日上午8:11:52
 * @todo TODO
 */
public interface ICrawlerService {

	/**
	 * 通过用户名的方式抓取新浪微博内容和评论
	 * 
	 * @param UserId
	 *            用户ID有两种形式：例如：zhangyumaths 和/u/2135145213
	 * @throws Exception 
	 */
	public void crawlerByUserId(String userId,Page page, CrawlDatums next) throws Exception;

	/**
	 * 通过搜索话题的方式抓取新浪微博内容和评论
	 * 
	 * @param topicName
	 *            话题名称。 例如：人工智能
	 */
	public void crawlerByTopic(String topocName,Page page, CrawlDatums next);
}
