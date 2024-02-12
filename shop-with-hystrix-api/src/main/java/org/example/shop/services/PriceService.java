package org.example.shop.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.shop.services.remote.PriceServiceApi;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PriceService {

    PriceServiceApi priceServiceApi;

    @HystrixCollapser(
            collapserKey = "getPriceInRubles",
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            batchMethod = "getPriceInRublesByGoods"
    )
    public Future<Long> getPriceInRubles(GoodEntity good) {
        return null;
    }

    @HystrixCommand(commandKey = "getGoodIdToPriceInRublesMap")
    public List<Long> getPriceInRublesByGoods(List<GoodEntity> goods) {

        log.info("getPriceInRublesByGoods handled %s goods.".formatted(goods.size()));

        return priceServiceApi.getGoodIdToPriceInRublesByGoodIds(
                goods.stream()
                        .map(GoodEntity::getId)
                        .toList()
        );
    }
}
