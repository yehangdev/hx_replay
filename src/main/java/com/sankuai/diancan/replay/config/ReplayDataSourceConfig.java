package com.sankuai.diancan.replay.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ReplayDataSourceConfig {

    @Value("${spring.datasource.replay.url}")
    private String replayUrl;

    @Value("${spring.datasource.replay.username}")
    private String replayUsername;

    @Value("${spring.datasource.replay.password}")
    private String replayPassword;

    @Bean
    @Primary
    public DataSource replayDataSource() {
        return buildDruid(replayUrl, replayUsername, replayPassword);
    }

    @Bean
    public JdbcTemplate replayJdbcTemplate(@Qualifier("replayDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private DruidDataSource buildDruid(String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setInitialSize(2);
        dataSource.setMinIdle(2);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(30000);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }
}
