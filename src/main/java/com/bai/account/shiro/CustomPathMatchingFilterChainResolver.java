package com.bai.account.shiro;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String currentPath = getPathWithinApplication(request);
        for (String pathPattern : filterChainManager.getChainNames()) {
            if (isHttpRequestMatched(pathPattern,currentPath,request)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + currentPath + "]. "
                        + "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }
    private boolean isHttpRequestMatched(String chain, String currentPath, ServletRequest request) {
        val array = chain.split("::");
        val url = array[0];
        boolean isHttpRequestMatched = true;
        if (array.length > 1) {
            val httpMethod = ((HttpServletRequest) request).getMethod();
            val method = array[1];
            isHttpRequestMatched = method.equals(httpMethod);
        }
        return pathMatches(url,currentPath) && isHttpRequestMatched;
    }
}
