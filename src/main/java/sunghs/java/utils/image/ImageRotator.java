package sunghs.java.utils.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Image 회전 처리
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class ImageRotator {

    public enum Direction {

        // 시계방향 (오른쪽)
        CLOCKWISE(1),
        // 반시계방향 (왼쪽)
        COUNTERCLOCKWISE(-1);

        public final int direction;

        Direction(final int direction) {
            this.direction = direction;
        }
    }

    public static BufferedImage rotate(BufferedImage bufferedImage, Direction direction, int degree) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        double theta = Math.toRadians(degree);
        double sinTheta = Math.abs(Math.sin(theta));
        double cosTheta = Math.abs(Math.cos(theta));

        int newWidth = (int) Math.floor((double) width * cosTheta + (double) height * sinTheta);
        int newHeight = (int) Math.floor((double) height * cosTheta + (double) width * sinTheta);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate((double) (newWidth - width) / 2, (double) (newHeight - height) / 2);
        affineTransform.rotate(theta * direction.direction, (double) width / 2, (double) height / 2);

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        return affineTransformOp.filter(bufferedImage, new BufferedImage(newWidth, newHeight, bufferedImage.getType()));
    }
}
