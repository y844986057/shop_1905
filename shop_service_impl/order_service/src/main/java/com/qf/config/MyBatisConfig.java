package com.qf.config;

import com.qf.datasource.Order1DataSource;
import com.qf.datasource.Order2DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;

@Configuration
public class MyBatisConfig {
    @Autowired
    private Order1DataSource order1DataSource;
    @Autowired
    private Order2DataSource order2DataSource;

    @Value("${mybatis.mapper-locations}")
    private String locations;

    @Bean
    public DynamicDataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> map = new HashMap<>();
        map.put(order1DataSource.getAliases(),order1DataSource.getDataSource());
        map.put(order2DataSource.getAliases(),order2DataSource.getDataSource());
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dynamicDataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.qf.entity");
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locations));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactoryBean;

    }

}
