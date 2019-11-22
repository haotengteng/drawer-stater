package com.bee.ms.drawer.filter;


import com.alibaba.fastjson.JSON;
import com.bee.ms.common.portal.core.PortalTokenContext;
import com.bee.ms.drawer.autoconfigure.DrawerProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * 入参/响应日志打印
 *
 * @author created by htt on 2018/8/2
 */
@WebFilter(urlPatterns = "/*")
@Order(value = 2147483647)
@Slf4j
@Component
public class LogFilter implements Filter {

    @Autowired
    DrawerProperties drawerProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Long st = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (drawerProperties.getLogSwitch()) {
            LoggingHttpServletRequestWrapper multiReadRequest = new LoggingHttpServletRequestWrapper(httpServletRequest);
            LoggingHttpServletResponseWrapper multiResponse = new LoggingHttpServletResponseWrapper(httpServletResponse);
            if (httpServletRequest.getMethod().equalsIgnoreCase("GET")) {
                LoggerModel loggerModel = new LoggerModel();
                loggerModel.setParams(URLDecoder.decode(StringUtils.isEmpty(httpServletRequest.getQueryString()) ? "" : httpServletRequest.getQueryString(), "UTF-8"));
                loggerModel.setPlatform(httpServletRequest.getHeader("platform"));
                loggerModel.setUrl(httpServletRequest.getRequestURL().toString());
                loggerModel.setAccountId(getAccountId());
                log.info("请求信息：{}", JSON.toJSONString(loggerModel, WriteNullStringAsEmpty));
            } else {
                StringBuilder content = new StringBuilder();
                ServletInputStream servletInputStream = multiReadRequest.getInputStream();
                byte[] b = new byte[1024];
                int lens = -1;
                while ((lens = servletInputStream.read(b)) > 0) {
                    content.append(new String(b, 0, lens));
                }
                String strcont = content.toString();
                LoggerModel loggerModel = new LoggerModel();
                loggerModel.setParams(strcont);
                loggerModel.setPlatform(httpServletRequest.getHeader("platform"));
                loggerModel.setUrl(httpServletRequest.getRequestURL().toString());
                loggerModel.setAccountId(getAccountId());
                log.info("请求信息：{}", JSON.toJSONString(loggerModel));
            }
            chain.doFilter(multiReadRequest, multiResponse);
            response.getOutputStream().write(multiResponse.getContentAsBytes());
            log.info("响应信息：{}，请求耗时:{}ms", multiResponse.getContent(), System.currentTimeMillis() - st);
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }

    }

    //    private String getAccountId(HttpServletRequest httpServletRequest) {
//        String accountId = "";
//        try {
//            String portalTokenHeader = httpServletRequest.getHeader("Ms-Header-Portal-Token");
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(portalTokenHeader)) {
//                PortalToken portalToken = JSON.parseObject(portalTokenHeader, PortalToken.class);
//                Long id = portalToken.getAccountId();
//                if (id == null) {
//                    return accountId;
//                } else {
//                    accountId = id.toString();
//                }
//            }
//        } catch (Exception e) {
//            log.warn("过滤器获取本次请求accountId失败");
//        }
//        return accountId;
//    }
    private String getAccountId() {
        // 依托网关处理之后 或者根据网关处理的合适自行从请求头中解析 兼容性更强
        String accountId = "";
        try {
            Long id = PortalTokenContext.getInstance().getAccountId();
            if (id == null) {
                return accountId;
            } else {
                accountId = id.toString();
            }
        } catch (Exception e) {
            log.warn("过滤器获取本次请求accountId失败");
        }
        return accountId;
    }


    @Override
    public void destroy() {

    }

    @Data
    class LoggerModel {
        private String url;
        private String params;
        private String platform;
        private String accountId;


        public LoggerModel() {
        }
    }

}
