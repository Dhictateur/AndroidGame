package io.github.eac4.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    private static final float DURATION = 0.17f;

    public static Animation<TextureRegion> playerDownAnim;
    public static Animation<TextureRegion> playerUpAnim;
    public static Animation<TextureRegion> playerLeftAnim;
    public static Animation<TextureRegion> playerRightAnim;

    public static Animation<TextureRegion> coinAnim;
    public static Animation<TextureRegion> timeCoinAnim;

    public static Animation<TextureRegion> bulletAnim;

    public static BitmapFont font;

    public static void load() {
        sheet = new Texture("sheet.png");
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        player = new TextureRegion(sheet, 16, 0, 16, 24);
        player.flip(false, true);

        // Extraer frames para las animaciones
        TextureRegion[] downFrames = new TextureRegion[3];
        TextureRegion[] upFrames = new TextureRegion[3];
        TextureRegion[] leftFrames = new TextureRegion[3];
        TextureRegion[] rightFrames = new TextureRegion[3];

        TextureRegion[] coinFrames = new TextureRegion[3];
        TextureRegion[] timeCoinFrames = new TextureRegion[3];

        TextureRegion[] bulletAnimFrames = new TextureRegion[3];

        for (int i = 0; i < 3; i++) {
            downFrames[i] = new TextureRegion(sheet, i * 16, 0, 16, 24);
            downFrames[i].flip(false, true);

            upFrames[i] = new TextureRegion(sheet, i * 16, 24, 16, 24);
            upFrames[i].flip(false, true);

            leftFrames[i] = new TextureRegion(sheet, i * 16, 48, 16, 24);
            leftFrames[i].flip(false, true);

            rightFrames[i] = new TextureRegion(sheet, i * 16, 48, 16, 24);
            rightFrames[i].flip(true, true); // Flip horizontal

            coinFrames[i] = new TextureRegion(sheet, i * 16, 72, 16, 24);
            coinFrames[i].flip(false, true);

            timeCoinFrames[i] = new TextureRegion(sheet, i * 16, 96, 16, 24);
            timeCoinFrames[i].flip(false, true);

            bulletAnimFrames[i] = new TextureRegion(sheet, i * 16, 120, 7, 7);
        }

        // Crear las animaciones
        playerDownAnim = new Animation<>(DURATION, downFrames);
        playerUpAnim = new Animation<>(DURATION, upFrames);
        playerLeftAnim = new Animation<>(DURATION, leftFrames);
        playerRightAnim = new Animation<>(DURATION, rightFrames);

        coinAnim = new Animation<>(DURATION, coinFrames);
        timeCoinAnim = new Animation<>(DURATION, timeCoinFrames);

        bulletAnim = new Animation<>(DURATION, bulletAnimFrames);

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

        FileHandle fontFile = Gdx.files.internal("font/font.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.05f);
    }



    public static void dispose() {
        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        if (tiledMap != null) {
            tiledMap.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
