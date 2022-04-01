package sunghs.java.utils.image;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
class ImageRotatorTest {

    String dir;
    String name;
    File imageFile;
    BufferedImage bufferedImage;

    @BeforeEach
    void setup() {
        dir = "C:\\Users\\sunghs\\Desktop\\";
        name = "test.jpg";
        imageFile = new File(dir + name);
        bufferedImage = ImageFileUtils.fileToBufferedImage(imageFile);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 15, 33, 45, 80, 90, 125, 150, 180, 235, 270, 339, 353})
    void rotateClockwiseTest(int degree) throws IOException {
        Assertions.assertNotNull(bufferedImage, "bufferedImage is null");
        String resultName = "clockwise" + degree + ".jpg";
        BufferedImage result = ImageRotator.rotate(bufferedImage, ImageRotator.Direction.CLOCKWISE, degree);
        ImageIO.write(result, "jpg", new File(dir + resultName));
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 45, 90, 150, 180, 250})
    void rotateCounterClockwiseTest(int degree) throws IOException {
        Assertions.assertNotNull(bufferedImage, "bufferedImage is null");
        String resultName = "counterClockwise" + degree + ".jpg";
        BufferedImage result = ImageRotator.rotate(bufferedImage, ImageRotator.Direction.COUNTERCLOCKWISE, degree);
        ImageIO.write(result, "jpg", new File(dir + resultName));
    }

    @Test
    void getFormatTest() {
        String format = ImageFileUtils.getFormats(new File(dir + name));
        ImageFormats imageFormats = ImageFormats.valueOf(format);
        Assertions.assertEquals(ImageFormats.JPEG, imageFormats);
    }
}
