package rocketgame;

import com.badlogic.gdx.Game;
import rocketgame.Screens.MenuGame;
import rocketgame.Styles.Graphics;

public class RocketGame extends Game {

    @Override
    public void create() {
        Graphics.loadGraphics();
        this.setScreen(new MenuGame(this));
    }
}
