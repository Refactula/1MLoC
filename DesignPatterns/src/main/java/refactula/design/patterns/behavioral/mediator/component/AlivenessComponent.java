package refactula.design.patterns.behavioral.mediator.component;

public class AlivenessComponent extends CreatureComponent {
    private boolean isAlive = true;

    public void die() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
