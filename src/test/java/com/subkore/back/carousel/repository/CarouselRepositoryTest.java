package com.subkore.back.carousel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subkore.back.carousel.entity.Carousel;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class CarouselRepositoryTest {

    @Autowired
    private CarouselRepository carouselRepository;

    @Test
    void 저장_테스트() {
        // given
        Carousel carousel = Carousel.builder()
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .isShow(true)
            .build();
        // when
        Carousel savedCarousel = carouselRepository.save(carousel);
        // then
        assertEquals(savedCarousel.getTitle(), carousel.getTitle());
    }

    @Test
    void 차례대로_정렬_테스트() {
        // given
        Carousel carousel1 = Carousel.builder()
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .isShow(true)
            .build();
        Carousel carousel2 = Carousel.builder()
            .order(1)
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .isDeleted(false)
            .isShow(true)
            .build();
        Carousel carousel3 = Carousel.builder()
            .order(1)
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .isDeleted(false)
            .isShow(false)
            .build();
        carouselRepository.save(carousel1);
        carouselRepository.save(carousel2);
        carouselRepository.save(carousel3);
        // when
        List<Carousel> carouselList = carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder();
        // then
        assertEquals(carouselList.getFirst().getOrder(), 0);
        assertEquals(carouselList.size(), 2);
    }

    @Test
    void 안보이는_것_포함_테스트() {
        // given
        Carousel carousel1 = Carousel.builder()
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .isShow(true)
            .build();
        Carousel carousel2 = Carousel.builder()
            .order(1)
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .isDeleted(false)
            .isShow(true)
            .build();
        Carousel carousel3 = Carousel.builder()
            .order(1)
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .isDeleted(false)
            .isShow(false)
            .build();
        carouselRepository.save(carousel1);
        carouselRepository.save(carousel2);
        carouselRepository.save(carousel3);
        // when
        List<Carousel> carouselList = carouselRepository.findAllByIsDeletedFalseOrderByOrder();
        // then
        assertEquals(carouselList.getFirst().getOrder(), 0);
        assertEquals(carouselList.size(), 3);
    }
}