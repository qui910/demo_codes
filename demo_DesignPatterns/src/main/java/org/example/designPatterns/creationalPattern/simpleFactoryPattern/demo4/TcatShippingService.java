package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @date 2023-08-09 14:47
 * @since 1.8
 **/
@Service("tCatShip")
@Slf4j
public class TcatShippingService implements ShippingService {
    @Override
    public void ship(String orderId) {
        log.info("黑貓出貨, orderId={}",orderId);
    }
}
