package me.yusung.practice.spring.security.admin;

import me.yusung.practice.spring.security.user.User;
import me.yusung.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
// 활성 프로파일: 스프링 컨테이너를 실행할 때, 실행 환경을 지정해주는 속성으로 환경을 구분하기 위해 사용
@ActiveProfiles(profiles = "test")
@Transactional
class AdminControllerTest {

    // 필요한 의존 객체 타입에 해당하는 빈을 찾아 주입
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;
    private User user;
    private User admin;

    // 테스트 메서드 실행 이전에 수행
    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // ROLE_USER 권한이 있는 유저 생성
        user = userRepository.save(new User("user", "user", "ROLE_USER"));
        // ROLE_ADMIN 권한이 있는 관리자 생성
        user = userRepository.save(new User("admin","admin","ROLE_ADMIN"));
    }

    @Test
    void getNoteForAdmin_인증없음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf()))
                .andExpect(redirectedUrlPattern("**/login"))
                .andExpect(status().is3xxRedirection()); // login 이 안되어있으므로 로그인 페이지로 redirect
    }

    @Test
    void getNoteForAdmin_어드민인증있음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf()).with(user(admin))) // csrf 토큰, 어드민 추가
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNoteForAdmin_유저인증있음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf()).with(user(user)))
                .andExpect(status().isForbidden()); // 접근 거부
    }
}