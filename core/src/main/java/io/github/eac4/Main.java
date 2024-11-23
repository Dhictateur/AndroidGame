package io.github.eac4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.eac4.helpers.AssetManager;
import io.github.eac4.screens.SplashScreen;

public class Main extends Game {

    @Override
    public void create() {
        System.err.println("PUNT 2");
        Gdx.app.log("Main", "El juego ha iniciado correctamente en el dispositivo móvil.");
        // A l'iniciar el joc carreguem els recursos
        AssetManager.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }
}
