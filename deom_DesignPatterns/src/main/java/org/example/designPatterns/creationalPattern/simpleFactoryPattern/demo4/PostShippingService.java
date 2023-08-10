package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @date 2023-08-09 14:48
 * @since 1.8
 **/
@Service("postShip")
@Slf4j
public class PostShippingService implements ShippingService {
    @Override
    public void ship(String orderId) {
        log.info("郵局出貨, orderId={}",orderId);
    }
}
