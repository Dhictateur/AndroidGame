package io.github.eac4.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

import io.github.eac4.helpers.AssetManager;
import io.github.eac4.utils.Settings;

public class Bullet extends Actor {

    private Texture texture;  // Textura del jugador
    private Vector2 position; // Posición del jugador
    private Rectangle collisionRect;
    private float width, height;
    private float speed;
    private float stateTime;
    private float dx, dy;
    private float x, y;

    private Pool<Bullet> pool;

    public Bullet(float x, float y, float width, float height, float dx, float dy) {
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        stateTime = 0f;
        this.pool = pool; // Guardamos una referencia al pool
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Deberías actualizar `position` en lugar de `x` e `y`
        position.x += dx * delta;
        position.y += dy * delta;

        // Actualiza las variables `x` e `y` si necesitas compatibilidad con otros métodos
        x = position.x;
        y = position.y;

        stateTime += delta;

        // Actualizar el rectángulo de colisión
        collisionRect.set(position.x, position.y, width, height);

        // Eliminar la bala si está fuera de los límites
        if (position.x < 0 || position.x > Settings.GAME_WIDTH ||
            position.y < 0 || position.y > Settings.GAME_HEIGHT) {
            Player.getBulletPool().free(this); // Liberar la bala del pool
            remove();
        }
    }

    public void init(float x, float y, float dx, float dy) {
        position.set(x, y);
        this.dx = dx;
        this.dy = dy;
        stateTime = 0f;
        setVisible(true); // Hacer visible la bala reciclada
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public TextureRegion getBulletTexture() {
        return AssetManager.bulletAnim.getKeyFrame(stateTime, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getBulletTexture(), x, y, width, height);
    }
}
