package com.curriculum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableScheduling //开启任务调度
@EnableTransactionManagement //开启事务管理注解模式
@Slf4j
public class CurriculumApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurriculumApiApplication.class, args);
		log.info("-----------------------------服务启动成功------------------------------------");
	}

}
