package com.curriculum.service.impl;

import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.utils.CheckCodeUtils;
import com.curriculum.utils.EncryptUtil;
import com.curriculum.utils.RedisCheckCodeStore;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @description 图片验证码
 */
@Service("pic_CheckCodeService")
@Slf4j
public class PicCheckCodeServiceImpl implements CheckCodeService{
	@Autowired
	private DefaultKaptcha kaptcha;

	@Autowired
	private RedisCheckCodeStore redisCheckCodeStore;

	private String createPic(String code) {
		// 生成图片验证码
		ByteArrayOutputStream outputStream = null;
		BufferedImage image = kaptcha.createImage(code);

		outputStream = new ByteArrayOutputStream();
		String imgBase64Encoder = null;
		try {
			// 对字节数组Base64编码
			Base64.Encoder encoder = Base64.getEncoder();
			ImageIO.write(image, "png", outputStream);
			imgBase64Encoder = "data:image/png;base64," + EncryptUtil.encodeBase64(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imgBase64Encoder;
	}

	@Override
	public CheckCodeResult execute(CheckCodeParamsDto checkCodeParamsDto) {
		log.info("生成验证码");
		CheckCodeResult generate = CheckCodeUtils.generate( 4, "checkcode:",true);
		String key = generate.getKey();
		String code = generate.getCaptcha();
		//存储验证码
		redisCheckCodeStore.set(key,code,300);

		//生成图片
		String pic = createPic(code);
		CheckCodeResult checkCodeResult = new CheckCodeResult();
		checkCodeResult.setCaptcha(pic);
		checkCodeResult.setKey(key);
		log.info("生成验证码成功");
		return checkCodeResult;
	}
}
