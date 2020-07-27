package com.bai.account.shiro;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CustomHttpFilter extends PermissionsAuthorizationFilter {
    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestUri = getPathWithinApplication(request);
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestUri);
        val array = path.split("::");
        //val url = array[0];
        boolean isHttpRequestMatched = true;
        if (array.length > 1) {
            val httpMethod = ((HttpServletRequest) request).getMethod();
            val method = array[1];
            isHttpRequestMatched = method.equals(httpMethod);
        }
        return pathsMatch(path, requestUri) && isHttpRequestMatched;
    }
}
