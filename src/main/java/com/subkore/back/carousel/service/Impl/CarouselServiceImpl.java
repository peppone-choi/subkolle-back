package com.subkore.back.carousel.service.Impl;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import com.subkore.back.carousel.mapper.CarouselMapper;
import com.subkore.back.carousel.repository.CarouselRepository;
import com.subkore.back.carousel.service.CarouselService;
import com.subkore.back.exception.CarouselException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarouselServiceImpl implements CarouselService {

    private final CarouselRepository carouselRepository;
    private final CarouselMapper carouselMapper = CarouselMapper.INSTANCE;
    @Override
    public List<CarouselResponseDto> getCarouselList() {
        List<Carousel> carouselList = carouselRepository.findAll();
        if (carouselList.isEmpty()) {
            throw new CarouselException("캐러셀 아이템이 존재하지 않습니다.");
        }
        List<Carousel> sortedCarouselList =
            carouselList.stream().sorted(Comparator.comparingInt(Carousel::getOrder)).toList();
        return sortedCarouselList.stream().map(carouselMapper::carouselToCarouselResponseDto).toList();
    }

    @Override
    public CarouselResponseDto createCarousel(CreateCarouselRequestDto createCarouselRequestDto) {
        Carousel carousel = carouselMapper.createCarouselRequestDtoToCarousel(createCarouselRequestDto);
        Carousel savedCarousel = carouselRepository.save(carousel);
        return carouselMapper.carouselToCarouselResponseDto(savedCarousel);
    }
}
