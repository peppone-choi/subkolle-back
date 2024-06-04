package com.subkore.back.user.repository;

import com.subkore.back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmailAndIsDeletedFalse(String email);

    User findByEmailAndIsDeletedFalse(String email);

    boolean existsUserByUserUUIDAndIsDeletedFalse(String userUUID);

    User findByUserUUIDAndIsDeletedFalse(String userUUID);

    boolean existsUserByNicknameAndIsDeletedFalse(String nickname);

    User findByNicknameAndIsDeletedFalse(String nickname);
}
