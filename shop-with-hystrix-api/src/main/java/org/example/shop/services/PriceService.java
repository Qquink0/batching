package org.example.shop.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.services.remote.PriceServiceApi;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PriceService {

    PriceServiceApi priceServiceApi;

    @HystrixCollapser(
            collapserKey = "getPriceInRubles",
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            batchMethod = "getGoodIdToPriceInRublesMap"
    )
    public Future<Long> getPriceInRubles(GoodEntity good) {
        return null;
    }

    @HystrixCommand(
            groupKey = "goods:price:get-all",
            commandKey = "getGoodIdToPriceInRublesMap"
    )
    public List<Long> getGoodIdToPriceInRublesMap(List<GoodEntity> goods) {

        return priceServiceApi.getGoodIdToPriceInRublesByGoodIds(
                goods.stream()
                        .map(GoodEntity::getId)
                        .toList()
        );
    }
}
