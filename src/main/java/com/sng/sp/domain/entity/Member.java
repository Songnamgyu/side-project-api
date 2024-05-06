package com.sng.sp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue
    @Column(name="memberId")
    private Long id;

    private String membername;

    private int age;
}
