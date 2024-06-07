package com.subkore.back.user.entity;

import com.subkore.back.user.enumerate.Role;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
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
    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    List<Role> role;
    @Default
    Boolean isDeleted = false;

    public void passwordEncode(String password) {
        this.password = password;
    }
}
