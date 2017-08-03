package com.zhb.sinaspider.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.zhb.sinaspider.service.ICrawlerService;
import com.zhb.sinaspider.service.ILoginSinaMicroBlogService;
import com.zhb.sinaspider.utils.FileUtil;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;

/**
 * @author zhanghaobo
 * @date 2017年7月30日上午8:41:42
 * @todo TODO
 */
public class CrawlerByUserIDImpl implements ICrawlerService {
	
	private ILoginSinaMicroBlogService loginSinaMicroBlogService;
	
	
	public void crawlerByUserId(String UserId, Page page, CrawlDatums next) throws Exception {

		//if (page.matchUrl("https://weibo.cn/" + UserId + ".*")) 
		if (page.matchUrl("https://weibo.cn/.*")){
			if (page.matchUrl("https://weibo.cn/[a-zA-Z0-9]+/info|https://weibo.cn/u/\\d+/info")) {
				// 获取新浪ID
				// String sinaid = ;
				// 获取微博昵称
				Element preBasicInfo = page.select("div.tip").get(1);
				Element basicInfo = preBasicInfo.nextElementSibling();
				// System.out.println(basicInfo.text());
				String sinausername = basicInfo.textNodes().get(0).text().substring(3,
						basicInfo.textNodes().get(0).text().length());			
				// 获取微博个人描述
				String sinadescription = "";
				List<TextNode> textNodes = basicInfo.textNodes();
				for (TextNode textNode : textNodes) {
					if (textNode.text().contains("简介:")) {
						sinadescription = textNode.text().substring(3, textNode.text().length());
					}
				}				
			}
			FileOutputStream fos = FileUtil.getOutputStream("/Users/jsj/eclipse-workspace/sinaspider/sinatext.txt");
			// Weibo weibo = new Weibo();
			Elements eles = page.select("div[id].c");
			// Elements eles = page.select("div.c");
			for (Element element : eles) {

				// 取得微博的id 比如M_F9b9Vz21r
				String weiboid = element.attr("id");
				// System.out.println(weiboid);
				String weibourlString = "";
				// 获取微博的正文
				// 判断是否有全文链接。如果有表示正文未显示完全。
				String quanwen = element.select("div span.ctt a[href^=/comment/]").text().trim();
				// System.out.println(quanwen);
				String weibocontent = "";
				if (quanwen.equals("全文")) {
					// 处理新的链接
					String quanwenurl = element.select("div span.ctt a[href^=/comment/]").attr("abs:href");
					try {
						Document document = Jsoup.connect(quanwenurl).header("Host", "weibo.cn").header("accept",
								"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
								// .header("Origin",
								// "https://passport.weibo.cn")
								.header("Accept-Encoding", "gzip, deflate, br")
								// .header("Content-Type",
								// "application/x-www-form-urlencoded")
								.header("User-Agent",
										"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
								// .header("Referer" ,
								// "https://weibo.cn/zhangyumaths")
								.header("Connection", "keep-alive").header("Upgrade-Insecure-Requests", "1")
								.cookie("SUB", loginSinaMicroBlogService.getSubCookie())
								//.cookie("SUHB", cookieSUHB)
								.get();

						weibocontent = document.select("div#M_").text();						
					} catch (IOException e) {
						e.printStackTrace();
					}

					// System.out.println(quanwenurl);
					// weibocontent ="包含全文！！！";
				} else {
					weibocontent = element.select("div span.ctt").text();
				}
				try {
					fos.write((weibocontent + "\r\n\r\n").getBytes());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// System.out.println(weibocontent);
				// 获取微博图片的url地址
				String weibotupiansrc = element.select("div a img").attr("src");
				// System.out.println(weibotupiansrc);
				// 取得微博的发布时间和方式信息
				String weibofabushijian = element.select("span.ct").text();
				// 取得微博转发数
				String weibozhuanfashu = element.select("div a[href^=https://weibo.cn/repost/]").text();
				// System.out.println(weibozhuanfashu);
				// 取得微博评论数
				String weibopinglunshu = element.child(element.childNodeSize() - 1)
						.select("a[href^=https://weibo.cn/comment/]").text();
				// System.out.println(weibopinglunshu);
				// 下面这个语句得到的是原文评论数和转发评论数。
				// String weibopinglunshu = element.select("div
				// a[href^=https://weibo.cn/comment/]").text();

				// 评论的网页URL
				String commentURL = element.child(element.childNodeSize() - 1)
						.select("a[href^=https://weibo.cn/comment/]").attr("href");
				//System.out.println(commentURL);
				// 抓取微博评论
				try {
					Document document1 = Jsoup.connect(commentURL).header("Host", "weibo.cn")
							// .header("Origin", "https://passport.weibo.cn")
							.header("Accept-Encoding", "gzip, deflate, br")
							.header("Content-Type", "application/x-www-form-urlencoded")
							.header("User-Agent",
									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
							.header("Referer", "https://weibo.cn/zhangyumaths").header("Connection", "keep-alive")
							.cookie("SUB", "cookie").get();

					Elements select = document1.select("div.c[id^=C_]");

					for (Element element2 : select) {
						fos.write((element2.text() + "\r\n").getBytes());

						// System.out.println(element2.text());
					}
					fos.write("####################################################\r\n".getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			System.out.println("结束爬取！！！");
		}
	}

	public void crawlerByTopic(String topicName, Page page, CrawlDatums next) {

	}

	public ILoginSinaMicroBlogService getLoginSinaMicroBlogService() {
		return loginSinaMicroBlogService;
	}

	public void setLoginSinaMicroBlogService(ILoginSinaMicroBlogService loginSinaMicroBlogService) {
		this.loginSinaMicroBlogService = loginSinaMicroBlogService;
	}

}
