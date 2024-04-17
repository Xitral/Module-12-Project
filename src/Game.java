import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static java.lang.System.exit;

public class Game extends JFrame {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int PLAYER_SPEED = 3;
    private static final int TILE_SIZE = 32;
    private static final int FRAME_DELAY = 9;

    // Player variables
    private final boolean[] keys = new boolean[4];
    private int[] playerPos = {300, 300};
    private int direction = 0;

    // Frame counter variables
    private int frameCounter = 0;
    private int currentFrame = 0;

    // Game objects
    private ArrayList<GameObject> wallList;
    private SpriteSheet floorMap;
    private SpriteSheet wallMap;
    private GamePanel gamePanel;
    private StatusPanel statusPanel;
    private SpriteSheet emotes;
    private Sound music;
    private Pet pet;
    private ArrayList<GameObject> furnitureList;
    private ArrayList<Bubble> bubbles = new ArrayList<>();

    private boolean isPaused;
    private boolean mouseHeld = false;


    public Game(TitleScreen titleScreen) {
        this.pet = titleScreen.getPet();
        this.music = titleScreen.getMusic();
        this.furnitureList = new ArrayList<>();
        this.wallList = new ArrayList<>();
        this.isPaused = false;

        setupWindow();
        setupTileMap();
        setupEnvironment();
        setupGamePanel();
        setupPet();
        setupStatusPanel();
        setupKeyListener();

        startGameLoop();

        emotes = new SpriteSheet("/resources/images/emotes.png", TILE_SIZE);
    }

    public Pet getPet() {
        return pet;
    }

