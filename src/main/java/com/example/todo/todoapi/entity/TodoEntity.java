package com.example.todo.todoapi.entity;

// 일정관리 프로그램

import com.example.todo.userapi.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "todoId")
@Builder

@Entity
@Table(name = "tbl_todo")

public class TodoEntity {

    // pk가 랜덤 문자열로 지정된다. 즉 절대 중복이 안나오는 방식중에 하나이다.
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    // uuid전략으로 자동 생성됨
    private String todoId;

    @Column(nullable = false, length = 30)
    private String title; // 제목
    private boolean done; // 일정 완료 여부

    @CreationTimestamp
    private LocalDateTime createDate; // 등록시간

    // 회원과 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // 다대일 내가 주인공
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
