package com.sample.api.core.configs;

import com.sample.api.core.utils.FileUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Configuration
@Transactional
@MapperScan(
        basePackages = {"com.sample.api.repository.ora02"},
        sqlSessionFactoryRef = "ora02SqlSessionFactory",
        sqlSessionTemplateRef = "ora02SqlSessionTemplate")
public class Ora02MybatisConfiguration {

    @Bean
    SqlSessionFactory ora02SqlSessionFactory(@Qualifier("ora02DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(FileUtils.fetchResource("classpath:config/mapper/mybatis-config-ora02.xml"));

        String[] mapperLocations = {
                "classpath*:config/mapper/ora02/*Mapper.xml"
        };

        sqlSessionFactoryBean.setMapperLocations(FileUtils.fetchResources(mapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    SqlSessionTemplate ora02SqlSessionTemplate(@Qualifier("ora02SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    PlatformTransactionManager ora02TransactionManager(@Qualifier("ora02DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
