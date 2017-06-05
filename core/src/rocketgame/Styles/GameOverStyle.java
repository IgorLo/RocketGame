package rocketgame.Styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GameOverStyle {

    public Window.WindowStyle windowStyle(){

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.color = Color.BLACK;
        p.size = 30;
        BitmapFont font = gen.generateFont(p);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;

        return windowStyle;
    }

    public Label.LabelStyle labelStyle(){

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.color = Color.WHITE;
        p.size = 70;
        BitmapFont font = gen.generateFont(p);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        return labelStyle;

    }

    public TextButton.TextButtonStyle textButtonStyle(){

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.color = Color.BLACK;
        p.size = 30;
        BitmapFont font = gen.generateFont(p);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = Graphics.textures.getDrawable("up");
        style.over = Graphics.textures.getDrawable("over");
        style.down = Graphics.textures.getDrawable("down");
        style.font = font;

        return style;
    }
}