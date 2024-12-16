package com.curriculum;

import com.curriculum.controller.UserController;
import com.curriculum.model.po.Dictionary;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.DictionaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CurriculumApiApplicationTests {

	@Autowired
	DictionaryService dictionaryService;

	@Autowired
	UserController userController;


	@Test
	void contextLoads() {
		List<Dictionary> dictionaries = dictionaryService.queryAll();
		for (Dictionary dictionary : dictionaries) {
			System.out.println(dictionary);
		}
	}


	@Test
	void getCode() {
		RestResponse<CheckCodeResult> code = userController.getCode();
		System.out.println(code.getData());
	}

}
