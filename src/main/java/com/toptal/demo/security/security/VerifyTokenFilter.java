package com.toptal.demo.security.security;


import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.toptal.demo.security.identity.TokenUtil;

/*
This filter checks if there is a token in the Request service header and the token is not expired
it is applied to all the routes which are protected
*/
public class VerifyTokenFilter extends GenericFilterBean {

    private final com.toptal.demo.security.identity.TokenUtil tokenUtil;
    //private AuthenticationFailureHandler loginFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    public VerifyTokenFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;
        try {
            Optional<Authentication> authentication = tokenUtil.verifyToken(request);
            if (authentication.isPresent()) {
              SecurityContextHolder.getContext().setAuthentication(authentication.get());
            }
            else{
              SecurityContextHolder.getContext().setAuthentication(null);
            }
            filterChain.doFilter(req, res);
        }
        catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
        finally {
//            SecurityContextHolder.getContext().setAuthentication(null);
            return;  // always return void
        }
    }

}
