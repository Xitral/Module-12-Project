import java.awt.*;

public class Bubble {
    private Point position;
    private int radius;
    private int lifespan;

    public Bubble(Point position, int radius, int lifespan) {
        this.position = position;
        this.radius = radius;
        this.lifespan = lifespan;
    }

    public Point getPosition() {
        return position;
    }

    public int getRadius() {
        return radius;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void decreaseLifespan() {
        lifespan--;
    }
}