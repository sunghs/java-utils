package sunghs.java.utils.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

class ImageResizerTest {

    private String originalFilePath;

    private String resizedFilePrefix;

    private String resizedFileExt;

    @BeforeEach
    void setUp() {
        originalFilePath = "/Users/sunghs/Documents/Example/docker-image.png";
        resizedFilePrefix = "/Users/sunghs/Documents/Example/docker-image-";
        resizedFileExt = ".png";
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void resizeTest() throws Exception {
        File original = new File(originalFilePath);
        InputStream in = new FileInputStream(original);

        ImageResizer imageResizer = new ImageResizer();
        int w = 1000;
        int h = 1000;
        BufferedImage resize = imageResizer.resize(ImageIO.read(in), w, h);

        String option = w + "x" + h;
        FileOutputStream out = new FileOutputStream(resizedFilePrefix + option + resizedFileExt);
        ImageIO.write(resize, "png", out);
        out.flush();
    }
}
