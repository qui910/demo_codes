package com.example.spring.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:25
 * @since 1.8
 **/
@Component
@Slf4j
public class IocServiceImpl implements IoCService{

    @Autowired
    private IoCDao dao;

    @Override
    public void query() {
      log.info("IndexService query");
      dao.query();
    }
}
