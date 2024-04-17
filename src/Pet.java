import java.awt.image.BufferedImage;

public abstract class Pet {
    private SpriteSheet spriteSheet;
    private BufferedImage[][] sprites;
    private final SpriteSheet emotes = new SpriteSheet("/resources/emotes.png", 16);
    private String name;
    private String color;
    private int age;
    private int health;
    private int happiness;
    private int hunger;
    private int thirst;
    private int energy;
    private int cleanliness;


    // Full Pet constructor
    public Pet(SpriteSheet spriteSheet, String color, String name) {
        this.spriteSheet = spriteSheet;
        this.sprites = new BufferedImage[5][16];
        this.color = color;
        this.name = name;
        this.age = 0;
        this.health = 100;
        this.happiness = 100;
        this.hunger = 0;
        this.thirst = 0;
        this.energy = 100;
        this.cleanliness = 100;
    }

    // Pet (w/ color) constructor
    public Pet(SpriteSheet spriteSheet, String color) {
        this.spriteSheet = spriteSheet;
        this.sprites = new BufferedImage[5][16];
        this.color = color;
    }

    // Anonymous constructor
    public Pet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        this.sprites = new BufferedImage[5][16];
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void adjustStat(String stat, int value) {
        switch (stat) {
            case "health" -> this.health = value >= 0 ? Math.min(this.health + value, 100) : Math.max(this.health + value, 0);
            case "happiness" -> this.happiness = value >= 0 ? Math.min(this.happiness + value, 100) : Math.max(this.happiness + value, 0);
            case "hunger" -> this.hunger = value >= 0 ? Math.min(this.hunger + value, 100) : Math.max(this.hunger + value, 0);
            case "thirst" -> this.thirst = value >= 0 ? Math.min(this.thirst + value, 100) : Math.max(this.thirst + value, 0);
            case "energy" -> this.energy = value >= 0 ? Math.min(this.energy + value, 100) : Math.max(this.energy + value, 0);
            case "cleanliness" -> this.cleanliness = value >= 0 ? Math.min(this.cleanliness + value, 100) : Math.max(this.cleanliness + value, 0);
            default -> throw new IllegalArgumentException("Invalid stat: " + stat);
        }
    }

    public void eat() {
        adjustStat("hunger", -100);
    }

    public void drink() {
        adjustStat("thirst", -100);
    }

    public void play() {
        adjustStat("happiness", 15);
        adjustStat("energy", -10);
    }
    public void bore() {
        adjustStat("happiness", 10);
    }

    public void sleep() {
        energy = 100;
    }

    public void clean() {
        cleanliness = 100;
    }

    public void age() {
        age++;
    }

    public void heal() {
        health = 100;
    }

    public void update() {
        adjustStat("hunger", 1);
        adjustStat("thirst", 1);
        adjustStat("energy", -1);
        adjustStat("cleanliness", -1);
        if (hunger >= 100 || thirst >= 100 || energy <= 0 || cleanliness <= 0) {
            adjustStat("health", -5);
        }
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public BufferedImage[][] getSprites() {
        return sprites;
    }

    public BufferedImage getSprite(int x, int y) {
        return sprites[x][y];
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getHealth() {
        return health;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public boolean isHappy() {
        return happiness >= 50;
    }

    public boolean isHungry() {
        return hunger >= 50;
    }

    public boolean isThirsty() {
        return thirst >= 50;
    }

    public boolean isSick() {
        return health <= 50;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Emote getEmote() {
        if (isDead()) {
            return null; // Dead pets don't have emotes :(
        } else if (!isHappy()) {
            return new Emote(emotes.getTile(3,1, 16), "happy");
        } else if (isHungry()) {
            return new Emote(emotes.getTile(4,6, 16), "hungry");
        } else if (isThirsty()) {
            return new Emote(emotes.getTile(1, 4, 16), "thirsty");
        } else if (isSick()) {
            return new Emote(emotes.getTile(2, 4, 16), "sick");
        } else {
            return null; // No emote
        }
    }

    public abstract void speak();

    public abstract void act();

    public abstract void listen();

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", health=" + health +
                ", happiness=" + happiness +
                ", hunger=" + hunger +
                ", thirst=" + thirst +
                ", energy=" + energy +
                ", cleanliness=" + cleanliness +
                '}';
    }


}
