package rocketgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import rocketgame.Styles.ButtonStyle;

public class MenuGame implements Screen {

    private Game game;
    private TextButton play, exit;
    private Stage stage;


    public MenuGame(Game rocketGame) {

        this.game = rocketGame;

    }

    @Override
    public void show() {


        ButtonStyle style = new ButtonStyle();
        play = new TextButton("PLAY", style.menuButton());
        exit = new TextButton("EXIT", style.menuButton());

        play.setPosition((Gdx.graphics.getWidth() / 2 - play.getWidth() / 2), (Gdx.graphics.getHeight() / 2 - play.getHeight() / 2) + 40);
        exit.setPosition((Gdx.graphics.getWidth() / 2 - exit.getWidth() / 2), (Gdx.graphics.getHeight() / 2 - exit.getHeight() / 2) - 40);

        stage = new Stage();
        stage.addActor(play);
        stage.addActor(exit);
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (play.isChecked()) game.setScreen(new MainGame(game));
                if (exit.isChecked()) Gdx.app.exit();
                super.clicked(event, x, y);
            }
        });

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.4f, 0.43f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
