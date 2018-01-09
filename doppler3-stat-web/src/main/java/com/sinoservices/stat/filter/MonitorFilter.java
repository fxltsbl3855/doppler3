package com.sinoservices.stat.filter;

import com.sinoservices.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MonitorFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(MonitorFilter.class);
    Pattern pattern = null;
    public String urlPattern;
    public String forceEncoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlPattern = filterConfig.getInitParameter("urlPattern");
        forceEncoding = filterConfig.getInitParameter("forceEncoding");

        if(urlPattern == null || "".equals(urlPattern.trim())){
            urlPattern = ".*\\.[s]{0,1}html.*";
        }
        pattern = Pattern.compile(urlPattern);

        if(forceEncoding!=null) forceEncoding = forceEncoding.trim();
        logger.info("init invoke...urlPattern={},forceEncoding={}",urlPattern,forceEncoding);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if(forceEncoding != null && !"".equals(forceEncoding.trim())){
            servletRequest.setCharacterEncoding(forceEncoding);
            logger.debug("use forceEncoding param,forceEncoding={}",forceEncoding);
        }

        if(logger.isDebugEnabled()) {
            logger.debug("[stat] doFilter start..............,reqCharset={},resCharset={}",servletRequest.getCharacterEncoding(),servletResponse.getCharacterEncoding());
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String cUrl = httpServletRequest.getRequestURI();
        StatusExposingServletResponse response = new StatusExposingServletResponse((HttpServletResponse)servletResponse);
        if(logger.isDebugEnabled()) {
            logger.debug("[stat] StatusExposingServletResponse charset={}",response.getCharacterEncoding());
        }

        if(!match(cUrl)){
            filterChain.doFilter(servletRequest,response);
            if(logger.isDebugEnabled()) {
                logger.debug("[stat] doFilter drop..............cUrl=" + cUrl+",urlPattern="+urlPattern+",pattern="+pattern);
            }
            return;
        }
        String username = "";
        String clientIp = MonitorUtil.getUserIp(httpServletRequest);
        String brower = BrowerUtil.getBrowerAndVersion(httpServletRequest.getHeader("User-Agent"));
        String deployPath = MonitorUtil.getDeployPath(servletRequest.getRealPath("/"));
        String className = MonitorUtil.getBrifePath(cUrl,deployPath);
        String headerInfo = MonitorUtil.getHeaderInfo(httpServletRequest);

        Map<String, String[]> paramMap = httpServletRequest.getParameterMap();
        String paramStr = MonitorUtil.toStr(paramMap);
        long startTime = System.currentTimeMillis();
        long endTime;
        try {
            filterChain.doFilter(servletRequest, response);
            username = MonitorUtil.getUsername(getSession(servletRequest));
            endTime = System.currentTimeMillis();
            if(logger.isInfoEnabled()) {
                logger.info("[stat] @action=ACTION_REQ_OUT @className="+className +" @method= @result=SUCCESS @param="+paramStr+" @username="+username+" @clientIp="+clientIp+" @brower="+brower+" @header="+headerInfo+" @code="+response.getStatus()+" @time="+(endTime-startTime)+"ms");
            }
        }catch (Exception e){
            endTime = System.currentTimeMillis();
            logger.error("[stat] @action=ACTION_REQ_OUT @className="+className +" @method= @result=EXCEPTION @param="+paramStr+" @username="+username+" @clientIp="+clientIp+" @brower="+brower+" @header="+headerInfo+" @code="+response.getStatus()+" @time="+(endTime-startTime)+"ms ",e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {}

    public HttpSession getSession(ServletRequest servletRequest){
        try {
            return ((HttpServletRequest) servletRequest).getSession();
        }catch (IllegalStateException e){
            logger.debug("current request has no session,e={}",e.getMessage());
            return null;
        }catch (Exception e){
            logger.error("current request get session exception,e="+e.getMessage());
            return null;
        }
    }

    /**
     * url是否需要统计
     * @param currentUrl
     * @return
     */
    public boolean match(String currentUrl){
        //按配置过滤
        if(pattern != null){
            Matcher matcher = pattern.matcher(currentUrl);
            if(logger.isDebugEnabled()) {
                logger.debug("[stat] doFilter match url,cUrl=" + currentUrl + ",urlPattern=" + urlPattern + ",pattern=" + pattern + ",result=" + matcher.matches());
            }
            return matcher.matches();
        }
        logger.error("MonitorFilter init error,pattern is null");
        //默认过滤shtml
        if(currentUrl.indexOf(".shtml")>0 || currentUrl.indexOf(".html")>0){
            return true;
        }
        return false;
    }
}
