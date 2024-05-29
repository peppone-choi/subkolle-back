package com.subkore.back.carousel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 메인의 캐러셀 아이템 엔티티
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
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String linkTo;
    @Column(name = "orders")
    @Setter
    private Integer order;
    @Builder.Default
    private Boolean isShow = true;
    @Builder.Default
    private Boolean isDeleted = false;
}
