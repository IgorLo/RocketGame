package rocketgame.Utils;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {


    private World world;
    boolean departed = false;
    boolean isDead = false;

    public MyContactListener(World world) {
        super();
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {

        if (contact.getFixtureA().getUserData().equals("PLAYER") && contact.getFixtureB().getUserData().equals("OBSTACLE")){
            isDead = true;
        }

        if (contact.getFixtureA().getUserData().equals("OBSTACLE") && contact.getFixtureB().getUserData().equals("PLAYER")){
            isDead = true;
        }

    }

    @Override
    public void endContact(Contact contact) {

        if (contact.getFixtureA().getUserData().equals("PLATFORM") && contact.getFixtureB().getUserData().equals("PLAYER")){
            departed = true;
        }

        if (contact.getFixtureA().getUserData().equals("PLAYER") && contact.getFixtureB().getUserData().equals("PLATFORM")){
            departed = true;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public boolean isDeparted() {
        return departed;
    }

    public boolean isDead() {
        return isDead;
    }
}
