package io.github.eac4.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import io.github.eac4.helpers.AssetManager;
import io.github.eac4.utils.Settings;

import java.util.Random;

public class Target extends Actor {
    private Rectangle collisionRect;
    private Vector2 position;
    private float width, height;

    private Vector2 direction;
    private float speed = 1; // Velocidad de movimiento (pixeles por segundo)
    private float timer = 0; // Temporizador para cambiar dirección
    private Random random; // Generador de números aleatorios

    public Target(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        direction = new Vector2(0, 0); // Dirección inicial
        random = new Random();
        chooseRandomDirection(); // Elegir dirección inicial
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public TextureRegion getTargetTexture() {
        return AssetManager.target;
    }

    private void chooseRandomDirection() {
        // Elegir una dirección aleatoria
        float angle = random.nextFloat() * 360; // Ángulo aleatorio en grados
        direction.set((float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle)));
        direction.nor(); // Normalizar el vector para tener magnitud 1
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Actualizar temporizador
        timer += delta;
        if (timer >= 1) { // Cambiar dirección cada segundo
            timer = 0;
            chooseRandomDirection();
        }

        // Mover en la dirección actual
        position.add(direction.x * speed * delta, direction.y * speed * delta);

        // Evitar que el Target salga de los límites del mapa
        if (position.x < 0 || position.x > Settings.GAME_WIDTH - width) {
            direction.x = -direction.x; // Invertir dirección en X
        }
        if (position.y < 0 || position.y > Settings.GAME_HEIGHT - height) {
            direction.y = -direction.y; // Invertir dirección en Y
        }

        // Ajustar posición a los límites
        position.x = Math.max(0, Math.min(position.x, Settings.GAME_WIDTH - width));
        position.y = Math.max(0, Math.min(position.y, Settings.GAME_HEIGHT - height));

        collisionRect.set(position.x, position.y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getTargetTexture(), position.x, position.y, width, height);
        collisionRect.set(position.x, position.y, width, height);
    }
}
