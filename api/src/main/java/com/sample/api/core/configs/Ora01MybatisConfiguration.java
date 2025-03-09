package com.sample.api.core.configs;

import com.sample.api.core.utils.FileUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Configuration
@Transactional
@MapperScan(
        basePackages = {"com.sample.api.repository.ora01"},
        sqlSessionFactoryRef = "ora01SqlSessionFactory",
        sqlSessionTemplateRef = "ora01SqlSessionTemplate")
public class Ora01MybatisConfiguration {

    @Primary
    @Bean
    SqlSessionFactory ora01SqlSessionFactory(@Qualifier("ora01DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(FileUtils.fetchResource("classpath:config/mapper/mybatis-config-ora01.xml"));

        String[] mapperLocations = {
                "classpath*:config/mapper/ora01/*Mapper.xml"
        };

        sqlSessionFactoryBean.setMapperLocations(FileUtils.fetchResources(mapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean
    SqlSessionTemplate ora01SqlSessionTemplate(@Qualifier("ora01SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean
    PlatformTransactionManager ora01TransactionManager(@Qualifier("ora01DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
