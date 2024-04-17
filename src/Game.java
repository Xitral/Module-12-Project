import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Game extends JFrame {
    // Player variables
    private final int playerSpeed = 3;
    private final boolean[] keys = new boolean[4];
    private PlayerState playerState = PlayerState.IDLE;
    private int[] playerPos = {300, 300};
    private int direction = 0;

    // Frame counter variables
    private int frameCounter = 0;
    private final int frameDelay = 9;
    private int currentFrame = 0;

    // Tile variables
    private final int tileSize = 32;

    // Game objects
    private SpriteSheet tileMap;
    private GamePanel gamePanel;
    private StatusPanel statusPanel;
    private SpriteSheet emotes;
    private Pet pet;

    public Game(Pet pet) {
        this.pet = pet;

        TitleScreen d = new TitleScreen();
        setupWindow();
        setupTileMap();
        setupGamePanel();
        setupPet();
        setupStatusPanel();
        setupKeyListener();

        music = new Music("music.wav");
        music.playContinuously();

        startGameLoop();

        emotes = new SpriteSheet("/resources/emotes.png", tileSize);
    }

    static Music music;

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    private void setupWindow() {
        setTitle("Untitled Pet Game - Survive!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void setupGamePanel() {
        gamePanel = new GamePanel();
        add(gamePanel);
    }

    private void setupStatusPanel() {
        this.statusPanel = new StatusPanel(this.pet);

        statusPanel.getStatusPanel().setBackground(Color.BLACK);
        add(statusPanel.getStatusPanel(), BorderLayout.SOUTH);
    }

    private void setupTileMap() {
        tileMap = new SpriteSheet("/resources/floors.png", tileSize);
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
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> keys[0] = false;
                    case KeyEvent.VK_S -> keys[1] = false;
                    case KeyEvent.VK_A -> keys[2] = false;
                    case KeyEvent.VK_D -> keys[3] = false;
                }
            }
        });
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
        updateFrameCounter();

        statusPanel.updateStatus(pet);

        if (pet.getHealth() > 0) {
            updatePlayerPosition();

            // Update the pet every minute
            if (System.currentTimeMillis() - lastUpdate[0] >= 200) {
                pet.update();
                System.out.println(pet.toString());
                lastUpdate[0] = System.currentTimeMillis();
            }

            // Update the age every 5 minutes
            if (System.currentTimeMillis() - lastUpdate[1] >= 300000) {
                pet.age();
                System.out.println(pet.toString());
                lastUpdate[1] = System.currentTimeMillis();
            }

            sleep();
        } else {
            playerState = PlayerState.DEAD;
        }

        gamePanel.repaint();
    }

    private void updatePlayerPosition() {
        int potentialPlayerX = playerPos[0];
        int potentialPlayerY = playerPos[1];

        if (keys[0]) {
            potentialPlayerY -= playerSpeed;
            direction = 1;
            playerState = PlayerState.WALKING;
        } else if (keys[1]) {
            potentialPlayerY += playerSpeed;
            direction = 2;
            playerState = PlayerState.WALKING;
        } else if (keys[2]) {
            potentialPlayerX -= playerSpeed;
            direction = 3;
            playerState = PlayerState.WALKING;
        } else if (keys[3]) {
            potentialPlayerX += playerSpeed;
            direction = 0;
            playerState = PlayerState.WALKING;
        } else {
            playerState = PlayerState.IDLE;
        }

        if (!checkCollision(potentialPlayerX, potentialPlayerY)) {
            playerPos[0] = potentialPlayerX;
            playerPos[1] = potentialPlayerY;
        }
    }

    private boolean checkCollision(int potentialPlayerX, int potentialPlayerY) {
        Rectangle playerRect = new Rectangle(potentialPlayerX, potentialPlayerY, tileSize * 2, tileSize * 2);
        Rectangle objectRect = new Rectangle(200, 200, tileSize * 2, tileSize * 2);
        return playerRect.intersects(objectRect);
    }

    private int animationDirection = 1; // 1 for forward, -1 for reverse

    private void updateFrameCounter() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            if (playerState == PlayerState.IDLE) {
                if ((currentFrame == 2 && animationDirection == 1) || (currentFrame == 0 && animationDirection == -1)) {
                    animationDirection *= -1; // Reverse the direction
                }
                currentFrame += animationDirection;
            } else if (isWalking()) {
                currentFrame = (currentFrame + 1) % 3; // Assuming you have 3 frames for walk animations
            }
        }
    }

    private boolean isWalking() {
        return keys[0] || keys[1] || keys[2] || keys[3];
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
            for (int i = 0; i < 800; i += tileSize) {
                for (int j = 0; j < 600; j += tileSize) {
                    BufferedImage tile = chooseTile(i, j);
                    bufferGraphics.drawImage(tile, i, j, tileSize, tileSize * 2, null);
                }
            }
            return buffer;
        }

        private BufferedImage chooseTile(int i, int j) {
            int tileX = i / tileSize;
            int tileY = j / tileSize;
            if (tileX == 0 || tileY == 0 || tileX == 800 / tileSize - 1 || tileY == 600 / tileSize - 1) {
                return tileMap.getTile(3, 38, 32);
            } else {
                return tileMap.getTile(1, 38, 32);
            }
        }

        private int hoverEffectCounter = 0;

        public int[] getEmotePosition() {
            int hoverEffect = (int) (Math.sin(hoverEffectCounter / 20.00) * 3);
            hoverEffectCounter++;
            if (playerState == PlayerState.IDLE) {
                return new int[]{playerPos[0] - 10, playerPos[1] + 10 + hoverEffect};
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.drawImage(buffer, 0, 0, null);

            Emote emote = pet.getEmote();
            if (emote != null) {
                int[] emotePosition = getEmotePosition();
                g2d.drawImage(emote.getImage(), emotePosition[0], emotePosition[1], 32, 32, null);
            }


            Map<String, Integer> colorMap = Map.of(
                    "White", 0,
                    "Gold", 1,
                    "Brown", 2,
                    "Black", 3
            );

            int colorIndex = colorMap.getOrDefault(pet.getColor(), 0) * 4;

            System.out.println(currentFrame);

            BufferedImage playerSprite;
            switch (playerState) {
                case WALKING -> playerSprite = pet.getSprite(direction, currentFrame + colorIndex);
                case IDLE -> playerSprite = pet.getSprite(4, currentFrame + colorIndex);
                case DEAD -> playerSprite = pet.getSprite(3, 3 + colorIndex);
                default -> playerSprite = pet.getSprite(direction, 0 + colorIndex);
            }
            g2d.drawImage(playerSprite, playerPos[0], playerPos[1], tileSize * 2, tileSize * 2, null);
        }
    }
}