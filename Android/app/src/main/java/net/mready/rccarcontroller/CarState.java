package net.mready.rccarcontroller;

/**
 * Created by andreivasilescu on 29/05/15.
 */

public class CarState {
    public enum Traction {
        NONE,
        FORWARD,
        REVERSE
    }

    public enum Steering {
        FRONT,
        LEFT,
        RIGHT
    }

    public Boolean headlights = new Boolean(false);
    public Boolean policelights = new Boolean(false);

    public Traction traction;
    public Steering steering;

}
