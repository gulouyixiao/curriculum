package com.curriculum;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @author 陆向荣
 * @version 1.0
 * @description: 代码生成器
 * @date 2025/6/3 17:17
 */

public class CodeGenerator {
    private String url = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai";
    private String username = "root";
    private String password = "123456";

    private String tables = "shopping_cart,order_main,orders_detail";

    @Test
    public void codeGeneratorText(){
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("lxr") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                            .commentDate("yyyy-MM-dd HH:mm:ss")
                            .dateType(DateType.TIME_PACK); //设置时间类型策略
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            if(typeCode == Types.BIGINT)
                            {
                                return DbColumnType.LONG;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.curriculum") // 设置父包名
//                                .moduleName("content") // 设置父包模块名
                                .entity("model.po")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                                .xml("mapper")
                                .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")) // 设置 Mapper XML 文件生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude(tables) // 设置需要生成的表名
                                .addTablePrefix("t_") // 设置过滤表前缀
                                .entityBuilder()
                                .enableLombok()  // 启用 Lombok
                                .enableTableFieldAnnotation() // 启用字段注解
                                .controllerBuilder()
                                .enableRestStyle() // 启用 REST 风格
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
