package me.yusung.practice.spring.security.note;


import lombok.RequiredArgsConstructor;
import me.yusung.practice.spring.security.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    /*
    노트 게시글 조회

    @return 노트 view (note/index.html)
     */

    @GetMapping
    public String getNote(Authentication authentication, Model model){
        // 인증된 객체에 대한 정보 얻기
        // SecurityContext context  = SecurityContextHolder.getContext()
        // Authentication authentication = context.getAuthentication()
        /*
        Authentication 은 AuthenticationManager 에 입력하는 것은 사용자가 인증을 위해 제공한 자격 증명을 제공하기 위함
        그리고 현재 인증된 사용자를 나타냄.
        현재 인증된 객체 Authentication 은 SecurityContext 에 저장되어있음
        principal: 사용자를 식별, 사용자의 username/ password 를 인증할 때 이는 종종 UserDetail 의 인스턴스로 표현된다.
        credentials: 대부분 암호를 나타냄. 거의 대부분 경우 이 정보는 유출되지 않도록 사용자가 인증된 후 지워진다.
        authorities: GrantedAuthority 는 사용자에게 부여되는 고수준 권한... 예로 role 이나 scope 가 있다.
         */

        User user = (User) authentication.getPrincipal();
        List<Note> notes = noteService.findByUser(user);
        // note/index.html 에서 notes 바인딩
        model.addAttribute("notes", notes);
        return "note/index";
    }

    /*
    노트 저장
     */
    @PostMapping
    public String saveNote(Authentication authentication, @ModelAttribute NoteRegisterDto noteDto){

        User user = (User) authentication.getPrincipal();
        noteService.saveNote(user, noteDto.getTitle(), noteDto.getContent());
        return "redirect:note";
    }

    /*
    노트 삭제
     */
    @DeleteMapping
    public String deleteNote(Authentication authentication, @RequestParam Long id){
        User user = (User) authentication.getPrincipal();
        noteService.deleteNote(user, id);
        return "redirect:note";
    }
}
