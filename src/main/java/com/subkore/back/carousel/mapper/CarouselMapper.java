package com.subkore.back.carousel.mapper;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarouselMapper {

    CarouselMapper INSTANCE = Mappers.getMapper(CarouselMapper.class);

    CarouselResponseDto carouselToCarouselResponseDto(Carousel carousel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isShow", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "order", ignore = true)
    Carousel createCarouselRequestDtoToCarousel(CreateCarouselRequestDto createCarouselRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateCarouselRequestDtoToCarousel(UpdateCarouselRequestDto updateCarouselRequestDto,
        @MappingTarget Carousel carousel);
}
