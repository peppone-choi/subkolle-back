package com.subkore.back.menu.repository;

import com.subkore.back.menu.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Menu 클래스의 Repository JpaRepository 를 상속받으며 DB를 CRUD 하는 역할을 한다
 *
 * @author peppone-choi (peppone.choi@gmail.com)
 * @version 1.0
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByIsDeletedFalseOrderByMenuOrder();

    Integer countByIsDeletedFalse();
}
