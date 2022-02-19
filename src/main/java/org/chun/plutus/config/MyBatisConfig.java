package org.chun.plutus.config;

import org.chun.plutus.common.constant.MyBatisConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = MyBatisConst.BASE_PACKAGE,
    sqlSessionFactoryRef = MyBatisConst.SQL_SESSION_FACTORY)
public class MyBatisConfig {
}
