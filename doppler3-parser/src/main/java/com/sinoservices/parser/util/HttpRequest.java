package com.sinoservices.parser.util;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * 2012-05-22 19:26
 */
public class HttpRequest {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	private static int connectTimeOut = 3000;
	private static int readTimeOut = 10000;
	private static String requestEncoding = "UTF-8";
	private static String jobUpdateUrl = null;

	static {
		String httpAddr = UpMessageConfig.es_addr_http == null || "".equals(UpMessageConfig.es_addr_http.trim()) ? "127.0.0.1:9200":UpMessageConfig.es_addr_http.trim();
		jobUpdateUrl = "http://"+httpAddr+"/"+ DopplerConstants.INDEX_STAT_JOB_NAME+"/_update_by_query";

	}

    public static String restPostUpdateJob(Object params){
        try {
            String res = restPost(jobUpdateUrl,params);
			return res;
		} catch(SocketTimeoutException e){
			logger.error("SocketTimeoutException error"+JsonUtils.object2Json(params));
			String res = null;
			try {
				Thread.currentThread().sleep(3000);
				res = restPost(jobUpdateUrl,params);
			} catch (Exception e1) {
				logger.error(" 2nd restPostUpdateJob error"+JsonUtils.object2Json(params),e);
			}
			return res;
		} catch(Exception e) {
            logger.error("restPostUpdateJob error"+JsonUtils.object2Json(params),e);
            return null;
        }
    }

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
		logger.debug("http execute end ,time={},param={}",strUrl,(end-start));
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
//		UpMessageConfig.es_addr_http = "192.168.0.90:9200";
//		HttpRequest.jobUpdateUrl = "http://"+"192.168.0.90:9200"+"/"+ DopplerConstants.INDEX_STAT_JOB_NAME+"/_update_by_query";
//		StatJobEntity statJobEntity = new StatJobEntity();
//		statJobEntity.setResult("SUCCESS");
//		statJobEntity.setTime(2000);
//		statJobEntity.setJobId("148878591434416329");
//		String res = HttpRequest.restPostUpdateJob(JobUpdate.fillData(statJobEntity.getResult(),new Long(statJobEntity.getTime()).intValue(),statJobEntity.getJobId()));
//		System.out.println("返回的消息是:"+res );

		String uu = "http://192.168.0.89:8081/test/go.shtml";

		Test test = new Test();
		test.setA("你好,奥奥");
		test.setS(2);
		try {
			String res = sendPost(uu,"a=爱国者&s=1");
			System.out.println(res+"_______________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
}
