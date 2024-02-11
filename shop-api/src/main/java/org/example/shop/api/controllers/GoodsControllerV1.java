package org.example.shop.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.controllers.exceptions.NotFoundException;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.api.factories.GoodDtoFactory;
import org.example.shop.store.repositories.GoodRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class GoodsControllerV1 {

    GoodRepository goodRepository;
    GoodDtoFactory goodDtoFactory;

    public static final String GET_ALL_GOODS = "/api/goods/v1";

    @GetMapping(GET_ALL_GOODS)
    public List<GoodDto> getAllGoods() {
        return goodDtoFactory.makeDtoToList(goodRepository.findAll());
    }

}
