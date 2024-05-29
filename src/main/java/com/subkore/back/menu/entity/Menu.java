package com.subkore.back.menu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Menu의 Entity Class
 *
 * @author peppone-choi (peppone.choi@gmail.com)
 * @version 1.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu {

    /**
     * db에 저장 될 시 저장되는 ID (auto increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 해당 메뉴의 위치 순서
     */
    @Setter
    private Integer menuOrder;
    /**
     * 해당 메뉴가 출력하는 아이콘
     */
    private String icon;
    /**
     * 해당 메뉴가 출력하는 아이콘의 대분류
     */
    private String iconType;
    /**
     * 해당 메뉴의 이름으로 링크에 보여짐
     */
    @Default
    private String text = "메뉴";
    /**
     * 해당 메뉴를 클릭하면 이동하는 경로로 상대 경로임
     */
    @Default
    private String linkTo = "/";

    @Default
    private Boolean isDeleted = false;
}
