package org.example.shop.api.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.services.PriceServiceApi;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodDtoFactory {

    PriceServiceApi priceServiceApi;

    public GoodDto makeDto(GoodEntity entity) {
        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceServiceApi.getPriceInRubles(entity))
                .build();
    }
}
