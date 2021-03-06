package refactula.design.patterns.behavioral.mediator;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class World {
    private final float width;
    private final float height;

    private final Collection<Creature> creatures = new HashSet<>();

    public World(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void add(Creature creature) {
        creatures.add(creature);
        creature.onAdded(this);
    }

    public void remove(Creature creature) {
        creature.onRemoved(this);
        creatures.remove(creature);
    }

    public boolean isInsideBounds(float x, float y) {
        return 0 <= x && x <= width && 0 <= y && y <= height;
    }

    public float restrictX(float x) {
        return Math.max(0, Math.min(x, width));
    }

    public float restrictY(float y) {
        return Math.max(0, Math.min(y, height));
    }

    public Iterable<Creature> getCreaturesInRadius(float x, float y, float radius) {
        return () -> creatures.stream().filter(creature -> creature.isInsideCircle(x, y, radius)).iterator();
    }

    public Collection<Creature> getCreatures() {
        return Collections.unmodifiableCollection(creatures);
    }

    public void update(float deltaTime) {
        creatures.forEach(creature -> creature.update(deltaTime));
        for (Iterator<Creature> iterator = creatures.iterator(); iterator.hasNext(); ) {
            Creature creature = iterator.next();
            if (!creature.isAlive()) {
                iterator.remove();
                creature.onRemoved(this);
            }
        }
    }

    public void draw(Painter painter) {
        creatures.forEach(creature ->  creature.draw(painter));
    }
}
