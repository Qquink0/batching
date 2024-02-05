package org.example.shop.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shop.store.entities.GoodEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PriceServiceApi {

    private static final Map<Long, Long> GOOD_ID_TO_PRICE_IN_RUBLES = new ConcurrentHashMap<>();

    public Long getPriceInRubles(GoodEntity good) {

        // Обращение к отдельному микросервису
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        return GOOD_ID_TO_PRICE_IN_RUBLES.computeIfAbsent(
                good.getId(),
                goodId -> new Random().nextLong(10, 40)
        );
    }
}
