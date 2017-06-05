package rocketgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import rocketgame.Styles.GameOverStyle;
import rocketgame.Styles.Graphics;
import rocketgame.Utils.Constants;
import rocketgame.Utils.MyContactListener;

import java.util.ArrayList;
import java.util.List;


public class MainGame implements com.badlogic.gdx.Screen {

    private static final float PPM = Constants.PPM;
    private Game game;

    private final float SCALE = 1.0f;
    private final int startSlide = 12;

    private MyContactListener contactListener;
    private OrthographicCamera camera;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Body player, platform;
    private List<Body> obstacles = new ArrayList<Body>();
    private List<float[]> verticies = new ArrayList<float[]>();
    private float slide;
    private float currentHeight, currentWidth, hRange, wRange, startFreq, maxWidth, minWidth;
    private float timeInSecs;
    private Sprite playerSprite;
    private SpriteBatch batch;
    private boolean isStarted = false;
    private BitmapFont font;
    private float counter;
    private String count = "";
    private Dialog dialog;
    private GameOverStyle gameOverStyle;
    private Stage stage;
    private State state = State.PLAY;

    public MainGame(Game game) {
        this.game = game;
    }

    @Override
	public void show () {

        slide = 0;

        camera = new OrthographicCamera();

        world = new World(new Vector2(0, -10f), false);
        b2dr = new Box2DDebugRenderer();
        contactListener = new MyContactListener(world);
        world.setContactListener(contactListener);

        player = createBox(150, 152, 16, 32, false, "PLAYER");
        platform = createBox(150, 120, 64, 32, true, "PLATFORM");

        currentHeight = 250;
        currentWidth = 500;
        hRange = 14;
        wRange = 25;
        startFreq = 7;
        maxWidth = 200;
        minWidth = 60;

        timeInSecs = 0;

        batch = new SpriteBatch();

        playerSprite = new Sprite(Graphics.textures.getSprite("rocket_off"));

        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.WHITE;
        parameter.size = 17;

        font = fontGen.generateFont(parameter);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        gameOverStyle = new GameOverStyle();
        dialog = new Dialog("Game Over", gameOverStyle.windowStyle()){
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    hide();
                    game.setScreen(new MainGame(game));
                } else {
                    hide();
                    game.setScreen(new MenuGame(game));
                }
            }
        };
        dialog.button("RESTART", true, gameOverStyle.textButtonStyle());
        dialog.button("MENU", false, gameOverStyle.textButtonStyle());
        dialog.text("YOU ARE DEAD", gameOverStyle.labelStyle());
        dialog.show(stage);
	}

	@Override
	public void render (float delta) {

	    update(delta);

	    ShapeRenderer shapeRenderer = new ShapeRenderer();

		Gdx.gl.glClearColor(0.55f, 0.4f, 0.43f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.scl(PPM));

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.15f, 0.15f, 0.1f);
        for (float[] verts: verticies) {
            shapeRenderer.triangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
        }
        shapeRenderer.end();

        batch.begin();
        playerSprite.setPosition(player.getPosition().x * PPM - playerSprite.getWidth() / 2,
                player.getPosition().y * PPM - playerSprite.getHeight() / 2);
        playerSprite.draw(batch);
        font.draw(batch, count, 10, 490);
        batch.end();



		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.setScreen(new MenuGame(game));
        if (contactListener.isDead()) {
            state = State.GAMEOVER;
        }
        if (state == State.GAMEOVER) {
            stage.act(delta);
            stage.draw();
        }

	}

    public void update(float delta){
	    if (state == State.PLAY) {
            world.step(delta, 6, 2);
            slideUpdate(delta);
            obstacleUpdate(delta);
        }
        timeInSecs += delta;
        if (!contactListener.isDead()) counter += delta;
        count = Float.toString(counter);
        player.setTransform(150 / PPM, player.getPosition().y, player.getAngle());
	    cameraUpdate(delta);
        inputUpdate(delta);
	    batch.setProjectionMatrix(camera.combined);
    }

    public void inputUpdate(float delta){
        float horizontalForce = 0;
        float deltaSlide = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            horizontalForce = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            deltaSlide = -0.15f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            deltaSlide = 0.15f;
        }

        slide += deltaSlide;
        player.setLinearVelocity(player.getLinearVelocity().x, player.getLinearVelocity().y + horizontalForce/1.5f);
    }

    public void slideUpdate(float delta){

        if (contactListener.isDeparted() && !isStarted){
            slide = startSlide;
            isStarted = true;
        }

        platform.setTransform(platform.getPosition().x - slide / 100, platform.getPosition().y, platform.getAngle());

        for (Body body: obstacles) {
            body.setTransform(body.getPosition().x - slide / 100, body.getPosition().y, body.getAngle());
        }

        for (float[] floats: verticies) {
            for (int i = 0; i < floats.length; i+=2){
                floats[i] -= (slide / 100) * PPM;
            }
        }

    }

    private void obstacleUpdate(float delta){

        if (!obstacles.isEmpty() && (obstacles.get(0).getPosition().x < -1200 / PPM)){
            world.destroyBody(obstacles.get(0));
            world.destroyBody(obstacles.get(1));
            obstacles.remove(0);
            obstacles.remove(0);
            verticies.remove(0);
            verticies.remove(0);
        }

        if (timeInSecs > 0.02f && contactListener.isDeparted()) {
            timeInSecs = 0;

            currentWidth += (-wRange/2 + MathUtils.random(wRange));
            if (currentWidth > maxWidth) currentWidth -= wRange;
            if (currentWidth < minWidth) currentWidth += wRange;

            currentHeight += (-hRange/2 + MathUtils.random(hRange));
            if (currentHeight < 100) currentHeight += hRange;
            if (currentHeight > 400) currentHeight -= hRange;

            createObstacle(obstacles, verticies);
        }

    }

    @Override
    public void resize(int width, int height) {camera.setToOrtho(false, width / SCALE, height / SCALE);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
	public void dispose () {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
	}

	public void cameraUpdate(float delta){
        Vector3 position = camera.position;
        camera.position.set(position);

        camera.update();
    }

	public Body createBox(int x, int y, int width, int height, boolean isStatic, String userData) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f).setUserData(userData);
        shape.dispose();
        return pBody;
    }

    public void createObstacle(List<Body> list, List<float[]> verts){
	    list.add(createTriangle(true, verts));
        list.add(createTriangle(false, verts));
    }

    public Body createTriangle(boolean isHigh, List<float[]> verts){
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.fixedRotation = true;
        pBody = world.createBody(def);

        ChainShape shape = new ChainShape();

        float[] verticies = new float[6];

        if (isHigh) {

            verticies[0] = (800 + MathUtils.random(150)) / PPM;
            verticies[1] = 500 / PPM;
            verticies[2] = 1000 / PPM;
            verticies[3] = (currentHeight + currentWidth/2) / PPM;
            verticies[4] = (1200 - MathUtils.random(150)) / PPM;
            verticies[5] = 500 / PPM;

        } else {

            verticies[0] = (800 + MathUtils.random(150)) / PPM;
            verticies[1] = 0;
            verticies[2] = 1000 / PPM;
            verticies[3] = (currentHeight - currentWidth/2) / PPM;
            verticies[4] = (1200 - MathUtils.random(150)) / PPM;
            verticies[5] = 0;

        }

        shape.createChain(new Vector2[]{
                new Vector2(verticies[0], verticies[1]),
                new Vector2(verticies[2], verticies[3]),
                new Vector2(verticies[4], verticies[5]),
        });

        for (int i = 0; i < verticies.length; i++) {
            verticies[i] *= PPM;
        }

        verts.add(verticies);
        pBody.createFixture(shape, 1.0f).setUserData("OBSTACLE");
        shape.dispose();
        return pBody;
    }

}