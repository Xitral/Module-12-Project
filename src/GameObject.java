import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected Point position;
    protected BufferedImage sprite;
    protected boolean interactable;

    public GameObject(Point position, BufferedImage sprite, boolean interactable) {
        this.position = position;
        this.sprite = sprite;
        this.interactable = interactable;
    }

    public Point getPosition() {
        return position;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public abstract void interact(Game game);

}