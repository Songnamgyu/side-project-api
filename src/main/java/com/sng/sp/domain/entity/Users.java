package com.sng.sp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phoneNumber;
    private String gender;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private PublicStatus publicStatus;

    @Enumerated
    @JsonIgnore(EnumType.STRING)
    private ShareStatus shareStatus;

    private LocalDateTime createdAt;
}
