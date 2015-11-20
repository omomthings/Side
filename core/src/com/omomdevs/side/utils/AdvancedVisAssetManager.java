package com.omomdevs.side.utils;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.runtime.component.ParticleComponent;
import com.kotcrab.vis.runtime.component.PhysicsComponent;
import com.kotcrab.vis.runtime.component.PointComponent;
import com.kotcrab.vis.runtime.component.PolygonComponent;
import com.kotcrab.vis.runtime.component.PositionComponent;
import com.kotcrab.vis.runtime.component.SpriteComponent;
import com.kotcrab.vis.runtime.component.SpriterComponent;
import com.kotcrab.vis.runtime.component.TextComponent;
import com.kotcrab.vis.runtime.component.VariablesComponent;
import com.kotcrab.vis.runtime.data.EntityData;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.font.FontProvider;
import com.kotcrab.vis.runtime.font.FreeTypeFontProvider;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.EntityEngine;

/**
 * Advanced kotcrab.vis.runtime.scene.VisAssetManager used to create and manage levels with continuous scenes without interruption
 * <p/>
 * Created by omaro on 15/11/2015.
 */
public class AdvancedVisAssetManager extends AssetManager {

    enum Direction {LEFT, RIGHT, UP, DOWN}

    Loader sceneLoader;
    private Direction secondSceneDirection, firstSceneDirection;
    private int sceneNumber = 0;
    private Array<Entity> firstSceneEntities, secondSceneEntities;
    private String standardName;
    private int numFormat;
    EmptyScene scene;

    /**
     * @param standardName Standard Name for all scene files, e.g for files scene/level00.scene - scene/level01.scene etc. pass "scene/level"
     * @param numFormat    Number of numerical characters in the file names e.g for level001.scene , pass 3
     * @param batch        -
     */
    public AdvancedVisAssetManager(String standardName, int numFormat, SpriteBatch batch) {
        super(new InternalFileHandleResolver());
        this.standardName = standardName;
        this.numFormat = numFormat;
        firstSceneEntities = new Array<>();
        secondSceneEntities = new Array<>();
        sceneLoader = new Loader(batch);
        setLoader(EmptyScene.class, sceneLoader);
    }

    /**
     * Initiates the loading of the first and second scene
     */
    public EmptyScene loadNow() {
        return loadNow(null);
    }

    /**
     * Initiates the loading of the first and second scene with the parameters
     */
    public EmptyScene loadNow(Loader.EmptySceneParameter parameter) {
        load(getSceneName(sceneNumber), EmptyScene.class, parameter);
        long t = TimeUtils.millis();
        finishLoading();
        Gdx.app.debug("loaded on", TimeUtils.timeSinceMillis(t) + "ms");
        scene = get(getSceneName(sceneNumber), EmptyScene.class);
        scene.createEntityEngine();
        load(getSceneName(sceneNumber + 1), EmptyScene.class, parameter);
        finishLoading();
        Json json = SceneLoader.getJson();
        SceneData data = json.fromJson(SceneData.class, Gdx.files.internal(getSceneName(sceneNumber)));
        firstSceneDirection = getDirection(data);
        addDataToScene(data, true);
        data = json.fromJson(SceneData.class, Gdx.files.internal(getSceneName(sceneNumber + 1)));
        secondSceneDirection = getDirection(data);
        addDataToScene(data, false);
        scene.init();
        moveSceneTo(true, secondSceneDirection);
        return scene;
    }

    private void addDataToScene(SceneData data, boolean toFirstScene) {
        EntityEngine engine = scene.getEntityEngine();
        for (EntityData entity : data.entities) {
            Entity e = entity.build(engine);
            if (toFirstScene)
                firstSceneEntities.add(e);
            else
                secondSceneEntities.add(e);
        }
    }

    /**
     * returns the name of the given scene number
     */
    String getSceneName(int number) {
        return String.format(standardName + "%" + numFormat + "d.scene", number);
    }


    public void loadNextScene() {
        Gdx.app.debug("AdvancedVisAssetManager", "Loading next scene");
        sceneNumber++;

        //Delete the first scene
        for (Entity entity : firstSceneEntities)
            entity.deleteFromWorld();
        firstSceneEntities.clear();

        firstSceneEntities.addAll(secondSceneEntities);
        secondSceneEntities.clear();
        //moves the scene to always stay around the (0,0)
        Json json = SceneLoader.getJson();
        SceneData nextData = json.fromJson(SceneData.class, Gdx.files.internal(getSceneName(sceneNumber + 1)));
        firstSceneDirection = secondSceneDirection;
        secondSceneDirection = getDirection(nextData);
        moveSceneTo(true, secondSceneDirection);
        addDataToScene(nextData, false);
        scene.init();
        Gdx.app.debug("AdvancedVisAssetManager", "Finished loading next scene successfully");
    }

