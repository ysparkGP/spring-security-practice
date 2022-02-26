package me.yusung.practice.spring.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// OncePerRequestFilter: 모든 서블릿에 일관된 요청을 처리하기 위해 만들어진 필터..
// 즉, 이 추상 클래스를 구현한 필터는 사용자의 한번에 요청 당 딱 한번만 실행되는 필터를 만들 수 있다.
@Slf4j
public class StopwatchFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            // 필터들을 순서대로 줄줄이 엮어놓은 객체
            FilterChain filterChain) throws ServletException, IOException{
        StopWatch stopWatch = new StopWatch(request.getServletPath());
        stopWatch.start(); // stop watch 시작
        // doFilter 메서드를 통해서 요청과 응답이 체인을 통과할 때 마다 컨테이너에서 호출된다.
        // 체인을 따라서 계속 다음에 존재하는 필터로 이동하는 것.
        filterChain.doFilter(request, response);
        stopWatch.stop(); //stop watch 종료
        log.info(stopWatch.shortSummary());

    }
}
