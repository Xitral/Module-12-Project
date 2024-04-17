import java.awt.*;
import java.awt.image.BufferedImage;

public class FoodPlate extends Furniture {

    public FoodPlate(Point position, BufferedImage sprite, boolean interactable, String name, String description) {
        super(position, sprite, interactable, name, description);
    }

    @Override
    public void interact(Game game) {
        game.getPet().adjustStat("hunger", -20);
    }
}