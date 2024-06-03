package com.subkore.back.carousel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
        given(carouselService.getCarouselList()).willReturn(List.of(carouselResponseDto));
        // when
        mockMvc.perform(get("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(List.of(carouselResponseDto))));
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
        given(carouselService.createCarousel(createCarouselRequestDto)).willReturn(
            CarouselResponseDto.builder()
                .id(0L)
                .order(0)
                .title("test")
                .description("test")
                .linkTo("test")
                .build());
        // when
        mockMvc.perform(post("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createCarouselRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(CarouselResponseDto.builder()
                        .id(0L)
                        .order(0)
                        .title("test")
                        .description("test")
                        .linkTo("test")
                        .build())));
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀이_없을_경우_예외가_발생한다() throws Exception {
        // given
        given(carouselService.getCarouselList()).willThrow(new CarouselException("캐러셀이 없습니다."));
        // when
        mockMvc.perform(get("/api/v1/carousels")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "캐러셀이 없습니다."));
        // then
    }
    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀_등록_시_항목이_없을_경우_예외가_던져진다() throws Exception {
        // given
        CreateCarouselRequestDto createCarouselRequestDto = CreateCarouselRequestDto.builder().build();
        given(carouselService.createCarousel(createCarouselRequestDto)).willThrow(new CarouselException("필수 항목이 "
            + "없습니다."));
        // when
        // then
        mockMvc.perform(post("/api/v1/carousels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createCarouselRequestDto)))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "필수 항목이 없습니다."));
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
        given(carouselService.updateCarousel(0L, carouselRequestDto)).willReturn(
            CarouselResponseDto.builder()
                .id(0L)
                .order(0)
                .title("test2")
                .description("test2")
                .linkTo("test2")
                .build());
        // when
        mockMvc.perform(put("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(carouselRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(CarouselResponseDto.builder()
                        .id(0L)
                        .order(0)
                        .title("test2")
                        .description("test2")
                        .linkTo("test2")
                        .build())));
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
        doNothing().when(carouselService).deleteCarousel(0L);
        // when

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
        doThrow(new CarouselException("존재하지 않는 캐러셀 아이템입니다.")).when(carouselService).deleteCarousel(0L);
        // when
        // then
        mockMvc.perform(delete("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "존재하지 않는 캐러셀 "
                + "아이템입니다."));
    }

    @Test
    @WithMockUser(username = "테스트")
    void 이미_삭제된_캐러셀을_삭제할_시_예외가_발생한다() throws Exception {
        // given
        doThrow(new CarouselException("이미 삭제된 캐러셀입니다.")).when(carouselService).deleteCarousel(0L);
        // when
        // then
        mockMvc.perform(delete("/api/v1/carousels/0")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "이미 삭제된 캐러셀입니다."));
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
        doNothing().when(carouselService).recoverCarousel(0L);

        // when

        // then
        mockMvc.perform(put("/api/v1/carousels/0/recover")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), ""));
    }

    @Test
    @WithMockUser(username = "테스트")
    void 캐러셀이_존재하지_않는_경우_복구가_불가능하고_예외가_발생한다() throws Exception {
        // given
        doThrow(new CarouselException("존재하지 않는 캐러셀 아이템입니다.")).when(carouselService).recoverCarousel(0L);
        // when

        // then
        mockMvc.perform(put("/api/v1/carousels/0/recover")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "존재하지 않는 캐러셀 "
                + "아이템입니다."));
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
        doThrow(new CarouselException("이미 복구된 캐러셀입니다.")).when(carouselService).recoverCarousel(0L);
        // when
        // then
        mockMvc.perform(put("/api/v1/carousels/0/recover")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "이미 복구된 캐러셀입니다."));
    }
}