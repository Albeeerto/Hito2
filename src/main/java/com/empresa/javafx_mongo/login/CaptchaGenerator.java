package com.empresa.javafx_mongo.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class CaptchaGenerator {

    private final DefaultKaptcha kaptchaProducer;

    public CaptchaGenerator() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "5");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial");

        Config config = new Config(properties);
        kaptchaProducer = new DefaultKaptcha();
        kaptchaProducer.setConfig(config);
    }

    public String createCaptchaText() {
        return kaptchaProducer.createText();
    }

    public InputStream createCaptchaImage(String text) throws Exception {
        BufferedImage image = kaptchaProducer.createImage(text);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        return new ByteArrayInputStream(os.toByteArray());
    }
}