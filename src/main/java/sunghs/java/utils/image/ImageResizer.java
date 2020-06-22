package sunghs.java.utils.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 이미지 리사이징
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class ImageResizer {

    public static final int MAX_SCALE_SIZE = 10000;

    /**
     * @param image 원본
     * @param w     리사이징 가로 (px)
     * @param h     리사이징 세로 (px)
     * @return reiszed buffered image
     */
    public BufferedImage resize(final BufferedImage image, int w, int h) {
        final int ow = image.getWidth();
        final int oh = image.getHeight();

        w = Math.min(w, MAX_SCALE_SIZE);
        h = Math.min(h, MAX_SCALE_SIZE);

        double sw = (double) w / (double) ow;
        double sh = (double) h / (double) oh;

        BufferedImage resize = new BufferedImage(w, h, image.getType());

        AffineTransform transForm = new AffineTransform();
        transForm.scale(sw, sh);
        AffineTransformOp transformOp = new AffineTransformOp(transForm, AffineTransformOp.TYPE_BICUBIC);
        return transformOp.filter(image, resize);
    }
}