    public void loadPreviousScene() {
        Gdx.app.debug("AdvancedVisAssetManager", "Loading previous scene");
        sceneNumber--;
        for (Entity entity : secondSceneEntities)
            entity.deleteFromWorld();
        secondSceneEntities.clear();
        secondSceneEntities.addAll(firstSceneEntities);
        firstSceneEntities.clear();

        Json json = SceneLoader.getJson();
        SceneData data = json.fromJson(SceneData.class, Gdx.files.internal(getSceneName(sceneNumber)));

        //Reset previous scene around 0
        moveSceneTo(false, oppositeDirectionOf(secondSceneDirection));
        secondSceneDirection = firstSceneDirection;
        firstSceneDirection = getDirection(data);

        addDataToScene(data, true);
        scene.init();
        moveSceneTo(true, secondSceneDirection);

        Gdx.app.debug("AdvancedVisAssetManager", "Finished loading previous scene successfully");

    }

    /**
     * Here the parameters are always in the first entity!
     *
     * @param data the {@link SceneData} from where to get the {@link Direction}
     * @return the {@link Direction} set in the given data
     */
    private Direction getDirection(SceneData data) {
        Direction direction = Direction.RIGHT;
        for (Component c : data.entities.first().components) {
            if (c instanceof VariablesComponent) {
                String dir = ((VariablesComponent) c).get("DIRECTION");
                switch (dir) {
                    case "UP":
                        direction = Direction.UP;
                        break;
                    case "DOWN":
                        direction = Direction.DOWN;
                        break;
                    case "LEFT":
                        direction = Direction.LEFT;
                        break;
                    case "RIGHT":
                        direction = Direction.RIGHT;
                        break;
                    default:
                        Gdx.app.error("AdvancedVisAssetManager", "Direction is incorrectly set in the file " + getSceneName(sceneNumber + 1));
                        break;
                }
                Gdx.app.debug("AdvancedVisAssetManager", "Direction is to " + dir);
            }
        }
        return direction;
    }

