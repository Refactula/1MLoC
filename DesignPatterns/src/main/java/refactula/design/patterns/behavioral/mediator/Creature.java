package refactula.design.patterns.behavioral.mediator;

import refactula.design.patterns.behavioral.mediator.shape.Shape;

public class Creature {
    private final CreatureMediator creatureMediator;

    public Creature(CreatureMediator creatureMediator) {
        this.creatureMediator = creatureMediator;
    }

    public void onAdded(World world) {
        creatureMediator.onAdded(world);
    }

    public void onRemoved(World world) {
        creatureMediator.onRemoved(world);
    }

    public float getX() {
        return creatureMediator.getX();
    }

    public float getY() {
        return creatureMediator.getY();
    }

    public Shape getShape() {
        return creatureMediator.getShape();
    }

    public float getVelocityX() {
        return creatureMediator.getVelocityX();
    }

    public float getVelocityY() {
        return creatureMediator.getVelocityY();
    }

    public boolean isAlive() {
        return creatureMediator.isActive() && creatureMediator.isAlive();
    }

    public boolean isInsideCircle(float x, float y, float radius) {
        return creatureMediator.isInsideCircle(x, y, radius);
    }

    public FleshType getFleshType() {
        return creatureMediator.getFleshType();
    }

    public void update(float deltaTime) {
        creatureMediator.updateBrain();
        creatureMediator.updateMotion(deltaTime);
        creatureMediator.updateCollisions();
    }

    public void setPosition(float x, float y) {
        creatureMediator.setPosition(x, y);
    }

    public void onEaten() {
        creatureMediator.die();
    }

    public void draw(Painter painter) {
        creatureMediator.draw(painter);
    }
}
