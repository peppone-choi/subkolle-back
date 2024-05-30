package com.subkore.back.carousel.service;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import java.util.List;

public interface CarouselService {

    List<CarouselResponseDto> getCarouselList();

    CarouselResponseDto createCarousel(CreateCarouselRequestDto createCarouselRequestDto);

    CarouselResponseDto updateCarousel(Long id, UpdateCarouselRequestDto updateCarouselRequestDto);

    void deleteCarousel(long id);

    void recoverCarousel(long id);
}
