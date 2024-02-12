package org.example.shop.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.controllers.exceptions.InternalServerErrorException;
import org.example.shop.api.controllers.exceptions.NotFoundException;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.api.factories.GoodDtoFactory;
import org.example.shop.store.repositories.GoodRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
        try {
            return goodDtoFactory.makeDtoToList(goodRepository.findAll());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException(e.getCause());
        }
    }

    @GetMapping(GET_GOOD)
    public GoodDto getGood(@PathVariable("good_id") Long good_id) {

        return goodRepository.findById(good_id).map(entity -> {
            try {
                return goodDtoFactory.makeDto(entity);
            } catch (ExecutionException | InterruptedException e) {
                throw new InternalServerErrorException(e.getCause());
            }
        }).orElseThrow(() -> {
            throw new NotFoundException("Good not found");
        });
    }
}
