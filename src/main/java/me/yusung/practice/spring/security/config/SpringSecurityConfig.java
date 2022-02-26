package me.yusung.practice.spring.security.config;


import lombok.RequiredArgsConstructor;
import me.yusung.practice.spring.security.filter.StopwatchFilter;
import me.yusung.practice.spring.security.filter.TesterAuthenticationFilter;
import me.yusung.practice.spring.security.jwt.JwtAuthenticationFilter;
import me.yusung.practice.spring.security.jwt.JwtAuthorizationFilter;
import me.yusung.practice.spring.security.jwt.JwtProperties;
import me.yusung.practice.spring.security.user.User;
import me.yusung.practice.spring.security.user.UserRepository;
import me.yusung.practice.spring.security.user.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
Security 설정 Config
 */

@Configuration
// 웹 보안 활성화를 위한 어노테이션
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final UserRepository userRepository;

    /*
    configure(HttpSecurity) 기본 설정 3 가지
    1. 사용자 인증이 된 요청에 대해서만 요청을 허용한다.
    2. 사용자는 폼 기반 로그인으로 인증할 수 있다.
    3. 사용자는 HTTP 기반 인증으로 인증할 수 있다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
/*
        // stopwatch filter
       http.addFilterBefore(
                new StopwatchFilter(),
                WebAsyncManagerIntegrationFilter.class
        );

        // tester authentication filter
        http.addFilterBefore(
                new TesterAuthenticationFilter(this.authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        );
*/
        // basic authentication, JSession
        http.httpBasic().disable(); // basic authentication filter 비활성화

        // csrf
        http.csrf().disable();

        // remember-me : 브라우저를 닫거나 개발자도구에서 세션을 끊어버리는 등
        // 직접 로그아웃 버튼을 눌러 로그아웃을 하지 않는 이상 rememberMe라는 기능은 계속 로그인 유지
        http.rememberMe().disable();

        // session 방식을 이용하지않고 token 방식을 이용하므로 stateless
        http.sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(userRepository),
                BasicAuthenticationFilter.class
        );

        // anonymous
        //http.anonymous().principal(new User());
        // authorization: 모든 http request 는 인증된 사용자만 접근할 수 있도록 한다.
        http.authorizeRequests()
                // /와 /home은 모두에게 허용
                .antMatchers("/","/home","/signup").permitAll()
                // hello 페이지는 USER 롤을 가진 유저에게만 허용
                .antMatchers("/note").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                // 이 외에 다른 요청들은 모두 인증되어야 함을 명시
                .anyRequest().authenticated();

        // login
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll(); // 모두 허용
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                // 로그아웃시 쿠키 삭제 안하면 로그아웃 못함
                .invalidateHttpSession(true)
                .deleteCookies(JwtProperties.COOKIE_NAME);
    }

    @Override
    public void configure(WebSecurity web){
        // 정적 리소스 spring security 대상에서 제외
        // web.ignoring().antMatchers("/images/**", "/css/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /*
    UserDetailsService 구현
    @return UserDetailsService
    Spring Security 에서 유저의 정보를 가져오는 인터페이스
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return username->{
            User user = userService.findByUserName(username);
            if(user == null){
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}
