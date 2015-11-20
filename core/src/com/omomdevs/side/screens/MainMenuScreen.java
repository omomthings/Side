package com.omomdevs.side.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.omomdevs.side.Side;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class MainMenuScreen extends ScreenAdapter implements InputProcessor {

    private enum Views{MAIN,LEVEL_CHOOSE};
    final Side side;
    Stage stage;
    Table firstTable;
    TextButton newGame,option,leaderboard;
    Label title;

    public MainMenuScreen(Side side) {
        this.side = side;
        stage = new Stage(side.viewport);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {
        firstTable = new Table(side.skin);
        firstTable.setFillParent(true);
        firstTable.top();
        //creating things
        newGame = new TextButton(side.langageManager.getString("Nouveau Jeu"), side.skin, "default black") {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.getColor().a *= parentAlpha;
                super.draw(batch, parentAlpha);
            }
        };
        leaderboard = new TextButton(side.langageManager.getString("Scores"), side.skin, "default black") {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.getColor().a *= parentAlpha;
                super.draw(batch, parentAlpha);
            }
        };
        option = new TextButton(side.langageManager.getString("Options"), side.skin, "default black") {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.getColor().a *= parentAlpha;
                super.draw(batch, parentAlpha);
            }
        };
        title = new Label(side.langageManager.getString("BOunDs"), side.skin, "titre") {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color c = batch.getColor();
                c.a *= parentAlpha;
                batch.setColor(c);
                super.draw(batch, parentAlpha);
            }
        };

        //Setting first menu
        firstTable.add().prefHeight(300).top().setActor(title).top().expandX().row();
        firstTable.add().prefHeight(140).prefWidth(Side.VIEWPORT_WIDTH / 4).setActor(leaderboard).expand().padBottom(30);
        firstTable.add().prefHeight(140).prefWidth(Side.VIEWPORT_WIDTH / 4).setActor(newGame).expand().padBottom(30);
        firstTable.add().prefHeight(140).prefWidth(Side.VIEWPORT_WIDTH / 4).setActor(option).expand().padBottom(30);
        firstTable.pack();
        firstTable.debug();

        //Setting listeners
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //side.ask for screen
                //launch transition an thn make screen
            }
        });
        leaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // side.setScreen(new LeaderBoardScreen(side));
                dispose();
            }
        });
        option.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //side.setScreen(new OptionScreen(side));
                dispose();
            }
        });
        stage.addActor(firstTable);
        transtionBetween(null,Views.MAIN);

        //Setting first in animation and lunching it



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getCamera().update();
    }


    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("MainMenuScreen", "Disposed");
    }

    private void exit() {
        dispose();
        Gdx.app.exit();
        Gdx.app.log("MainMenu", "Game exited");
    }

    /**Makes a transition between different main menu screen
     *
     * @param from where you came
     * @param to where you go
     */
    private void transtionBetween(Views from,Views to){
        if (from==null){   //came from nowhere and obviously make a first menu
            newGame.addAction(alpha(0));
            leaderboard.addAction(alpha(0));
            option.addAction(alpha(0));
            newGame.setTouchable(Touchable.disabled);
            leaderboard.setTouchable(Touchable.disabled);
            option.setTouchable(Touchable.disabled);    //disabling and then enabling after the end of the anim

            title.addAction(sequence(alpha(0), alpha(1, 2f, Interpolation.exp5), delay(1), run(new Runnable() {
                @Override
                public void run() {
                    Gdx.app.log("MainMenu", "animation transition - starting second part");
                    leaderboard.addAction(alpha(1, .8f, Interpolation.exp10In));
                    newGame.addAction(sequence(delay(.2f), alpha(1, .8f, Interpolation.exp10In)));
                    option.addAction(sequence(delay(.4f), alpha(1, .8f, Interpolation.exp10In)));
                    newGame.setTouchable(Touchable.enabled);
                    option.setTouchable(Touchable.enabled);
                    leaderboard.setTouchable(Touchable.enabled);
                }
            })));
        }
        else {

            //TODO make different transition in the main menu screen
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}