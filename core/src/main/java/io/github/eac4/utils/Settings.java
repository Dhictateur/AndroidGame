package io.github.eac4.utils;

public class Settings {

    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 30;
    public static final int GAME_HEIGHT = 20;

    public static final int CAMERA_WIDTH = 15;
    public static final int CAMERA_HEIGHT = 10;

    public static final float PLAYER_VELOCITY = 7;
    public static final int PLAYER_WIDTH = 1;
    public static final int PLAYER_HEIGHT = 2;
    public static final float PLAYER_STARTY = GAME_HEIGHT/2 - PLAYER_HEIGHT/2;
    public static final float PLAYER_STARTX = GAME_WIDTH/2 - PLAYER_WIDTH/2;
    public static final int COIN_WIDTH = 1;
    public static final int COIN_HEIGHT = 2;
    public static final int INITIAL_COIN_COUNT = 10;
    public static final float BULLET_WIDTH = 0.4f;
    public static final float BULLET_HEIGHT = 0.6f;

    public static final int SCORE_INCREASE = 100; // s'incrementa en 100 cada cop que toca una moneda
    public static final int SCORE_SPEED = -175;



}
