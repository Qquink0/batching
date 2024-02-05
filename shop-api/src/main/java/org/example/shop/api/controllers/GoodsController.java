package org.example.shop.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.api.controllers.exceptions.NotFoundException;
import org.example.shop.api.factories.GoodDtoFactory;
import org.example.shop.store.repositories.GoodRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class GoodsController {

    GoodRepository goodRepository;

    GoodDtoFactory goodDtoFactory;

    public static final String GET_ALL_GOODS = "/api/goods";

    public static final String GET_GOOD = "/api/goods{good_id}";

    @GetMapping(GET_ALL_GOODS)
    public List<GoodDto> getAllGoods() {

        return goodRepository
                .findAll()
                .stream()
                .map(goodDtoFactory::makeDto)
                .toList();
    }

    @GetMapping(GET_GOOD)
    public GoodDto getGood(@PathVariable("good_id") Long good_id) {

        return goodRepository
                .findById(good_id)
                .map(goodDtoFactory::makeDto)
                .orElseThrow(() -> {
                    throw new NotFoundException("Good not found");
                });
    }
}
