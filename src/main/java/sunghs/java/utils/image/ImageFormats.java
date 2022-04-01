package sunghs.java.utils.image;

public enum ImageFormats {

    JPEG("jpg"),
    BMP("bmp"),
    GIF("gif"),
    PNG("png"),
    TIFF("tiff"),
    WBMP("wbmp");

    public final String name;

    ImageFormats(String name) {
        this.name = name;
    }
}
