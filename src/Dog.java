public class Dog extends Pet {
    private final Sound bark1 = new Sound("resources/sounds/bark1.wav");
    private final Sound bark2 = new Sound("resources/sounds/bark2.wav");
    public Dog(String color, String name) {
        super(new SpriteSheet("/resources/images/dog.png", 32), color, name);
    }

    public Dog(String color) {
        super(new SpriteSheet("/resources/images/dog.png", 32), color);
    }

    public Dog() {
        super(new SpriteSheet("/resources/images/dog.png", 32));
    }

    @Override
    public void speak() {
        System.out.println("Woof!");
        Sound.playRandomSound(bark1, bark2);
    }

    @Override
    public void act() {
        System.out.println("The dog is fetching a stick.");
    }

    @Override
    public void listen() {
        System.out.println("The dog is wagging its tail.");
    }
}
