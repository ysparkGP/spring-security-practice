package me.yusung.practice.spring.security.jwt;


import me.yusung.practice.spring.security.user.User;
import me.yusung.practice.spring.security.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/*
JWT 를 이용한 인증
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException{
        String token = null;

        try{
            // cookie 에서 JWT token 을 가져온다.
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception ignored){

        }
        if(token != null){
            try{
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch(Exception e){
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        chain.doFilter(request, response);
    }

    /*
    JWT 토큰으로 User 를 찾아서 UsernamePasswordAuthenticationToken 을 만들어서 반환
    User 가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token){
        String userName = JwtUtils.getUserName(token);
        if(userName != null){
            User user = userRepository.findByUsername(userName); // 유저를 유저명으로 찾기
            return new UsernamePasswordAuthenticationToken(
                    user, // principal
                    null,
                    user.getAuthorities()
            );
        }
        return null; // 유저가 없다면 null
    }
}
