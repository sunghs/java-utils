package sunghs.java.utils.image;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

class ImageResizerTest {

    @Test
    void resizeTest() throws Exception {
        File original = new File("/Users/sunghs/Documents/Example/docker-image.png");
        InputStream in = new FileInputStream(original);

        ImageResizer imageResizer = new ImageResizer();
        int w = 1000;
        int h = 1000;
        BufferedImage resize = imageResizer.resize(ImageIO.read(in), w, h);

        String option = w + "x" + h;
        FileOutputStream out = new FileOutputStream("/Users/sunghs/Documents/Example/docker-image-" + option + ".png");
        ImageIO.write(resize, "png", out);
        out.flush();
    }
}
