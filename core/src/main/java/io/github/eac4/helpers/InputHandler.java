package io.github.eac4.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.eac4.objects.Player;
import io.github.eac4.screens.GameScreen;

public class InputHandler implements InputProcessor {

    private int previousX, previousY;
    private Player player;
    private GameScreen screen;
    private Stage stage;

    public InputHandler(GameScreen screen) {
        this.screen = screen;
        player = screen.getPlayer();
        stage = screen.getStage();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        previousX = screenX;
        previousY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.stay(); // Detener el movimiento cuando se levanta el dedo
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(screenX, screenY));

        // Calculamos los desplazamientos (delta) desde el último evento touch
        float deltaX = screenX - previousX;
        float deltaY = previousY - screenY;  // Invertimos el cálculo del delta Y

        // Definir un umbral de movimiento para evitar movimientos muy pequeños
        float MIN_MOVEMENT_THRESHOLD = 30f; // Umbral mínimo de movimiento en píxeles

        // Verificamos si el desplazamiento es suficiente para que el jugador se mueva
        if (Math.abs(deltaX) > MIN_MOVEMENT_THRESHOLD || Math.abs(deltaY) > MIN_MOVEMENT_THRESHOLD) {
            // Si el desplazamiento horizontal es mayor, movemos hacia la izquierda o derecha
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    player.goRight();
                    Gdx.app.log("InputHandler", "Moving Right");
                } else {
                    player.goLeft();
                    Gdx.app.log("InputHandler", "Moving Left");
                }
            }
            // Si el desplazamiento vertical es mayor, movemos hacia arriba o abajo
            else {
                if (deltaY > 0) { // Lógica invertida para Y
                    player.goUp();
                    Gdx.app.log("InputHandler", "Moving Up");
                } else {
                    player.goDown();
                    Gdx.app.log("InputHandler", "Moving Down");
                }
            }

            // Actualizar las posiciones previas
            previousX = screenX;
            previousY = screenY;
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