    public Sound getMusic() {
        return music;
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    private void setupWindow() {
        setTitle("Untitled Pet Game - Survive!");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void setupGamePanel() {
        this.gamePanel = new GamePanel();

        JLabel label = new JLabel("[ESC]: Pause      [M]: Mute      [F]: Dance      [SPACE]: Sleep");
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        gamePanel.add(label);

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseHeld = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseHeld = false;
                }
            }
        });
        gamePanel.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseHeld && isMouseOverPet(e.getX(), e.getY())) {
                    pet.clean();
                    bubbles.add(new Bubble(e.getPoint(), (int)(Math.random() * 2 + 5), 10));
                }
            }
        });
        add(gamePanel);
    }

    private boolean isMouseOverPet(int mouseX, int mouseY) {
        int petX = playerPos[0];
        int petY = playerPos[1];
        int petWidth = TILE_SIZE * 2;
        int petHeight = TILE_SIZE * 2;

        return mouseX >= petX && mouseX <= petX + petWidth && mouseY >= petY && mouseY <= petY + petHeight;
    }

    private void setupStatusPanel() {
        this.statusPanel = new StatusPanel(this.pet);

        statusPanel.getStatusPanel().setBackground(Color.BLACK);
        add(statusPanel.getStatusPanel(), BorderLayout.SOUTH);
    }

    private void setupTileMap() {
        floorMap = new SpriteSheet("/resources/images/floors.png", TILE_SIZE);
        wallMap = new SpriteSheet("/resources/images/walls.png", TILE_SIZE);
    }

    private void setupEnvironment() {
        SpriteSheet itemMap = new SpriteSheet("/resources/images/items.png", 32);

        this.addFurniture(
                new WaterBowl(
                        new Point(400, 450),
                itemMap.getTile(3,15,32),
                true, "Water Bowl", "Water for your pet.")
        );

        this.addFurniture(
                new FoodPlate(
                        new Point(450, 450),
                        itemMap.getTile(14,15,32),
                        true, "Food Plate", "Food for your pet.")
        );

        BufferedImage wallSprite;
        for (int i = 0; i < SCREEN_WIDTH; i += TILE_SIZE) {

            wallSprite = wallMap.getTile(2+8, 10, 32);
            wallList.add(new Wall(new Point(i, 0), wallSprite)); // Top wall
        }

    }

    public void addFurniture(Furniture furniture) {
        furnitureList.add(furniture);
    }

    private void setupPet() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
                pet.getSprites()[i][j] = pet.getSpriteSheet().getSprite(j * 32, i * 32, 32, 32);
            }
        }
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> keys[0] = true;
                    case KeyEvent.VK_S -> keys[1] = true;
                    case KeyEvent.VK_A -> keys[2] = true;
                    case KeyEvent.VK_D -> keys[3] = true;
                    case KeyEvent.VK_SPACE -> pet.sleep();
                    case KeyEvent.VK_F -> pet.dance();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> keys[0] = false;
                    case KeyEvent.VK_S -> keys[1] = false;
                    case KeyEvent.VK_A -> keys[2] = false;
                    case KeyEvent.VK_D -> keys[3] = false;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_F -> pet.wake();
                    case KeyEvent.VK_ESCAPE -> isPaused = !isPaused;
                    case KeyEvent.VK_M -> music.stop();
                }
            }
        });
    }

    private void renderGameObjects(Graphics2D g2d) {
        for (GameObject furniture : furnitureList) {
            if (furniture instanceof Furniture) {
                BufferedImage sprite = furniture.getSprite();
                int width = sprite.getWidth() * 2;
                int height = sprite.getHeight() * 2;
                g2d.drawImage(sprite, furniture.getPosition().x, furniture.getPosition().y, width, height, null);
            } else {
                g2d.drawImage(furniture.getSprite(), furniture.getPosition().x, furniture.getPosition().y, null);
            }
        }

        for (GameObject wall : wallList) {
            g2d.drawImage(wall.getSprite(), wall.getPosition().x, wall.getPosition().y, null);
        }
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                playFrame();
            }
        }).start();
    }

    private final long[] lastUpdate = {0, 0};

    private void playFrame() {
        if (isPaused) {
            return;
        }

        updateFrameCounter();

        statusPanel.updateStatus(pet);

        if (pet.getHealth() > 0) {
            updatePlayerPosition();

            // Update the pet every minute
            if (System.currentTimeMillis() - lastUpdate[0] >= 500) {
                pet.update();
                lastUpdate[0] = System.currentTimeMillis();
            }

            // Update the age every minute
            if (System.currentTimeMillis() - lastUpdate[1] >= 60000) {
                pet.age();
                lastUpdate[1] = System.currentTimeMillis();
            }

            sleep();
        } else {
            gameOver();
        }

        gamePanel.repaint();
    }

    private void gameOver() {
        pet.setState(PetState.DEAD);

        try {
            FileWriter writer = new FileWriter(new File("highscores.txt"), true);
            writer.write(pet.getName() + ": " + pet.getAge() + "minutes\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, pet.getName() + " survived for " + pet.getAge() + " minute(s). Game over!");

        dispose();
        exit(0);
    }

    private void updatePlayerPosition() {
        int potentialPlayerX = playerPos[0];
        int potentialPlayerY = playerPos[1];

        // Pet can only move if it's NOT sleeping
        if (!pet.isSleeping() && !pet.isDancing()) {
            if (keys[0]) {
                potentialPlayerY -= PLAYER_SPEED;
                direction = 1;
                pet.setState(PetState.WALKING);
            } else if (keys[1]) {
                potentialPlayerY += PLAYER_SPEED;
                direction = 2;
                pet.setState(PetState.WALKING);
            } else if (keys[2]) {
                potentialPlayerX -= PLAYER_SPEED;
                direction = 3;
                pet.setState(PetState.WALKING);
            } else if (keys[3]) {
                potentialPlayerX += PLAYER_SPEED;
                direction = 0;
                pet.setState(PetState.WALKING);
            } else {
                pet.setState(PetState.IDLE);
            }
        }

        // Check if the new position is within the screen boundaries
        if (potentialPlayerX >= 0 && potentialPlayerX <= SCREEN_WIDTH - TILE_SIZE - 40 &&
                potentialPlayerY >= 0 && potentialPlayerY <= SCREEN_HEIGHT - TILE_SIZE - 110) {
            // Check if the new position collides with any furniture
            if (!checkCollision(potentialPlayerX, potentialPlayerY)) {
                playerPos[0] = potentialPlayerX;
                playerPos[1] = potentialPlayerY;
            }
        }
    }

    private boolean checkCollision(int potentialPlayerX, int potentialPlayerY) {
        Rectangle playerRect = new Rectangle(potentialPlayerX, potentialPlayerY, 32, 32);

        for (GameObject furniture : furnitureList) {
            Rectangle objectRect = new Rectangle(furniture.getPosition().x, furniture.getPosition().y, furniture.getSprite().getWidth(), furniture.getSprite().getHeight());
            if (playerRect.intersects(objectRect)) {
                if (furniture.isInteractable()) {
                    furniture.interact(this);
                }

                return true;
            }
        }

        return false;
    }

    private int animationDirection = 1; // 1 for forward, -1 for reverse

    private void updateFrameCounter() {
        frameCounter++;
        if (frameCounter >= FRAME_DELAY) {
            frameCounter = 0;

            if (pet.isIdle()) {
                if ((currentFrame == 2 && animationDirection == 1) || (currentFrame == 0 && animationDirection == -1)) {
                    animationDirection *= -1; // Reverse the direction
                }
                currentFrame += animationDirection;
            } else if (pet.isDancing()) {
                currentFrame = (currentFrame + 1) % 5; // Assuming you have 3 frames for walk animations
            } else if (pet.isWalking()) {
                currentFrame = (currentFrame + 1) % 3; // Assuming you have 3 frames for walk animations
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000 / 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class GamePanel extends JPanel {
        private final BufferedImage buffer;

        public GamePanel() {
            buffer = createBuffer();
        }

        private BufferedImage createBuffer() {
            BufferedImage buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferGraphics = buffer.createGraphics();
            for (int i = 0; i < 800; i += TILE_SIZE) {
                for (int j = 0; j < 600; j += TILE_SIZE) {
                    BufferedImage tile = chooseTile(i, j);
                    bufferGraphics.drawImage(tile, i, j, TILE_SIZE, TILE_SIZE * 2, null);
                }
            }
            return buffer;
        }

        private BufferedImage chooseTile(int i, int j) {
            int tileX = i / TILE_SIZE;
            int tileY = j / TILE_SIZE;
            if (tileX == 0 || tileY == 0 || tileX == 800 / TILE_SIZE - 1 || tileY == 600 / TILE_SIZE - 1) {
                return floorMap.getTile(3, 38, 32);
            } else {
                return floorMap.getTile(1, 38, 32);
            }
        }

        private int hoverEffectCounter = 0;

        public int[] getEmotePosition() {
            int hoverEffect = (int) (Math.sin(hoverEffectCounter / 20.00) * 3);
            hoverEffectCounter++;
            if (pet.isIdle()) {
                return new int[]{playerPos[0] - 10, playerPos[1] + 10 + hoverEffect};
            } else if(pet.isSleeping()) {
                return new int[]{playerPos[0] - 20, playerPos[1] - 10 + hoverEffect};
            } else {
                return switch (direction) {
                    case 0 -> new int[]{playerPos[0], playerPos[1] - 10 + hoverEffect};
                    case 1 -> new int[]{playerPos[0] - 10, playerPos[1] - 10 + hoverEffect};
                    case 2 -> new int[]{playerPos[0] - 10, playerPos[1] + 10 + hoverEffect};
                    case 3 -> new int[]{playerPos[0] - 20, playerPos[1] - 10 + hoverEffect};
                    default -> new int[]{playerPos[0] + 20, playerPos[1] + 20 + hoverEffect};
                };
            }
        }

        private void updatePetSprite(Graphics2D g2d) {
            Map<String, Integer> colorMap = Map.of(
                    "White", 0,
                    "Gold", 1,
                    "Brown", 2,
                    "Black", 3
            );

            int colorIndex = colorMap.getOrDefault(pet.getColor(), 0) * 4;

            // Fixes bug where % 5 wouldn't be updated to % 3 in time for next frame
            if (!pet.isDancing()) {
                currentFrame = currentFrame % 3;
            }

            BufferedImage playerSprite;
            switch (pet.getState()) {
                case WALKING -> playerSprite = pet.getSprite(direction, currentFrame + colorIndex);
                case IDLE -> playerSprite = pet.getSprite(4, currentFrame + colorIndex);
                case SLEEPING, DEAD -> playerSprite = pet.getSprite(3, 3 + colorIndex);
                case DANCING -> playerSprite = pet.getSprite(currentFrame, colorIndex);
                default -> playerSprite = pet.getSprite(direction, colorIndex);
            }

            g2d.drawImage(playerSprite, playerPos[0], playerPos[1], TILE_SIZE * 2, TILE_SIZE * 2, null);
        }

        private void updateEmotes(Graphics2D g2d) {
            Emote emote = pet.getEmote();
            if (emote != null) {
                int[] emotePosition = getEmotePosition();
                g2d.drawImage(emote.getImage(), emotePosition[0], emotePosition[1], 32, 32, null);
            }
        }

        private void drawBubbles(Graphics2D g2d) {
            for (Bubble bubble : bubbles) {
                g2d.setColor(new Color(225, 225, 255, bubble.getLifespan() * 255 / 10));
                g2d.drawOval((int) (bubble.getPosition().x + (Math.random()*10+5)-15), (int) (bubble.getPosition().y + (Math.random()*10+5)-10), bubble.getRadius(), bubble.getRadius());
            }
        }

        private void updateBubbles() {
            Iterator<Bubble> iterator = bubbles.iterator();
            while (iterator.hasNext()) {
                Bubble bubble = iterator.next();
                bubble.decreaseLifespan();
                if (bubble.getLifespan() <= 0) {
                    iterator.remove();
                }
            }
        }

        private Graphics2D setupG2D(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.drawImage(buffer, 0, 0, null);
            return g2d;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = setupG2D(g);

            renderGameObjects(g2d);
            updatePetSprite(g2d);
            updateEmotes(g2d);
            drawBubbles(g2d);
            updateBubbles();
        }
    }
}