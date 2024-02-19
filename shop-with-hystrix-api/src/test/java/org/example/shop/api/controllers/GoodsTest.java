package org.example.shop.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.store.entities.GoodEntity;
import org.example.shop.store.repositories.GoodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class GoodsTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    GoodRepository goodRepository;

    @Test
    void should_Pass_When_GetAllGoodsByController() throws Exception {

        Long goodsCount = goodRepository.countAll();

        long getAllGoodsFromControllerStartTime = System.currentTimeMillis();

        byte[] contentAsByteArray = mvc.perform(
                        get(GoodsController.GET_ALL_GOODS)
                )
                .andExpect(jsonPath("$.size()").value(goodsCount))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();


        log.info("All goods fetched by %s millis (controller)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromControllerStartTime));

        List<GoodDto> goodDtoList = mapper.readValue(contentAsByteArray, new TypeReference<>() {});

        List<GoodEntity> goodEntities = goodRepository.findAll();

        Iterator<GoodDto> goodDtoIterator = goodDtoList.iterator();
        Iterator<GoodEntity> goodEntityIterator = goodEntities.iterator();

        do {

            boolean hasNextGoodDto = goodDtoIterator.hasNext();
            boolean hasNextGoodEntity = goodEntityIterator.hasNext();

            Assertions.assertEquals(hasNextGoodDto, hasNextGoodEntity);

            if (!hasNextGoodEntity) {
                break;
            }

            GoodDto nextGoodDto = goodDtoIterator.next();
            GoodEntity nextGoodEntity = goodEntityIterator.next();

            Assertions.assertEquals(nextGoodDto.getId(), nextGoodEntity.getId());

        } while (true);
    }

    @Test
    void should_Pass_When_GetAllGoodsByRepository() {

        long getAllGoodsFromRepositoryStartTine = System.currentTimeMillis();

        List<GoodEntity> goods = goodRepository.findAll();

        log.info("All goods fetched by %s millis (repository)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromRepositoryStartTine));
    }

}