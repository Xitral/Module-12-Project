import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    public Wall(Point position, BufferedImage sprite) {
        super(position, sprite, false);
    }

    @Override
    public void interact(Game game) {
        // Walls are not interactable
    }
}