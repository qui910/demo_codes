package com.example.spring.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:28
 * @since 1.8
 **/
@Component
@Slf4j
public class IocDaoImpl implements IoCDao {
    @Override
    public void query() {
        log.info("IndexDao query");
    }
}
