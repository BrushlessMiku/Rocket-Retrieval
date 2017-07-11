package com.rocket.retrieval.State;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocket.retrieval.GameManager.GameStateManager;
import com.rocket.retrieval.RocketRetrieval;

public abstract class GameState {
    protected GameStateManager gsm;
    protected RocketRetrieval app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        app = gsm.application();
        batch = app.getBatch();
        camera = app.getCamera();

    }

    public void resize(int w, int h){
          camera = app.getCamera();
          camera.setToOrtho(false,w/2,h/2);
    }

    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

}
