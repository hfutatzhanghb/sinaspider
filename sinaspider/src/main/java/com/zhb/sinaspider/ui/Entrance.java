package com.zhb.sinaspider.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhb.sinaspider.service.ICrawlerService;
import com.zhb.sinaspider.service.ILoginSinaMicroBlogService;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * @author zhanghaobo
 * @date 2017年7月29日下午8:06:57
 */
public class Entrance extends BreadthCrawler {

	private ILoginSinaMicroBlogService loginService ;
	private ICrawlerService crawlerService ;

	private String seed;

	public Entrance(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Entrance entrance = (Entrance) context.getBean("entrance");
		// 添加url种子
		//entrance.addSeed(entrance.getSeed());
		entrance.addSeed(new CrawlDatum(entrance.getSeed()));	
		// 爬取微博正文界面
		entrance.addRegex("https://weibo.cn/"+entrance.getSeed().substring(entrance.getSeed().lastIndexOf("/")+1)+"\\?page=[0-9]*");
		//entrance.addRegex("https://weibo.cn/zhangyumaths\\?page=[0-9]+");
		// 爬取个人信息界面
		entrance.addRegex("https://weibo.cn/[0-9]+/info");
		// 去除第一页的重复
	//	entrance.addRegex("-https://weibo.cn/zhangyumaths\\?page=1");
		entrance.addRegex("-https://weibo.cn/"+entrance.getSeed().substring(entrance.getSeed().lastIndexOf("/")+1)+"\\?page=1");
		entrance.setThreads(1);		
		entrance.start(3);		

	}

	// 对获取到的界面按个人需求进行处理
	public void visit(Page page, CrawlDatums next) {
		try {
			crawlerService.crawlerByUserId(this.getSeed().substring(this.getSeed().lastIndexOf("/")+1), page, next);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 模拟保持登录
	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpRequest request = new HttpRequest(crawlDatum);
		request.setCookie("SUB=" + loginService.getSubCookie());
		return request.response();
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public ILoginSinaMicroBlogService getLoginService() {
		return loginService;
	}

	public void setLoginService(ILoginSinaMicroBlogService loginService) {
		this.loginService = loginService;
	}

	public ICrawlerService getCrawlerService() {
		return crawlerService;
	}

	public void setCrawlerService(ICrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

}