package io.github.eac4.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.eac4.Main;

public class GameOver implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private TextButton botonReinicio;
    private Label labelTiempo;
    private Label labelPuntuacion;
    private Label labelGameOver;  // Nuevo label para "GAME OVER"

    private float tiempoTranscurrido;
    private int puntuacion;

    private Main game; // Referencia a Main para cambiar la pantalla

    // Constructor para aceptar una referencia a Main
    public GameOver(Main game, float tiempoTranscurrido, int puntuacion) {
        this.game = game; // Guardamos la referencia
        this.tiempoTranscurrido = tiempoTranscurrido;
        this.puntuacion = puntuacion;

        stage = new Stage();
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Establecer tamaño base de la fuente y escalarla según el tamaño de la pantalla
        float screenWidth = Gdx.graphics.getWidth();
        float fontScale = screenWidth / 500f; // Escala basada en un ancho de pantalla de referencia (1920px)

        font.getData().setScale(fontScale); // Escalar la fuente

        // Configurar el estilo del botón sin texturas ni estados
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;

        botonReinicio = new TextButton("Reiniciar", style);
        botonReinicio.pad(20); // Añadir relleno para hacerlo más grande

        // Configurar las etiquetas de tiempo y puntuación
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        labelTiempo = new Label("Tiempo: " + (int)tiempoTranscurrido + " segundos", labelStyle);
        labelPuntuacion = new Label("Puntuación: " + puntuacion, labelStyle);
        labelGameOver = new Label("GAME OVER", labelStyle);  // Crear el label para "GAME OVER"

        // Alinear las etiquetas al centro
        labelTiempo.setAlignment(Align.center);
        labelPuntuacion.setAlignment(Align.center);
        labelGameOver.setAlignment(Align.center);  // Alinear al centro

        // Crear una tabla para organizar los elementos
        Table table = new Table();
        table.center(); // Centrar todo
        table.setFillParent(true);

        // Añadir elementos a la tabla
        table.add(labelGameOver).padBottom(40).row(); // Añadir "GAME OVER" con separación abajo
        table.add(labelTiempo).padBottom(40).row(); // Añadir tiempo con separación abajo
        table.add(labelPuntuacion).padBottom(100).row(); // Añadir puntuación con más separación abajo
        table.add(botonReinicio).padTop(40); // Añadir el botón con separación arriba

        // Añadir la tabla al Stage
        stage.addActor(table);

        // Añadir listener para el botón
        botonReinicio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reiniciarJuego();
            }
        });

        // Configura la entrada
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // Mostrar la pantalla
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renderizar los elementos
        batch.begin();
        batch.end();

        // Renderizar la interfaz
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Limpiar recursos si es necesario
    }

    @Override
    public void pause() {
        // Pausar si es necesario
    }

    @Override
    public void resume() {
        // Reanudar si es necesario
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
    }

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        game.setScreen(new SplashScreen(game)); // Cambiar a la pantalla inicial
    }
}

