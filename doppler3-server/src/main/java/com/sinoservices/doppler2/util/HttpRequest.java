package com.sinoservices.doppler2.util;

import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 2012-05-22 19:26
 */
public class HttpRequest {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	private static int connectTimeOut = 3000;
	private static int readTimeOut = 10000;
	private static String requestEncoding = "UTF-8";
	private static String jobUpdateUrl = null;



    public static String restPost(String strUrl, Object params) throws IOException {
		if (strUrl == null || "".equals(strUrl.trim())) {
			throw new RuntimeException("http req strUrl is null");
		}
		String param = JsonUtils.object2Json(params);
		logger.debug("http execute,url={},param={}",strUrl,param);

		long start = System.currentTimeMillis();
		URL url = new URL(strUrl);
		HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
		urlconn.setRequestMethod("POST");
		urlconn.setDoOutput(true);
		urlconn.setDoInput(true);
		urlconn.setUseCaches(false);
		urlconn.setAllowUserInteraction(false);
		urlconn.setConnectTimeout(connectTimeOut);
		urlconn.setReadTimeout(readTimeOut);
		urlconn.setRequestProperty("Content-Type", "application/json");
		OutputStream outs = urlconn.getOutputStream();
		Writer writer = new OutputStreamWriter(outs, requestEncoding);
		writer.write(param);
		writer.close();
		outs.close();
		Integer httpCode = urlconn.getResponseCode();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		long end = System.currentTimeMillis();
		if (httpCode != 200) {
			logger.error("http reqError ,code={},res={}", httpCode, stringBuilder.toString());
		}
		logger.debug("http execute end ,time={}",(end-start));
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
//		String url  ="http://192.168.0.90:6060/api/push";
//		for (int i = 0; i <1000 ; i++) {
//			int value = NumberUtil.getRandom(2);
//			if(i>=10 && i<=20) value = 0;
//			MetricsBo[] mb = get("doppler3-web","/doppler/view/index.shtml",System.currentTimeMillis()/1000,value ,15);
//			try {
//				String res = restPost(url,mb);
//				System.out.println(i+"__"+res+"__"+JsonUtils.object2Json(mb));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				Thread.sleep(30000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		}

	}


}
