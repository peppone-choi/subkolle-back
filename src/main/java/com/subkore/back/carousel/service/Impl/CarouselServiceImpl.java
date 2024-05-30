package com.subkore.back.carousel.service.Impl;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import com.subkore.back.carousel.mapper.CarouselMapper;
import com.subkore.back.carousel.repository.CarouselRepository;
import com.subkore.back.carousel.service.CarouselService;
import com.subkore.back.exception.CarouselException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarouselServiceImpl implements CarouselService {

    private final CarouselRepository carouselRepository;
    private final CarouselMapper carouselMapper = CarouselMapper.INSTANCE;

    @Override
    public List<CarouselResponseDto> getCarouselList() {
        List<Carousel> sortedCarouselList =
            carouselRepository.findAllByIsDeletedFalseAndIsShowTrueOrderByOrder();
        if (sortedCarouselList.isEmpty()) {
            throw new CarouselException("캐러셀 아이템이 존재하지 않습니다.");
        }
        return sortedCarouselList.stream().map(carouselMapper::carouselToCarouselResponseDto)
            .toList();
    }

    @Override
    public CarouselResponseDto createCarousel(CreateCarouselRequestDto createCarouselRequestDto) {
        Carousel carousel = carouselMapper.createCarouselRequestDtoToCarousel(
            createCarouselRequestDto);
        Integer count = carouselRepository.countByIsDeletedFalse();
        carousel.setOrder(count);
        Carousel savedCarousel = carouselRepository.save(carousel);
        return carouselMapper.carouselToCarouselResponseDto(savedCarousel);
    }

    @Override
    public CarouselResponseDto updateCarousel(Long id,
        UpdateCarouselRequestDto updateCarouselRequestDto) {
        if (carouselRepository.existsById(id)) {
            Carousel carousel = carouselRepository.findById(id).get();
            carouselMapper.updateCarouselRequestDtoToCarousel(updateCarouselRequestDto, carousel);
            Carousel savedCarousel = carouselRepository.save(carousel);
            return carouselMapper.carouselToCarouselResponseDto(savedCarousel);
        } else {
            throw new CarouselException("존재하지 않는 캐러셀 아이템입니다.");
        }
    }

    @Override
    public void deleteCarousel(long id) {
        if (carouselRepository.existsById(id) && !carouselRepository.findById(id).get().getIsDeleted()) {
            Carousel carousel = carouselRepository.findById(id).get();
            carousel.setIsDeleted(true);
            carouselRepository.save(carousel);
        } else if (!carouselRepository.existsById(id)) {
            throw new CarouselException("존재하지 않는 캐러셀 아이템입니다.");
        } else {
            throw new CarouselException("이미 삭제된 캐러셀 아이템입니다.");
        }
    }

    @Override
    public void recoverCarousel(long id) {
        if (carouselRepository.existsById(id) && carouselRepository.findById(id).get().getIsDeleted()) {
            Carousel carousel = carouselRepository.findById(id).get();
            carousel.setIsDeleted(false);
            carouselRepository.save(carousel);
        } else if (!carouselRepository.existsById(id)) {
            throw new CarouselException("존재하지 않는 캐러셀 아이템입니다.");
        } else {
            throw new CarouselException("이미 복구된 캐러셀 아이템입니다.");
        }
    }
}
