import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Furniture extends GameObject {
    private String name;
    private String description;

    public Furniture(Point position, BufferedImage sprite, boolean interactable, String name, String description) {
        super(position, sprite, interactable);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void interact(Game game);
}
