import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/*
 * This class represents a scene in the game.
 * A scene is a collection of GameObjects that the player can interact with.
 * The player can move between scenes by interacting with a GameObject that represents a door or a portal.
 * The player can also interact with other GameObjects in the scene to trigger events or change stats.
 *
 * TODO: The player can also interact with NPCs in the scene to trigger dialogues or quests.
 *  TODO: The player can also interact with items in the scene to pick them up and add them to their inventory.
 *   TODO: The player can also interact with furniture in the scene to change the layout or appearance of the scene.
 *
 * @author Xitral
 * @see GameObject
 * @see Furniture
 */
public class Scene {
    private String name;
    private List<GameObject> gameObjects;
    private BufferedImage background;
    private Point spawnPoint;

    public Scene(String name, List<GameObject> gameObjects, List<Emote> emotes, BufferedImage background, Point spawnPoint) {
        this.name = name;
        this.gameObjects = gameObjects;
        this.background = background;
        this.spawnPoint = spawnPoint;
    }

    public String getName() {
        return name;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }
}
