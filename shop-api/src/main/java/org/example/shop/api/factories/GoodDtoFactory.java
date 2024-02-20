package org.example.shop.api.factories;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.services.PriceService;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodDtoFactory {

    PriceService priceService;

    public GoodDto makeDto(GoodEntity entity) {

        return makeDto(entity, priceService.getPriceInRubles(entity));

    }

    public List<GoodDto> makeDtoToList(List<GoodEntity> entities) {

        Map<Long, Long> goodIdToPriceInRublesMap = new HashMap<>();

        Lists
                .partition(entities, 25)
                .forEach(entitiesBatch ->
                        goodIdToPriceInRublesMap.putAll(
                                priceService.getGoodIdToPriceInRublesMap(entitiesBatch)
                        )
                );


        return entities.stream()
                .map(entity -> makeDto(entity, goodIdToPriceInRublesMap.get(entity.getId())))
                .toList();
    }

    private GoodDto makeDto(GoodEntity entity, Long priceInRubles) {
        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceInRubles)
                .build();
    }
}
