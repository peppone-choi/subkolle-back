package com.subkore.back.carousel.repository;

import com.subkore.back.carousel.entity.Carousel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselRepository extends JpaRepository<Carousel, Long> {

    List<Carousel> findAllByIsDeletedFalseOrderByOrder();

    List<Carousel> findAllByIsDeletedFalseAndIsShowTrueOrderByOrder();

    Integer countByIsDeletedFalse();
}