package io.github.eac4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.eac4.Main;
import io.github.eac4.helpers.AssetManager;
import io.github.eac4.helpers.InputHandler;
import io.github.eac4.objects.Player;
import io.github.eac4.utils.Settings;

public class GameScreen implements Screen {

    private Stage stage;
    private Player player;
    private TiledMapRenderer mapRenderer; // Para el fondo

    private ShapeRenderer shapeRenderer;
    private Batch batch;

    public GameScreen (Batch prevBatch, Viewport prevViewport) {

        AssetManager.backgroundMusic.play();

        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);
        batch = stage.getBatch();

        // Cargar el mapa desde el AssetManager y configurar el renderer
        TiledMap tiledMap = AssetManager.background;
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 32f);

        player = new Player(Settings.PLAYER_STARTX, Settings.PLAYER_STARTY,
                            Settings.PLAYER_WIDTH, Settings.PLAYER_HEIGHT);
        stage.addActor(player);
        // Donem nom a l'Actor
        player.setName("player");

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        // Actualiza la posición de la cámara para seguir al jugador
        OrthographicCamera camera = (OrthographicCamera) stage.getViewport().getCamera();

        // Obtener las dimensiones del mapa desde AssetManager
        TiledMap tiledMap = AssetManager.background;
        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        // Convertir a unidades del mundo
        float worldWidth = mapWidth * tileWidth / 32f; // Escala 1/32
        float worldHeight = mapHeight * tileHeight / 32f;

        // Calcular los límites de la cámara
        float cameraHalfWidth = camera.viewportWidth / 2;
        float cameraHalfHeight = camera.viewportHeight / 2;

        // Limitar la posición de la cámara para que no salga del mapa
        float cameraX = Math.max(cameraHalfWidth, Math.min(player.getX(), worldWidth - cameraHalfWidth));
        float cameraY = Math.max(cameraHalfHeight, Math.min(player.getY(), worldHeight - cameraHalfHeight));

        camera.position.set(cameraX, cameraY, 0);
        camera.update();

        // Configura la cámara para el renderer del mapa
        mapRenderer.setView(camera);
        mapRenderer.render(); // Renderiza el fondo (mapa)
        // Dibuixem tots els actors de l'stage
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

    public Player getPlayer() {
        return player;
    }

    public Stage getStage() {
        return stage;
    }
}
