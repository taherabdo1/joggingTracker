package com.toptal.demo.security.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.entities.LoginAttempt;
import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.identity.TokenUser;
import com.toptal.demo.security.identity.TokenUtil;
import com.toptal.demo.security.response.session.OperationResponse.ResponseStatusEnum;
import com.toptal.demo.security.response.session.SessionResponse;

import lombok.extern.slf4j.Slf4j;

/* This filter maps to /session and tries to validate the username and password */
@Slf4j
public class GenerateTokenForUserFilter extends AbstractAuthenticationProcessingFilter {

    private TokenUtil tokenUtil;

    private LoginAttemptRepository loginAttemptRepository;

    private UserRepository userRepository;

    protected GenerateTokenForUserFilter(final String urlMapping, final AuthenticationManager authenticationManager, final TokenUtil tokenUtil,
            final LoginAttemptRepository loginAttemptRepository, final UserRepository userRepository) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        try {
            final Map<String, String> userNameAndPassword = getEmailAndPasswordFromRequest(request);
            request.setAttribute("email", userNameAndPassword.get("email"));
            log.info("username:{} and paswword:{} \n", userNameAndPassword.get("email"), userNameAndPassword.get("password"));
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userNameAndPassword.get("email"),
                    userNameAndPassword.get("password"));
            return getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or
                                                                       // faliureAuthentication function
        } catch (JSONException | AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain,
            final Authentication authToken)
        throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authToken);

        final TokenUser tokenUser = (TokenUser) authToken.getPrincipal();
        final SessionResponse resp = new SessionResponse();
        final UserDto respItem = new UserDto();
        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        final String tokenString = this.tokenUtil.createTokenForUser(tokenUser);

        respItem.setEmail(tokenUser.getUser().getEmail());
        respItem.setId(tokenUser.getUser().getId());
        respItem.setRole(tokenUser.getUser().getRole());
        respItem.setToken(tokenString);

        resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
        resp.setOperationMessage("Login Success");
        resp.setItem(respItem);
        final String jsonRespString = ow.writeValueAsString(resp);

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(jsonRespString);
        res.getWriter().flush();
        res.getWriter().close();

    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed)
        throws IOException, ServletException {
        final User user = userRepository.findOneByEmail((String) request.getAttribute("email")).orElse(null);
        if (user != null) {

            if (user.getLoginAttempt() == null) {
                final LoginAttempt newLoginAttempt = new LoginAttempt();
                newLoginAttempt.setDate(new Date());
                newLoginAttempt.setNumberOfTrials(1);
                newLoginAttempt.setUser(user);
                loginAttemptRepository.save(newLoginAttempt);
            } else {
                if (user.getLoginAttempt().getNumberOfTrials() + 1 >= 3) {
                    user.setBlocked(true);
                }
                user.getLoginAttempt().setNumberOfTrials(user.getLoginAttempt().getNumberOfTrials() + 1);
                loginAttemptRepository.save(user.getLoginAttempt());
                userRepository.save(user);
            }
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private Map<String, String> getEmailAndPasswordFromRequest(final HttpServletRequest request) {
        String jsonString;
        final Map<String, String> result = new HashMap<>();
        try {
            jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
            /* using org.json */
            final JSONObject userJSON = new JSONObject(jsonString);
            result.put("email", userJSON.getString("email"));
            result.put("password", userJSON.getString("password"));
        } catch (final IOException e) {
            throw new InternalServerErrorException();
        }
        return result;
    }

}
