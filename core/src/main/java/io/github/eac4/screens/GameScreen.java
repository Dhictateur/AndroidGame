package io.github.eac4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.eac4.Main;
import io.github.eac4.helpers.AssetManager;
import io.github.eac4.helpers.InputHandler;
import io.github.eac4.objects.Coin;
import io.github.eac4.objects.TimeCoin;
import io.github.eac4.objects.Player;
import io.github.eac4.utils.Settings;

public class GameScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Player player;
    private TiledMapRenderer mapRenderer; // Para el fondo

    private ShapeRenderer shapeRenderer;
    private Batch batch;

    private Array<Coin> coins; // Lista para almacenar las monedas
    private int coinsCollected;

    private Array<TimeCoin> timeCoins;

    private int timeRemaining; // Tiempo restante en segundos
    private float timeAccumulator; // Acumulador para gestionar el deltaTime


    public GameScreen (Main game, Batch prevBatch, Viewport prevViewport) {
        this.game = game;

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

        timeCoins = new Array<>();

        // Inicializa el temporizador
        timeRemaining = 60;
        timeAccumulator = 0;
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

    private void spawnTimeCoins (int count, float worldWidth, float worldHeight) {
        for (int i = 0; i < count; i++) {
            Vector2 position = generateRandomPosition(worldWidth, worldHeight);
            TimeCoin coin = new TimeCoin(position.x, position.y, Settings.COIN_WIDTH, Settings.COIN_HEIGHT);
            timeCoins.add(coin);
            stage.addActor(coin);
        }
    }

    @Override
    public void show() {
        spawnCoins(Settings.INITIAL_COIN_COUNT, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        spawnTimeCoins(Settings.INITIAL_COIN_COUNT, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        // Temporizador para disparar balas
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                player.shoot(); // Dispara una bala
            }
        }, 0, 1);
    }

    @Override
    public void render(float delta) {
        // Actualizar el acumulador y el temporizador
        timeAccumulator += delta;
        if (timeAccumulator >= 1f) {
            timeRemaining--; // Resta un segundo
            timeAccumulator = 0;
        }

        // Verifica si el tiempo se ha agotado
        if (timeRemaining <= 0) {
            game.setScreen(new SplashScreen(game)); // Cambia a SplashScreen
            dispose();
            return; // Sal del método para evitar errores
        }

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

                // Generar una nueva posición aleatoria para la nueva moneda
                Vector2 newPosition = generateRandomPosition(worldWidth, worldHeight);
                Coin newCoin = new Coin(newPosition.x, newPosition.y, Settings.COIN_WIDTH, Settings.COIN_HEIGHT);
                coins.add(newCoin); // Añadir la nueva moneda a la lista
                stage.addActor(newCoin); // Añadir la nueva moneda al escenario
            }
        }

        // Remover monedas recogidas del escenario y de la lista
        for (Coin coin : coinsToRemove) {
            coin.remove(); // Elimina la moneda del escenario
            coins.removeValue(coin, true); // Elimina la moneda de la lista
        }

        Array<TimeCoin> timeCoinsToRemove = new Array<>();

        for (TimeCoin coin : timeCoins) {
            if (coin.getCollisionRect().overlaps(player.getCollisionRect())) {
                timeCoinsToRemove.add(coin); // Marcar la moneda para eliminar
                timeRemaining++;

                // Generar una nueva posición aleatoria para la nueva moneda
                Vector2 newPosition = generateRandomPosition(worldWidth, worldHeight);
                TimeCoin newCoin = new TimeCoin(newPosition.x, newPosition.y, Settings.COIN_WIDTH, Settings.COIN_HEIGHT);
                timeCoins.add(newCoin); // Añadir la nueva moneda a la lista
                stage.addActor(newCoin); // Añadir la nueva moneda al escenario
            }
        }

        // Remover monedas recogidas del escenario y de la lista
        for (TimeCoin coin : timeCoinsToRemove) {
            coin.remove(); // Elimina la moneda del escenario
            timeCoins.removeValue(coin, true); // Elimina la moneda de la lista
        }

        // Dibuixem tots els actors de l'stage
        stage.act(delta);
        stage.draw();
        batch.begin();

        float pointTextX = camera.position.x - camera.viewportWidth / 2 + 0.5f;
        float pintTextY = camera.position.y + camera.viewportHeight / 2 - 9;

        float TimeTextX = camera.position.x - camera.viewportWidth / 2 + 12.5f;
        float TimeTextY = camera.position.y + camera.viewportHeight / 2 - 9;

        // Dibuja el texto en la pantalla
        AssetManager.font.setColor(Color.WHITE);  // Establece el color del texto (puedes cambiarlo)
        AssetManager.font.draw(batch, "" + coinsCollected, pointTextX, pintTextY);
        AssetManager.font.draw(batch, "" + timeRemaining, TimeTextX, TimeTextY);
        batch.end();
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
        stage.dispose();
        shapeRenderer.dispose();
    }

    public Player getPlayer() {
        return player;
    }

    public Stage getStage() {
        return stage;
    }
}
