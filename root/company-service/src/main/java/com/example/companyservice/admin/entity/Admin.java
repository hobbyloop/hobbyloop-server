package com.example.companyservice.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @Column(name = "administer_id")
    private Long id;

    private String email;

    private String password;

    public static Admin of(String email, String encodedPassword) {
        return Admin.builder()
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
