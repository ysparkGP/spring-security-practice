package me.yusung.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import me.yusung.practice.spring.security.user.User;
import me.yusung.practice.spring.security.user.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig2 extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // basic authentication filter disable
        http.httpBasic().disable();

        //csrf
        http.csrf();

        //rememberMeAuthenticationFilter
        http.rememberMe();

        // authorization (경로별 권한 설정)
        http.authorizeRequests()
                // /와 /home 와 signup
                .antMatchers("/", "/home", "/signup").permitAll()
                .antMatchers("/note").hasRole("USER") // 유저권한인 경우만
                .antMatchers("/admin").hasRole("ADMIN") // 어드민권한인 경우만
//                .antMatchers(HttpMethod.GET,"/notice").authenticated() //인증 받았다면 가능
                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN") //어드민권한인 경우만
                .antMatchers(HttpMethod.DELETE,"/notice").hasRole("ADMIN")
                .anyRequest().authenticated();

        // login
        http.formLogin()
                .loginPage("/login") //로그인 페이지 경로 설정
                .defaultSuccessUrl("/")
                .permitAll();

        // logout
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        // 정적리소스들의 일반적인 위치들은 필터를 거치지않고 이그노어링
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userService.findByUserName(username);
            if(user == null){
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}*/
