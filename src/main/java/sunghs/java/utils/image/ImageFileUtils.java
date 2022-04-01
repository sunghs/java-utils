package sunghs.java.utils.image;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Image 관련 Utils
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
@Slf4j
public class ImageFileUtils {

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

    public static String getFormats(final File file) {
        List<String> formats = new ArrayList<>();
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

            while (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                formats.add(reader.getFormatName());
            }
        } catch (Exception e) {
            log.error("image file get format error", e);
        }
        return formats.get(0);
    }
}
