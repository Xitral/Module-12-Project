import java.awt.image.BufferedImage;

public class Emote {
    private BufferedImage image;
    private String action;

    public Emote(BufferedImage image, String action) {
        this.image = image;
        this.action = action;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getAction() {
        return action;
    }
}