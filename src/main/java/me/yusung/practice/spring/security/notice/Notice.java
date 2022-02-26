package me.yusung.practice.spring.security.notice;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue
    private Long id;

    /*
    공지사항 제목
     */
    private String title;

    /*
    공지사항 내용
    @Lob 은 가변길이를 갖는 큰 데이터를 저장하는데 사용하는 데이터형
     스프링이 추론하여 어떤 타입으로 저장할지를 판단하는데, String 과 char 를 기본으로 하는 타입을 제외하면
     @Blob 으로 사용된다.
     */
    @Lob
    private String content;

    /*
    등록일시
     */
    @CreatedDate
    private LocalDate createdAt;

    /*
    수정일시
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Notice(
            String title,
            String content
    ){
        this.title = title;
        this.content = content;
    }
}
