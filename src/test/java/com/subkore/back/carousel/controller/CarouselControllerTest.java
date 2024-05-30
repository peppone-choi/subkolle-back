package com.subkore.back.carousel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.carousel.dto.CarouselResponseDto;
import com.subkore.back.carousel.dto.CreateCarouselRequestDto;
import com.subkore.back.carousel.dto.UpdateCarouselRequestDto;
import com.subkore.back.carousel.entity.Carousel;
import com.subkore.back.carousel.mapper.CarouselMapper;
import com.subkore.back.carousel.repository.CarouselRepository;
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
    @MockBean
    private CarouselRepository carouselRepository;
    @Autowired
    private CarouselController carouselController;
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
    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀_등록_시_항목이_없을_경우_예외가_던져진다() throws Exception {
        // given
        CreateCarouselRequestDto createCarouselRequestDto = CreateCarouselRequestDto.builder().build();
        // when
        // then
        mockMvc.perform(post("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createCarouselRequestDto)))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀을_수정할_수_있다() throws Exception {
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
        carouselMapper.updateCarouselRequestDtoToCarousel(carouselRequestDto, carousel);
        when(carouselService.updateCarousel(0L, carouselRequestDto)).thenReturn(
            carouselMapper.carouselToCarouselResponseDto(carousel));
        // when
        mockMvc.perform(put("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(carouselRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated());
        // then
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀을_삭제할_수_있다() throws Exception {
        // given
        Carousel carousel = Carousel.builder()
            .id(0L)
            .order(0)
            .title("test")
            .description("test")
            .linkTo("test")
            .isDeleted(false)
            .build();
        // when
        doNothing().when(carouselService).deleteCarousel(0L);

        // then
        mockMvc.perform(delete("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀이_존재하지_않는_경우_삭제가_불가능하고_예외가_발생한다() throws Exception {
        // given
        // when
        when(carouselController.deleteCarousel(0L)).thenThrow(new CarouselException("존재하지 않는 캐러셀 "
            + "아이템입니다."));
        // then
        mockMvc.perform(delete("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 이미_삭제된_캐러셀을_삭제할_시_예외가_발생한다() throws Exception {
        // given
        when(carouselController.deleteCarousel(0L)).thenThrow(new CarouselException("이미 삭제된 캐러셀 "
            + "아이템입니다."));
        // when
        // then
        mockMvc.perform(delete("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀을_되살릴_수_있다() throws Exception {
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
        mockMvc.perform(put("/api/v1/carousels/0/recover")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀이_존재하지_않는_경우_복구가_불가능하고_예외가_발생한다() throws Exception {
        // given
        // when
        when(carouselRepository.existsById(0L)).thenReturn(false);
        when(carouselRepository.findById(0L)).thenReturn(java.util.Optional.empty());
        when(carouselController.recoverCarousel(0L)).thenThrow(new CarouselException("존재하지 않는 캐러셀 "
            + "아이템입니다."));
        // then
        mockMvc.perform(put("/api/v1/carousels/0/recover")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "테스트")
void 이미_복구된_캐러셀을_복구할_시_예외가_발생한다() throws Exception {
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
        mockMvc.perform(put("/api/v1/carousels/0/recover/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}