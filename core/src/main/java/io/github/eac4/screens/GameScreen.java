package io.github.eac4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.eac4.Main;
import io.github.eac4.helpers.AssetManager;
import io.github.eac4.helpers.InputHandler;
import io.github.eac4.objects.Coin;
import io.github.eac4.objects.Player;
import io.github.eac4.utils.Settings;

public class GameScreen implements Screen {

    private Stage stage;
    private Player player;
    private TiledMapRenderer mapRenderer; // Para el fondo

    private ShapeRenderer shapeRenderer;
    private Batch batch;

    private Array<Coin> coins; // Lista para almacenar las monedas
    private int coinsCollected;

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

        coins = new Array<>(); // Inicializa la lista de monedas
        coinsCollected = 0;
    }

    private Vector2 generateRandomPosition(float worldWidth, float worldHeight) {
        float x = (float) Math.random() * worldWidth;  // Genera una posición X aleatoria
        float y = (float) Math.random() * worldHeight; // Genera una posición Y aleatoria
        return new Vector2(x, y);
    }

    private void spawnCoins(int count, float worldWidth, float worldHeight) {
        for (int i = 0; i < count; i++) {
            Vector2 position = generateRandomPosition(worldWidth, worldHeight);
            Coin coin = new Coin(position.x, position.y, Settings.COIN_WIDTH, Settings.COIN_HEIGHT);
            coins.add(coin); // Añade la moneda a la lista
            stage.addActor(coin); // Añade la moneda al escenario
        }
    }

    @Override
    public void show() {
        spawnCoins(Settings.INITIAL_COIN_COUNT, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
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

        Array<Coin> coinsToRemove = new Array<>();

        for (Coin coin : coins) {
            if (coin.getCollisionRect().overlaps(player.getCollisionRect())) {
                coinsToRemove.add(coin); // Marcar la moneda para eliminar
                coinsCollected++; // Incrementar el puntaje
            }
        }

        // Remover monedas recogidas del escenario y de la lista
        for (Coin coin : coinsToRemove) {
            coin.remove(); // Elimina la moneda del escenario
            coins.removeValue(coin, true); // Elimina la moneda de la lista
        }

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
