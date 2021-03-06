package com.javalec.kakaoBot;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.introspect.Annotated;
//add
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document; 
import org.jsoup.nodes.Element; 
import org.jsoup.select.Elements;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String keyboard() {
		System.out.println("키보드");
		JSONObject jsonObject = new JSONObject();
		ArrayList<String> btn = new ArrayList();
		
		btn.add("최근 1주일 공지사항 모두 모아보기");
		//btn.add("최근 1달 학사공지 모아보기");
		//btn.add("최근 1주일 일반소식 모아보기");
		
		jsonObject.put("type", "buttons");
		jsonObject.put("buttons",btn);
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST, headers = "Accept=application/json",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String message(@RequestBody JSONObject responseObject) throws IOException {
		System.out.println("메시지 전송");
		System.out.println(responseObject.toJSONString());
		
		String content = (String)responseObject.get("content");
		JSONObject jsonText = new JSONObject();
		JSONObject jsonBtn = new JSONObject();
		//JSONObject jsonInnerBtn = new JSONObject();
		JSONObject jsonReturn = new JSONObject();
		
		ArrayList<String> btn = new ArrayList();
		btn.add("최근 1주일 공지사항 모두 모아보기");
		//btn.add("최근 1달 학사공지 모아보기");
		//btn.add("최근 1주일 일반소식 모아보기");
		
		jsonBtn.put("type", "buttons");
		jsonBtn.put("buttons",btn);
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		String today = formatter.format(date);
		
		int cnt=0;
		String response="";
		/*
		if(content.contains("최근 1달 학사 모아보기")) {
			String page="notice";
			ArrayList<Elements> ele = getTitles(page);
			
			Elements titles = ele.get(0);
			Elements time = ele.get(1);
			
			response="★학사★\n";
			for(int i=0; i<titles.size(); i++) {
				if(doDiffOfDate(time.get(i).text(),today,30)) {
					cnt++;
					response+="\n["+time.get(i).text()+"]\n:"+titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="※학사가 없습니다.";}
			cnt=0;
			
			
		}
		
		if(content.contains("최근 1주일 일반소식 모아보기")) {
			String page="gnotice";
			ArrayList<Elements> ele = getTitles(page);
			
			Elements titles = ele.get(0);
			Elements time = ele.get(1);
			
			response="★일반소식★\n";
			for(int i=0; i<titles.size(); i++) {
				if(doDiffOfDate(time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+time.get(i).text()+"]\n:"+titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="※일반소식이 없습니다.";}
			cnt=0;
			
		}
		*/
		if(content.contains("최근 1주일 공지사항 모두 모아보기")) {
			String notice_page ="notice";
			String gnotice_page ="gnotice";
			String saccord_page ="saccord";
			String job_page ="job";
			String event_page ="event";
						
			ArrayList<Elements> notice_ele = getTitles(notice_page);
			ArrayList<Elements> gnotice_ele = getTitles(gnotice_page);
			ArrayList<Elements> saccord_ele = getTitles(saccord_page);
			ArrayList<Elements> job_ele = getTitles(job_page);
			ArrayList<Elements> event_ele = getTitles(event_page);
			
			Elements notice_titles = notice_ele.get(0);
			Elements notice_time = notice_ele.get(1);
			
			Elements gnotice_titles = gnotice_ele.get(0);
			Elements gnotice_time = gnotice_ele.get(1);
			
			Elements saccord_titles = saccord_ele.get(0);
			Elements saccord_time = saccord_ele.get(1);
			
			Elements job_titles = job_ele.get(0);
			Elements job_time = job_ele.get(1);
			
			Elements event_titles = event_ele.get(0);
			Elements event_time = event_ele.get(1);
			
			
			response="★학사공지★\n";
			for(int i=0; i<notice_titles.size(); i++) {
				if(doDiffOfDate(notice_time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+notice_time.get(i).text()+"]\n:"+notice_titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="\n※학사공지가 없습니다.\n";}
			cnt=0;
			
			response +="\n★일반소식★";
			for(int i=0; i<gnotice_titles.size(); i++) {
				if(doDiffOfDate(gnotice_time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+gnotice_time.get(i).text()+"]\n:"+gnotice_titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="\n※일반소식이 없습니다.\n";}
			cnt=0;
			
			response +="\n★사업단소식★";
			for(int i=0; i<saccord_titles.size(); i++) {
				if(doDiffOfDate(saccord_time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+saccord_time.get(i).text()+"]\n:"+saccord_titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="\n※사업단소식이 없습니다.\n";}
			cnt=0;
			
			response +="\n★취업정보★";
			for(int i=0; i<job_titles.size(); i++) {
				if(doDiffOfDate(job_time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+job_time.get(i).text()+"]\n:"+job_titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="\n※취업정보가 없습니다.\n";}
			cnt=0;
			
			response +="\n★CSE News★";
			for(int i=0; i<event_titles.size(); i++) {
				if(doDiffOfDate(event_time.get(i).text(),today,7)) {
					cnt++;
					response+="\n["+event_time.get(i).text()+"]\n:"+event_titles.get(i).text()+"\n";
				}
			}
			if(cnt==0) {response+="\n※CSE News가 없습니다.";}
			cnt=0;
				
		}
		
		jsonText.put("text", response);
		jsonReturn.put("message", jsonText);
		jsonReturn.put("keyboard", jsonBtn);
		
		return jsonReturn.toJSONString();
	}
	public ArrayList getTitles(String page) throws IOException {
		Document doc = Jsoup.connect("http://computer.cnu.ac.kr/index.php?mid="+page).get();
		Element table = doc.select("table[class=bd_lst bd_tb_lst bd_tb] tbody").first();
		Elements titles = table.select( "a" );
		Elements times = doc.select("td[class=time]");
		ArrayList<Elements> ele = new ArrayList<Elements>();
		ele.add(titles);
		ele.add(times);
		return ele;
	}
	
	// 두날짜의 차이 구하기
	public boolean doDiffOfDate(String start,String end, long differ){

		try {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
	        Date beginDate = formatter.parse(start);
	        Date endDate = formatter.parse(end);
	         
	        // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
	        long diff = endDate.getTime() - beginDate.getTime();
	        long diffDays = diff / (24 * 60 * 60 * 1000);
	 
	        //System.out.println("날짜차이=" + diffDays);
	        if(diffDays<=differ) {
	        	return true;
	        }
	        else {
	        	return false;
	        }
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return false;
	}

}
