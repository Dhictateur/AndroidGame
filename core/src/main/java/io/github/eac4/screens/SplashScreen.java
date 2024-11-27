package io.github.eac4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.eac4.Main;
import io.github.eac4.helpers.AssetManager;
import io.github.eac4.objects.Player;
import io.github.eac4.utils.Settings;

public class SplashScreen implements Screen {

    private Stage stage;
    private Main game;
    private TiledMapRenderer mapRenderer;

    public SplashScreen(Main game) {

        this.game = game;

        // Creem la càmera de les dimensions del joc
        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        // Posant el paràmetre a true configurem la càmera per a
        // que faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(true);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.CAMERA_WIDTH, Settings.CAMERA_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        // Cargar el mapa desde el AssetManager y configurar el renderer
        TiledMap tiledMap = AssetManager.background;
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 32f);

        Image player = new Image(AssetManager.player);
        player.setSize(Settings.PLAYER_WIDTH, Settings.PLAYER_HEIGHT);
        player.setPosition(Settings.CAMERA_WIDTH / 2f - 1, Settings.CAMERA_HEIGHT / 2f - 1);
        stage.addActor(player);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Configura la cámara para el renderer
        mapRenderer.setView((OrthographicCamera) stage.getViewport().getCamera());
        mapRenderer.render();

        stage.draw();
        stage.act(delta);

        // Si es fa clic en la pantalla, canviem la pantalla
        if (Gdx.input.isTouched()) {
            Gdx.app.log("SplashScreen", "El juego ha detectado un toque.");
            game.setScreen(new GameScreen(game, stage.getBatch(), stage.getViewport()));
            dispose();
        }
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
