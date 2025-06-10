package com.curriculum;

import com.curriculum.controller.UserController;
import com.curriculum.controller.VideoBaseController;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.po.Dictionary;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.DictionaryService;
import com.curriculum.service.MailService;
import com.curriculum.service.MediaFilesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = CurriculumApiApplication.class)
class CurriculumApiApplicationTests {

	@Autowired
	DictionaryService dictionaryService;

	@Autowired
	UserController userController;

	@Autowired
	VideoBaseController videoBaseService;

	@Autowired
	MediaFilesService mediaFilesService;

	@Autowired
	MailService mailService;

	@Test
	void contextLoads() {
		List<Dictionary> dictionaries = dictionaryService.queryAll();
		for (Dictionary dictionary : dictionaries) {
			System.out.println(dictionary);
		}
	}



//	@Test
//	void getCode2() {
//		mediaFilesService.getVideoBasePage()
//	}

	@Test
	void getCode2() {

	}

}
