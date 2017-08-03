package com.zhb.sinaspider.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.zhb.sinaspider.service.ILoginSinaMicroBlogService;
import com.zhb.sinaspider.utils.EncodeUtil;
import com.zhb.sinaspider.utils.FastJSonUtil;

/**
 * @author zhanghaobo
 * @date 2017年7月29日下午7:51:03
 * @todo TODO
 */
public class LoginSinaMicroBlogServiceImpl implements ILoginSinaMicroBlogService {

	// 登录账号和密码
	private String username;
	private String pwd ;


	public String getSubCookie() throws Exception {
		Map<String, String> cookies = login(this.getUsername(), this.getPwd());
		String cookieSUB = cookies.get("SUB");
		return cookieSUB;
	}

	/**
	 * WAP版登录
	 * 
	 * @param username
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private Map<String, String> login(String username, String pwd) throws Exception {
		String url = "https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9";
		// &_=" + System.currentTimeMillis();
		String content = preloginWap(username);
		Map<String, Object> paramMap = FastJSonUtil.toMap(content);

		String pubkey = paramMap.get("pubkey").toString();
		String servertime = paramMap.get("servertime").toString();
		String nonce = paramMap.get("nonce").toString();
		String rsakv = paramMap.get("rsakv").toString();

		Map<String, String> params = new HashMap<String, String>();

		params.put("username", username);
		params.put("password", pwd);
		params.put("savestate", "1");
		params.put("r", "http://weibo.cn");
		params.put("ec", "0");
		params.put("pagerefer", "");
		params.put("entry", "mweibo");
		params.put("wentry", "");
		params.put("loginfrom", "");
		params.put("client_id", "");
		params.put("code", "");
		params.put("qq", "");
		params.put("mainpageflag", "1");
		params.put("hff", "");
		params.put("hfp", "");

		Map<String, String> cookie = null;
		Response response = Jsoup.connect("https://passport.weibo.cn/sso/login").data(params)
				.header("Host", "passport.weibo.cn").header("Origin", "https://passport.weibo.cn")
				.header("Accept-Encoding", "gzip, deflate, br")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				.header("Referer",
						"https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=")
				.header("Connection", "keep-alive")
				// .header("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64;
				// x64) AppleWebKit/537.36 (KHTML, like Gecko)
				// Chrome/59.0.3071.115 Safari/537.36")
				.method(Method.POST).execute();
		cookie = response.cookies();
		return cookie;
	}

	/**
	 * WAP版预登陆
	 * 
	 * @return JSON数据
	 */
	private String preloginWap(String username) {
		long currentTimeMillis = System.currentTimeMillis();
		String url = "https://login.sina.com.cn/sso/prelogin.php?checkpin=1&entry=mweibo&su="
				+ EncodeUtil.username2Base64(username) + "&callback=jsonpcallback" + currentTimeMillis;
		Document document;
		try {
			document = Jsoup.connect(url).ignoreContentType(true).get();
			Element element = document.body();
			String content = element.text();
			if (null != content && !content.equals("")) {
				content = content.replaceAll("jsonpcallback" + currentTimeMillis + "\\((.*)\\)", "$1");
			}
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
