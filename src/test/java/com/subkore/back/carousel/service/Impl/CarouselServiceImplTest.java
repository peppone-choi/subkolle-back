package com.subkore.back.carousel.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import com.subkore.back.carousel.mapper.CarouselMapper;
import com.subkore.back.carousel.repository.CarouselRepository;
import com.subkore.back.exception.CarouselException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarouselServiceImplTest {
    @Mock
    CarouselRepository carouselRepository;
    @Mock
    CarouselMapper carouselMapper = Mappers.getMapper(CarouselMapper.class).INSTANCE;
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
        when(carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder()).thenReturn(carouselList);
        // when
        List<CarouselResponseDto> responseList = carouselService.getCarouselList();
        // then
        assertEquals(responseList.size(), 2);
    }
    @Test
    void 캐러셀이_없을_때엔_리스트가_반환되지_않고_예외가_발생한다() {
        // given
        when(carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder()).thenReturn(List.of());
        // when
        CarouselException e = assertThrows(CarouselException.class, (() -> carouselService.getCarouselList()));
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
        when(carouselRepository.save(any(Carousel.class))).then(AdditionalAnswers.returnsFirstArg());
        // when
        CarouselResponseDto savedCarousel = carouselService.createCarousel(carouselRequestDto);
        // then
        assertEquals(savedCarousel.title(), "test");
    }
}