    /**
     * @param direction the {@link Direction}
     * @return the opposite of the given {@link Direction}
     */
    private Direction oppositeDirectionOf(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return direction;
        }
    }


    /**
     * Moves the scene to always stay on the center, used for internal call
     *
     * @param moveFirstScene true if you want to move the firstScene, false to move the second one
     * @param direction      direction where the next scene should go
     */
    private void moveSceneTo(boolean moveFirstScene, Direction direction) {
        Array<Entity> sceneToMove = (moveFirstScene) ? firstSceneEntities : secondSceneEntities;
        EntityEngine engine = scene.getEntityEngine();
        Viewport viewport = engine.getManager(CameraManager.class).getViewport();

        //direction are reversed! e.g if next scene should come on the right, the actual scene goes to left
        switch (direction) {
            case LEFT:
                for (Entity entity : sceneToMove) {
                    Bag<Component> bag = entity.getComponents(new Bag<Component>());
                    Object component = new Object();
                    for (int i = 0; i < bag.getData().length && component != null; i++) {
                        component = bag.getData()[i];
                        if (component instanceof Component) {
                            if (component instanceof PointComponent)                                //PointComponent
                                ((PointComponent) component).x += viewport.getWorldWidth();
                            else if (component instanceof PhysicsComponent) {                       //PhysicsComponent
                                ((PhysicsComponent) component).body.getPosition().add(viewport.getWorldWidth(),0);
                            } else if (component instanceof SpriteComponent) {                      //SpriteComponent
                                float x = ((SpriteComponent) component).getX();
                                ((SpriteComponent) component).setX(x + viewport.getWorldWidth());
                            } else if (component instanceof PositionComponent)                      //PositionComponent
                                ((PositionComponent) component).x += viewport.getWorldWidth();
                            else if (component instanceof TextComponent) {                          //TextComponent
                                float x = ((TextComponent) component).getX();
                                ((TextComponent) component).setX(x + viewport.getWorldWidth());
                            } else if (component instanceof SpriterComponent) {                     //SpriterComponent
                                float x = ((SpriterComponent) component).getX();
                                ((SpriterComponent) component).setX(x + viewport.getWorldWidth());
                            } else if (component instanceof PolygonComponent) {                     //PolygonComponent
                                Array<Vector2> vertices = ((PolygonComponent) component).vertices;
                                for (Vector2 v : vertices)
                                    v.add(viewport.getWorldWidth(), 0);
                            } else if (component instanceof ParticleComponent) {                    //ParticleComponent
                                float x = ((ParticleComponent) component).getX();
                                ((ParticleComponent) component).setX(x + viewport.getWorldWidth());
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (Entity entity : sceneToMove) {
                    Bag bag = entity.getComponents(new Bag<Component>());
                    Object component = new Object();
                    for (int i = 0; i < bag.getData().length && component != null; i++) {
                        component = bag.getData()[i];
                        if (component instanceof Component) {
                            if (component instanceof PointComponent)                                //PointComponent
                                ((PointComponent) component).x -= viewport.getWorldWidth();
                            else if (component instanceof PhysicsComponent) {                       //PhysicsComponent
                                ((PhysicsComponent) component).body.getPosition().add(-viewport.getWorldWidth(),0);
                            } else if (component instanceof SpriteComponent) {                      //SpriteComponent
                                float x = ((SpriteComponent) component).getX();
                                ((SpriteComponent) component).setX(x - viewport.getWorldWidth());
                            } else if (component instanceof PositionComponent)                      //PositionComponent
                                ((PositionComponent) component).x -= viewport.getWorldWidth();
                            else if (component instanceof TextComponent) {                          //TextComponent
                                float x = ((TextComponent) component).getX();
                                ((TextComponent) component).setX(x - viewport.getWorldWidth());
                            } else if (component instanceof SpriterComponent) {                     //SpriterComponent
                                float x = ((SpriterComponent) component).getX();
                                ((SpriterComponent) component).setX(x - viewport.getWorldWidth());
                            } else if (component instanceof PolygonComponent) {                     //PolygonComponent
                                Array<Vector2> vertices = ((PolygonComponent) component).vertices;
                                for (Vector2 v : vertices)
                                    v.add(-viewport.getWorldWidth(), 0);
                            } else if (component instanceof ParticleComponent) {                    //ParticleComponent
                                float x = ((ParticleComponent) component).getX();
                                ((ParticleComponent) component).setX(x - viewport.getWorldWidth());
                            }
                        }
                    }
                }
                break;
            case UP:
                for (Entity entity : sceneToMove) {
                    Bag<Component> bag = entity.getComponents(new Bag<Component>());
                    Object component = new Object();
                    for (int i = 0; i < bag.getData().length && component != null; i++) {
                        component = bag.getData()[i];
                        if (component instanceof Component) {
                            if (component instanceof PointComponent)                                //PointComponent
                                ((PointComponent) component).y -= viewport.getWorldHeight();
                            else if (component instanceof PhysicsComponent) {                       //PhysicsComponent
                                ((PhysicsComponent) component).body.getPosition().add(0,-viewport.getWorldHeight());
                            } else if (component instanceof SpriteComponent) {                      //SpriteComponent
                                float y = ((SpriteComponent) component).getY();
                                ((SpriteComponent) component).setY(y - viewport.getWorldHeight());
                            } else if (component instanceof PositionComponent)                      //PositionComponent
                                ((PositionComponent) component).y -= viewport.getWorldHeight();
                            else if (component instanceof TextComponent) {                          //TextComponent
                                float y = ((TextComponent) component).getY();
                                ((TextComponent) component).setY(y - viewport.getWorldHeight());
                            } else if (component instanceof SpriterComponent) {                     //SpriterComponent
                                float y = ((SpriterComponent) component).getY();
                                ((SpriterComponent) component).setY(y - viewport.getWorldHeight());
                            } else if (component instanceof PolygonComponent) {                     //PolygonComponent
                                Array<Vector2> vertices = ((PolygonComponent) component).vertices;
                                for (Vector2 v : vertices)
                                    v.add(0, -viewport.getWorldHeight());
                            } else if (component instanceof ParticleComponent) {                    //ParticleComponent
                                float y = ((ParticleComponent) component).getY();
                                ((ParticleComponent) component).setX(y - viewport.getWorldHeight());
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (Entity entity : sceneToMove) {
                    Bag<Component> bag = entity.getComponents(new Bag<Component>());
                    Object component = new Object();
                    for (int i = 0; i < bag.getData().length && component != null; i++) {
                        component = bag.getData()[i];
                        if (component instanceof Component) {
                            if (component instanceof PointComponent)                                //PointComponent
                                ((PointComponent) component).y += viewport.getWorldHeight();
                            else if (component instanceof PhysicsComponent) {                       //PhysicsComponent
                                ((PhysicsComponent) component).body.getPosition().add(0,viewport.getWorldHeight());
                            } else if (component instanceof SpriteComponent) {                      //SpriteComponent
                                float y = ((SpriteComponent) component).getY();
                                ((SpriteComponent) component).setY(y + viewport.getWorldHeight());
                            } else if (component instanceof PositionComponent)                      //PositionComponent
                                ((PositionComponent) component).y += viewport.getWorldHeight();
                            else if (component instanceof TextComponent) {                          //TextComponent
                                float y = ((TextComponent) component).getY();
                                ((TextComponent) component).setY(y + viewport.getWorldHeight());
                            } else if (component instanceof SpriterComponent) {                     //SpriterComponent
                                float y = ((SpriterComponent) component).getY();
                                ((SpriterComponent) component).setY(y + viewport.getWorldHeight());
                            } else if (component instanceof PolygonComponent) {                     //PolygonComponent
                                Array<Vector2> vertices = ((PolygonComponent) component).vertices;
                                for (Vector2 v : vertices)
                                    v.add(0, viewport.getWorldHeight());
                            } else if (component instanceof ParticleComponent) {                    //ParticleComponent
                                float y = ((ParticleComponent) component).getY();
                                ((ParticleComponent) component).setX(y + viewport.getWorldHeight());
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * Allows to enable FreeType support.
     *
     * @param freeTypeFontProvider must be instance of {@link FreeTypeFontProvider}. Note that this parameter is not checked!
     */
    public void enableFreeType(FontProvider freeTypeFontProvider) {
        if (freeTypeFontProvider != null) sceneLoader.enableFreeType(this, freeTypeFontProvider);
    }

    public Loader getSceneLoader() {
        return sceneLoader;
    }
}
