package com.example.companyservice.administer.entity;

import com.example.companyservice.administer.dto.request.AdministerRequestDto;
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
public class Administer {

    @Id
    @Column(name = "administer_id")
    private Long id;

    private String email;

    private String password;

    public static Administer of(String email, String encodedPassword) {
        return Administer.builder()
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
