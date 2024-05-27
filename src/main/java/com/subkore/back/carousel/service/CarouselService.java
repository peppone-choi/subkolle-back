package com.subkore.back.carousel.service;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import java.util.List;

public interface CarouselService {

    List<CarouselResponseDto> getCarouselList();

    CarouselResponseDto createCarousel(CreateCarouselRequestDto createCarouselRequestDto);
}
