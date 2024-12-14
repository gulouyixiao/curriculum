package com.curriculum;

import com.curriculum.model.po.Dictionary;
import com.curriculum.service.DictionaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CurriculumApiApplicationTests {

	@Autowired
	DictionaryService dictionaryService;
	@Test
	void contextLoads() {
		List<Dictionary> dictionaries = dictionaryService.queryAll();
		for (Dictionary dictionary : dictionaries) {
			System.out.println(dictionary);
		}
	}

}
