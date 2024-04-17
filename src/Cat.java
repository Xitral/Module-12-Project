public class Cat extends Pet {
    public Cat(String color, String name) {
        super(new SpriteSheet("/resources/cat.png", 32),color, name);
    }

    public Cat(String color) {
        super(new SpriteSheet("/resources/cat.png", 32), color);
    }

    public Cat() {
        super(new SpriteSheet("/resources/cat.png", 32));
    }

    @Override
    public void speak() {
        System.out.println("Meow!");
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
