package com.subkore.back.carousel.service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import com.subkore.back.carousel.repository.CarouselRepository;
import com.subkore.back.exception.CarouselException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class CarouselServiceImplTest {

    @Mock
    CarouselRepository carouselRepository;
    @InjectMocks
    CarouselServiceImpl carouselService;

    @Test
    void 캐러셀의_리스트가_반환된다() {
        // given
        List<Carousel> carouselList = List.of(Carousel.builder()
                .id(0L)
                .order(0)
                .title("test")
                .description("test")
                .linkTo("test")
                .isDeleted(false)
                .build(),
            Carousel.builder()
                .id(1L)
                .order(1)
                .title("test2")
                .description("test2")
                .linkTo("test2")
                .isDeleted(false)
                .build());
        when(carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder()).thenReturn(
            carouselList);
        // when
        List<CarouselResponseDto> responseList = carouselService.getCarouselList();
        // then
        assertEquals(responseList.size(), 2);
    }

    @Test
    void 캐러셀이_없을_때엔_리스트가_반환되지_않고_예외가_발생한다() {
        // given
        when(carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder()).thenReturn(
            List.of());
        // when
        CarouselException e = assertThrows(CarouselException.class,
            (() -> carouselService.getCarouselList()));
        // then
        assertEquals(e.getMessage(), "캐러셀 아이템이 존재하지 않습니다.");
    }

    @Test
    void 캐러셀을_등록할_수_있다() {
        // given
        CreateCarouselRequestDto carouselRequestDto = CreateCarouselRequestDto.builder()
            .title("test")
            .description("test")
            .linkTo("test")
            .imageUrl("test")
            .build();
        when(carouselRepository.save(any(Carousel.class))).then(
            AdditionalAnswers.returnsFirstArg());
        // when
        CarouselResponseDto savedCarousel = carouselService.createCarousel(carouselRequestDto);
        // then
        assertEquals(savedCarousel.title(), "test");
    }

    @Test
    void 캐러셀을_수정할_수_있다() {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .build();
        UpdateCarouselRequestDto carouselRequestDto = UpdateCarouselRequestDto.builder()
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .imageUrl("test2")
            .build();
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.of(carousel));
        when(carouselRepository.existsById(0L)).thenReturn(true);
        when(carouselRepository.save(any(Carousel.class))).then(
            AdditionalAnswers.returnsFirstArg());
        // when
        CarouselResponseDto updatedCarousel = carouselService.updateCarousel(0L,
            carouselRequestDto);
        // then
        assertEquals(updatedCarousel.title(), "test2");
    }

    @Test
    void 캐러셀이_존재하지_않는_경우_수정이_불가능하고_예외가_발생한다() {
        // given
        UpdateCarouselRequestDto carouselRequestDto = UpdateCarouselRequestDto.builder()
            .title("test2")
            .description("test2")
            .linkTo("test2")
            .imageUrl("test2")
            .build();
        when(carouselRepository.existsById(0L)).thenReturn(false);
        // when
        // then
        assertThrows(CarouselException.class,
            () -> carouselService.updateCarousel(0L, carouselRequestDto));
    }

    @Test
    void 캐러셀을_삭제할_수_있다() {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .build();
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.of(carousel));
        when(carouselRepository.existsById(0L)).thenReturn(true);
        // when
        carouselService.deleteCarousel(0L);
        // then
        assertEquals(carousel.getIsDeleted(), true);
    }

    @Test
    void 캐러셀이_존재하지_않는_경우_삭제가_불가능하고_예외가_발생한다() {
        // given
        when(carouselRepository.existsById(0L)).thenReturn(false);
        // when
        // then
        assertThrows(CarouselException.class,
            () -> carouselService.deleteCarousel(0L));
    }

    @Test
    void 이미_삭제된_캐러셀을_삭제할_시_예외가_발생한다() {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(true)
            .build();
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.of(carousel));
        when(carouselRepository.existsById(0L)).thenReturn(true);
        // when
        // then
        assertThrows(CarouselException.class,
            () -> carouselService.deleteCarousel(0L));
    }

    @Test
    void 삭제된_캐러셀을_복구할_수_있다() {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(true)
            .build();
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.of(carousel));
        when(carouselRepository.existsById(0L)).thenReturn(true);
        // when
        carouselService.recoverCarousel(0L);
        // then
        assertEquals(carousel.getIsDeleted(), false);
    }

    @Test
    void 이미_복구된_캐러셀을_다시_복구_할_수_없다() {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .build();
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.of(carousel));
        when(carouselRepository.existsById(0L)).thenReturn(true);
        // when
        // then
        assertThrows(CarouselException.class,
            () -> carouselService.recoverCarousel(0L));
    }
}