import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage sheet;
    private int tileSize;


    public SpriteSheet(String path, int tileSize) {
        this.tileSize = tileSize;
        try {
            sheet = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }

    public BufferedImage getSheet() {
        return sheet;
    }

    public BufferedImage getTile(int column, int row, int tileSize) {
        int x = (column - 1) * tileSize;
        int y = (row - 1) * tileSize;
        return sheet.getSubimage(x, y, tileSize, tileSize);
    }
}