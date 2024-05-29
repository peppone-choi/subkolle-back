package com.subkore.back.carousel.controller;

import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import com.subkore.back.carousel.service.CarouselService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    @GetMapping("/api/v1/carousels")
    public ResponseEntity<List<CarouselResponseDto>> getCarouselList() {
        List<CarouselResponseDto> result = carouselService.getCarouselList();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/api/v1/carousels")
    public ResponseEntity<CarouselResponseDto> createCarousel(
        @RequestBody CreateCarouselRequestDto createCarouselRequestDto) {
        CarouselResponseDto result = carouselService.createCarousel(createCarouselRequestDto);
        return ResponseEntity.created(URI.create("/carousels/" + result.id())).body(result);
    }

    @PutMapping("/api/v1/carousels/{id}")
    public ResponseEntity<CarouselResponseDto> updateCarousel(@PathVariable("id") Long id,
        @RequestBody UpdateCarouselRequestDto updateCarouselRequestDto) {
        CarouselResponseDto result = carouselService.updateCarousel(id, updateCarouselRequestDto);
        return ResponseEntity.created(URI.create("/carousels/" + result.id())).body(result);
    }

}
