package com.curriculum.service.impl;

import com.curriculum.model.dto.CheckCodeParamsDto;
import com.curriculum.model.vo.CheckCodeResult;
import com.curriculum.service.CheckCodeService;
import com.curriculum.utils.EncryptUtil;
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
 * @description 图片验证码生成器
 */
@Service("PicCheckCodeService")
@Slf4j
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService implements CheckCodeService {
	@Autowired
	private DefaultKaptcha kaptcha;

	@Override
	@PostConstruct
	public void init() {
		this.checkCodeGenerator = new NumberLetterCheckCodeGenerator();
	}


	/**
	 * 生成数字字母的验证码生成器，抽取出去最好
	 */
	public class NumberLetterCheckCodeGenerator implements CheckCodeService.CheckCodeGenerator {
		@Override
		public String generate(int length) {
			String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			Random random=new Random();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<length;i++){
				int number=random.nextInt(36);
				sb.append(str.charAt(number));
			}
			return sb.toString();
		}
	}

	@Override
	public CheckCodeResult generate(CheckCodeParamsDto checkCodeParamsDto) {
		log.info("生成验证码");
		GenerateResult generate = generate(checkCodeParamsDto, 4, "checkcode:", 300);
		String key = generate.getKey();
		String code = generate.getCode();
		String pic = createPic(code);
		CheckCodeResult checkCodeResult = new CheckCodeResult();
		checkCodeResult.setCaptcha(pic);
		checkCodeResult.setKey(key);
		log.info("生成验证码成功");
		return checkCodeResult;
	}

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
}
