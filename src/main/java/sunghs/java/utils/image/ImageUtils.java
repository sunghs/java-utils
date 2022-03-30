package sunghs.java.utils.image;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Image 관련 Utils
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
@Slf4j
public class ImageUtils {

    public static BufferedImage fileToBufferedImage(final File file) {
        if (file == null) {
            return null;
        }
        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            log.error("file to bufferedImage error", e);
            return null;
        }
    }

    public static BufferedImage copyImage(final BufferedImage bufferedImage) {
        return new BufferedImage(bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getType());
    }

    public static double getTheta(final int degree) {
        if (degree >= 1 && degree < 360) {
            return Math.PI / (double) (degree / 180);
        } else {
            return Math.PI;
        }
    }
}
