package io.github.eac4.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.audio.Music;

public class AssetManager {
    // Sprite Sheet
    public static Texture sheet;

    public static TiledMap tiledMap;

    // Nau i fons
    public static TextureRegion player;
    public static TextureRegion playerUp;
    public static TextureRegion playerRight;
    public static TextureRegion playerLeft;
    public static TiledMap background;
    public static Music backgroundMusic;

    public static void load() {
        sheet = new Texture("sheet.png");
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        player = new TextureRegion(sheet, 16, 0, 16, 24);
        player.flip(false, true);

        playerUp = new TextureRegion(sheet, 48, 0, 16, 24);
        playerUp.flip(false, true);

        playerLeft = new TextureRegion(sheet, 80, 0, 16, 24);
        playerLeft.flip(false, true);

        playerRight = new TextureRegion(sheet, 80, 0, 16, 24);
        playerRight.flip(true, true);

        try {
            // Carga el mapa desde un archivo .tmx
            tiledMap = new TmxMapLoader().load("map.tmx");
        } catch (Exception e) {
            System.err.println("Error cargando el mapa: " + e.getMessage());
            e.printStackTrace();
        }
        background = new TmxMapLoader().load("map.tmx");

        // Cargar música
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/theme.mp3"));
        backgroundMusic.setLooping(true); // Repetir la música
        backgroundMusic.setVolume(0.2f); // Establecer el volumen (ajusta según lo necesario)
    }



    public static void dispose() {
        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }

}
