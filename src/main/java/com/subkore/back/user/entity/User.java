package com.subkore.back.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Default
    String userUUID = UUID.randomUUID().toString().replace("-","");
    String email;
    String nickname;
    String password;
    String profileImage;
    String role;
    @Default
    Boolean isDeleted = false;

    public void passwordEncode(String password) {
        this.password = password;
    }
}
