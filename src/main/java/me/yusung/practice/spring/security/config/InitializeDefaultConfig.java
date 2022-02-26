package me.yusung.practice.spring.security.config;


import lombok.RequiredArgsConstructor;
import me.yusung.practice.spring.security.note.NoteService;
import me.yusung.practice.spring.security.notice.NoticeService;
import me.yusung.practice.spring.security.user.User;
import me.yusung.practice.spring.security.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/*
초기 상태 등록 Config
 */

@Configuration
@RequiredArgsConstructor
@Profile(value = "!test") // test 에서는 제외
public class InitializeDefaultConfig {

    private final UserService userService;
    private final NoteService noteService;
    private final NoticeService noticeService;

    /*
    유저 등록, note 3개 등록
     */
    @Bean
    public void initializeDefaultUser(){
        User user = userService.signup("user", "user");
        noteService.saveNote(user, "테스트1", "테스트1입니다.");
        noteService.saveNote(user, "테스트2", "테스트2입니다.");
        noteService.saveNote(user, "테스트3", "테스트3입니다.");
    }

    /*
    어드민 등록, 공지하상 2개 등록
     */
    @Bean
    public void initializeDefaultAdmin(){
        userService.signupAdmin("admin","admin");
        noticeService.saveNotice("환영합니다.","Welcome~");
        noticeService.saveNotice("노트 작성 방법 공지","1. 회원가입\n 2. 로그인\n 3. 노트 작성\n 4. 저장\n *본인 외에는 게시글을 볼 수 없습니다.");
    }
}
