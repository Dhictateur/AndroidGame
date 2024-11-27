package io.github.eac4.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import io.github.eac4.helpers.AssetManager;

public class TimeCoin extends Actor {

    private Rectangle collisionRect;
    private Vector2 position;
    private int width, height;
    private float stateTime;

    public TimeCoin(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        stateTime = 0f;
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public TextureRegion getTimeCoinTexture() {
        return AssetManager.timeCoinAnim.getKeyFrame(stateTime, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta; // Incrementa el tiempo de estado con el delta
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getTimeCoinTexture(), position.x, position.y, width, height);
        collisionRect.set(position.x, position.y, width, height);
    }
}
