package com.subkore.back.carousel.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.mapper.CarouselMapper;
import com.subkore.back.carousel.service.CarouselService;
import com.subkore.back.exception.CarouselException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CarouselController.class)
@AutoConfigureWebMvc
class CarouselControllerTest {

    private final CarouselMapper carouselMapper = Mappers.getMapper(CarouselMapper.class).INSTANCE;
    @MockBean
    private CarouselService carouselService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀_리스트를_확인할_수_있다() throws Exception {
        // given
        CarouselResponseDto carouselResponseDto = CarouselResponseDto.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .build();
        when(carouselService.getCarouselList()).thenReturn(List.of(carouselResponseDto));
        // when
        mockMvc.perform(get("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀을_등록할_수_있다() throws Exception {
        // given
        CreateCarouselRequestDto createCarouselRequestDto = CreateCarouselRequestDto.builder()
            .title("test")
            .description("test")
            .linkTo("test")
            .build();
        CarouselResponseDto carouselResponseDto =
            carouselMapper.carouselToCarouselResponseDto(
                carouselMapper.createCarouselRequestDtoToCarousel(createCarouselRequestDto));
        when(carouselService.createCarousel(createCarouselRequestDto)).thenReturn(
            carouselResponseDto);
        // when
        mockMvc.perform(post("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createCarouselRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀이_없을_경우_예외가_발생한다() throws Exception {
        // given
        when(carouselService.getCarouselList()).thenThrow(
            new CarouselException("캐러셀 아이템이 존재하지 않습니다."));
        // when
        mockMvc.perform(get("/api/v1/carousels")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
        // then
    }
}