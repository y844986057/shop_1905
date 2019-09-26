package com.qf.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;

import javax.sql.DataSource;

@Data
public class BaseDataSource {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String aliases;

public DataSource getDataSource(){
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setUsername(username);
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setPassword(password);
    hikariDataSource.setDriverClassName(driverClassName);
    return hikariDataSource;


}




}
