package io.github.eac4.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import io.github.eac4.helpers.AssetManager;
import io.github.eac4.utils.Settings;

public class Player extends Actor {
    public static final int PLAYER_STILL = 0;
    public static final int PLAYER_UP = 1;
    public static final int PLAYER_DOWN = 2;
    public static final int PLAYER_RIGHT = 3;
    public static final int PLAYER_LEFT = 4;

    private Texture texture;  // Textura del jugador
    private Vector2 position; // Posición del jugador
    private Rectangle collisionRect;
    private int width, height;
    private int direction;

    private float stateTime;

    // Constructor de la clase Player
    public Player(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        stateTime = 0f;

        direction = PLAYER_STILL;

        // Definir la posición inicial del jugador
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;

        float nextX = position.x;
        float nextY = position.y;

        // Calcular la nueva posición basada en la dirección
        switch (direction) {
            case PLAYER_DOWN:
                nextY += Settings.PLAYER_VELOCITY * delta;
                break;
            case PLAYER_UP:
                nextY -= Settings.PLAYER_VELOCITY * delta;
                break;
            case PLAYER_RIGHT:
                nextX += Settings.PLAYER_VELOCITY * delta;
                break;
            case PLAYER_LEFT:
                nextX -= Settings.PLAYER_VELOCITY * delta;
                break;
            case PLAYER_STILL:
                break;
        }

        // Limitar la posición a los bordes del mapa
        nextX = Math.max(0, Math.min(nextX, Settings.GAME_WIDTH - width)); // Limitar en X
        nextY = Math.max(0, Math.min(nextY, Settings.GAME_HEIGHT - height)); // Limitar en Y

        // Actualizar la posición si es válida
        position.set(nextX, nextY);

        // Actualizar el rectángulo de colisión
        collisionRect.set(position.x, position.y, width, height);
        setBounds(position.x, position.y, width, height);
    }

    // Métodos para mover al jugador
    public void goUp() {
        direction = PLAYER_UP;
    }

    public void goDown() {
        direction = PLAYER_DOWN;
    }

    public void goRight() {
        direction = PLAYER_RIGHT;
    }

    public void goLeft() {
        direction = PLAYER_LEFT;
    }

    public void stay() {
        direction = PLAYER_STILL;
    }

    // Obtener la posición del jugador
    public Vector2 getPosition() {
        return position;
    }

    // Establecer la posición del jugador
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    // Obtener el rectángulo de colisión del jugador
    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    // Liberar recursos al destruir el jugador
    public void dispose() {
        texture.dispose();
    }

    // Obtenim el TextureRegion depenent de la posició de la spacecraft
    public TextureRegion getPlayerTexture() {
        switch (direction) {
            case PLAYER_STILL:
                return AssetManager.player;
            case PLAYER_UP:
                return AssetManager.playerUpAnim.getKeyFrame(stateTime, true);
            case PLAYER_DOWN:
                return AssetManager.playerDownAnim.getKeyFrame(stateTime, true);
            case PLAYER_RIGHT:
                return  AssetManager.playerRightAnim.getKeyFrame(stateTime, true);
            case PLAYER_LEFT:
                return AssetManager.playerLeftAnim.getKeyFrame(stateTime, true);
            default:
                return AssetManager.player;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getPlayerTexture(), position.x, position.y, width, height);
    }
}
