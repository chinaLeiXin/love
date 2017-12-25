package cn.leixin.controller;

import cn.leixin.dao.PostData;
import cn.leixin.dao.Url;
import cn.leixin.service.UrlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping
public class IndexController {
	@Autowired
	UrlService urlService;
	@Autowired
	RedisTemplate template;
	@RequestMapping("/index")
	public String list(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		template.opsForList().leftPush(time,"地址"+ip+"  时间: "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
		return "index";
	}

	@RequestMapping("/play")
	public String play(String url, Model model){
		model.addAttribute("url",url);
		return "play";
	}

	@RequestMapping(value = "/selectall")
	@ResponseBody
	public PageInfo selectall(@RequestBody PostData postData) {
		PageHelper.startPage(postData.getPageIndex(),postData.getPageSize());
		if (postData.getName()!=""){
			Url url = new Url();
			url.setName(postData.getName());
			List<Url> selectname = urlService.selectname(url);
			PageInfo p = new PageInfo(selectname);
			return p;
		}
		List<Url> selectall = urlService.selectall();
		PageInfo p = new PageInfo(selectall);
		return p;
	}

	@RequestMapping(value = "/avi")
    @ResponseBody
	public String avi() throws Exception {
		if (template.opsForValue().size("currentPage")>0) {
			for (int i = (Integer) template.opsForValue().get("currentPage"); i < 800; i++) {
				if (i == 1) {
					String url = "http://www.sezyz1.com/vod-show-p.html";
					//正在抓取的页面
					template.opsForValue().set("currentPage",i);
					template.opsForValue().set("error",true);
					urlService.getURLInfo(url, "utf-8",i);
					//是否中断了
					template.opsForValue().set("error",false);
					continue;
				}
				String url = "http://www.sezyz1.com/vod-show-p-" + i + ".html";
				template.opsForValue().set("currentPage",i);
				template.opsForValue().set("error",true);
				urlService.getURLInfo(url, "utf-8",i);
				template.opsForValue().set("error",false);
			}
			return "获取链接完毕";
		}else {
			template.opsForValue().set("currentPage",1);
			return "获取失败,redis页面不存在,请再次抓取";
		}
	}

	@RequestMapping(value = "/redis")
	@ResponseBody
	public String redis() {
		Integer page = (Integer) template.opsForValue().get("page");
		String pagenumber = (String) template.opsForValue().get("pagenumber");
		return page+"页:"+pagenumber;
	}

	@RequestMapping(value = "/ip")
	@ResponseBody
	public List ip(String time) {
		List range = template.opsForList().range(time, 0, -1);
		return range;
	}
}