package org.example.shop.api.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.services.PriceService;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodDtoFactory {

    PriceService priceService;

    public GoodDto makeDto(GoodEntity entity) throws ExecutionException, InterruptedException {

        return makeDto(
                entity,
                priceService.getPriceInRubles(entity).get()
        );
    }

    public List<GoodDto> makeDtoToList(List<GoodEntity> entities) throws ExecutionException, InterruptedException {

        List<GoodDto> goodDtoList = new ArrayList<>();

        for (GoodEntity entity : entities) {
            goodDtoList.add(
                    makeDto(entity, priceService.getPriceInRubles(entity).get())
            );
        }

        return goodDtoList;
    }

    private GoodDto makeDto(GoodEntity entity, Long priceInRubles) {
        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceInRubles)
                .build();
    }
}