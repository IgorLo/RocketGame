package rocketgame.Styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonStyle {

    public TextButton.TextButtonStyle menuButton(){

        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.WHITE;
        parameter.size = 17;

        BitmapFont font = fontGen.generateFont(parameter);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.up = Graphics.textures.getDrawable("up");
        style.down = Graphics.textures.getDrawable("down");
        style.over = Graphics.textures.getDrawable("over");

        style.font = font;

        return style;

    }



}
