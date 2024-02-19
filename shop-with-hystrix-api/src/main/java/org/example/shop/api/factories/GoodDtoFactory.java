package org.example.shop.api.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.api.dto.GoodDto;
import org.example.shop.services.PriceService;
import org.example.shop.store.entities.GoodEntity;
import org.example.shop.utils.FutureUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public List<GoodDto> makeDtoToList(List<GoodEntity> entities) {

        List<GoodDto> goodDtoList = new ArrayList<>();

        List<CompletableFuture<Long>> futures = new ArrayList<>();

        for (GoodEntity entity : entities) {

            CompletableFuture<Long> future = FutureUtils
                    .makeCompletableFuture(priceService.getPriceInRubles(entity));

            futures.add(future);

            GoodDto goodDto = makeBaseDto(entity);

            goodDtoList.add(goodDto);

            // TODO: Если для товара не была возвращена цена - нужно что-то с этим сделать
            future.whenComplete((priceInRubles, e) -> goodDto.setPriceInRubles(priceInRubles));
        }

        futures.forEach(CompletableFuture::join);

        return goodDtoList;
    }

    private GoodDto makeDto(GoodEntity entity, Long priceInRubles) {
        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceInRubles)
                .build();
    }

    private GoodDto makeBaseDto(GoodEntity entity) {
        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
