package com.bai.account.shiro;

import com.bai.account.exception.BizErrorCode;
import com.bai.account.exception.ErrorResponse;
import com.bai.account.exception.ServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                //allow them to see the login page ;)
                return true;
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            val errorResponse = ErrorResponse.builder()
                .message("No access for related url")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorType(ServiceException.ErrorType.Client)
                .code(BizErrorCode.NO_AUTHORIZED)
                .build();
            val writer = response.getWriter();
            writer.write(new ObjectMapper().writeValueAsString(errorResponse));
            return false;
        }
    }
}
