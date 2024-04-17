public class Cat extends Pet {
    private final Sound meow1 = new Sound("resources/sounds/meow1.wav");
    private final Sound meow2 = new Sound("resources/sounds/meow2.wav");

    public Cat(String color, String name) {
        super(new SpriteSheet("/resources/images/cat.png", 32),color, name);
    }

    public Cat(String color) {
        super(new SpriteSheet("/resources/images/cat.png", 32), color);
    }

    public Cat() {
        super(new SpriteSheet("/resources/images/cat.png", 32));
    }

    @Override
    public void speak() {
        System.out.println("Meow!");
        Sound.playRandomSound(meow1, meow2);
    }

    @Override
    public void act() {
        System.out.println("The cat is playing with a toy.");
    }

    @Override
    public void listen() {
        System.out.println("The cat is purring.");
    }
}
