package com.igorlo.rocketgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rocketgame.RocketGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 500;
		config.width = 800;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.title = "Rocket Landing";
		config.resizable = false;
		new LwjglApplication(new RocketGame(), config);
	}
}
