package rocketgame.Styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Graphics {

    public static Skin textures;

    public static void loadGraphics(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
        textures = new Skin();
        textures.addRegions(atlas);
    }

}
