package com.toptal.demo.security.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.identity.TokenUtil;


@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void configure(final WebSecurity web) throws Exception {
        // Filters will not get executed for the resources
        web.ignoring().antMatchers("/", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**"
            , "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**"
            , "/*.html", "/**/*.html" ,"/**/*.css","/**/*.js","/**/*.png","/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf","/**/*.woff");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        .exceptionHandling().and()
        .anonymous().and()
        // Disable Cross site references
        .csrf().disable()
        // Add CORS Filter
        .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                // custom JSON based authentication by POST of {"email":"<name>","password":"<password>"} which sets the
                // token header upon authentication
                .addFilterBefore(new GenerateTokenForUserFilter("/authenticate", authenticationManager(), tokenUtil, loginAttemptRepository, userRepository),UsernamePasswordAuthenticationFilter.class)
        // Custom Token based authentication based on the header previously given to the client
        .addFilterBefore(new VerifyTokenFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .antMatchers(HttpMethod.GET, "/activate/*").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/joggings/", "/reports/averageSpeedAndDistancePerWeek", "/reports/getFastestSlowestRun",
                        "/reports/getDayWithTheGreatestDistanceRan", "/reports/gettotalTimeSpentJogging")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/joggings/{joggingId}", "/joggings/getAll").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/joggings/{joggingId}").hasAnyRole("ADMIN", "USER").antMatchers(HttpMethod.PUT, "/joggings/")
                .hasAnyRole("ADMIN", "USER").antMatchers(HttpMethod.GET, "/users/", "/users/getByEmail/{userEmail}", "/users/getById/{ID}")
                .hasAnyRole("ADMIN", "MANAGER")
                .antMatchers(HttpMethod.GET, "/joggings/getAllForUser/{userEmail}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/reacivate_user/{userId}", "/users/").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers(HttpMethod.DELETE, "/users/{ID}").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated();
    }

    /*
    * If You want to store encoded password in your databases and authenticate user
    * based on encoded password then uncomment the below method and provde an encoder

    //@Autowired
    //private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    */
}
