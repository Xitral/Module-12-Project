public class Dog extends Pet {
    public Dog(String color, String name) {
        super(new SpriteSheet("/resources/dog.png", 32), color, name);
    }

    public Dog(String color) {
        super(new SpriteSheet("/resources/dog.png", 32), color);
    }

    public Dog() {
        super(new SpriteSheet("/resources/dog.png", 32));
    }

    @Override
    public void speak() {
        System.out.println("Woof!");
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
