package com.omomdevs.side;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.omomdevs.side.screens.GameScreen;
import com.omomdevs.side.screens.MainMenuScreen;
import com.omomdevs.side.screens.OptionScreen;
import com.omomdevs.side.utils.LangageManager;
import com.omomdevs.side.utils.NotificationListener;


public class Side extends Game {
    enum Screens {MAIN_MENU, OPTION}

    static public final int VIEWPORT_WIDTH = 1200, VIEWPORT_HEIGHT = 720,PX_M=30;
    //TODO make enumeration to use in "ask for screen

    public OrthographicCamera camera;
    public FitViewport viewport;
    public Skin skin;
    public LangageManager langageManager;
    Screen actualSscreen = null, nextScreen = null;
    Array<Screen> screens;


    @Override
    public void create() {
        //Setting Screen Sizes & Loading elements
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        skin = new Skin(Gdx.files.internal("data/skin t/uiskin.json"));
        langageManager = LangageManager.getInstance();
        screens = new Array<Screen>();

        //Setting fonts & skin
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/artifice.rs-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        skin.add("titre", new Label.LabelStyle(generator.generateFont(parameter), skin.getColor("dark grey")), Label.LabelStyle.class);
        generator.dispose();
        parameter.size = 60;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/oatmeal and raisins.ttf"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.getDrawable("black_dot"), skin.getDrawable("dark_grey_dot"), null, generator.generateFont(parameter));
        style.fontColor = skin.getColor("white");
        style.downFontColor = skin.getColor("dark grey");
        skin.add("default black", style, style.getClass());
        generator.dispose();

        //making first screen and starting the game
        //askForScreen(Screens.MAIN_MENU);
        //setScreen();
        setScreen(new GameScreen(this));
        //TODO reset this
    }

    @Override
    public void dispose() {
        skin.dispose();
        super.dispose();
    }

    public void setScreen() {
        if (nextScreen == null) {
            askForScreen(Screens.MAIN_MENU);
            Gdx.app.error("setScreen", "askForScreen should be called before setting the actual screen ");
        }
        actualSscreen = nextScreen;
        nextScreen = null;
        setScreen(actualSscreen);
    }


    /**
     * Used to prepare next screen if it's ever the first time so there is less latence time on slow equipements
     *
     * @param screen one of the screen identifiers
     */
    public void askForScreen(Screens screen) {
        int i;
        switch (screen) {
            case MAIN_MENU:
                i = checkFor(MainMenuScreen.class);
                if (i == -1) {
                    nextScreen = new MainMenuScreen(this);
                    screens.add(nextScreen);
                } else
                    nextScreen = screens.get(i);
                break;
            case OPTION:
                i = checkFor(OptionScreen.class);
                if (i == -1) {
                    nextScreen = new OptionScreen(this);
                    screens.add(nextScreen);
                } else
                    nextScreen = screens.get(i);
        }
    }


    /**
     * check if there is allready an instance of the screen in the screens Array
     *
     * @param screen the class of the given screen
     * @return the index of the given screen, -1 if don't exist (first time asked)
     */
    private int checkFor(Class screen) {
        for (int i = 0; i < screens.size; i++) {
            if (screens.get(i).getClass() == screen)
                return i;
        }
        return -1;
    }

    //TODO make transition between screens
}
