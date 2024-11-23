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

    // Constructor de la clase Player
    public Player(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();

        direction = PLAYER_STILL;

        // Definir la posición inicial del jugador
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        switch (direction) {
            case PLAYER_DOWN:
                this.position.y += Settings.PLAYER_VELOCITY * delta;  // Mover hacia arriba
                break;
            case PLAYER_UP:
                this.position.y -= Settings.PLAYER_VELOCITY * delta;  // Mover hacia abajo
                break;
            case PLAYER_RIGHT:
                this.position.x += Settings.PLAYER_VELOCITY * delta;  // Mover hacia la derecha
                break;
            case PLAYER_LEFT:
                this.position.x -= Settings.PLAYER_VELOCITY * delta;  // Mover hacia la izquierda
                break;
            case PLAYER_STILL:
                break;
        }
        // Actualizar el rectángulo de colisión
        collisionRect.set(position.x, position.y + 3, width, 10);
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
                return AssetManager.playerUp;
            case PLAYER_DOWN:
                return AssetManager.player;
            case PLAYER_RIGHT:
                return  AssetManager.playerRight;
            case PLAYER_LEFT:
                return AssetManager.playerLeft;
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
