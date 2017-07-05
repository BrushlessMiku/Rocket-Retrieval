package com.rocket.retrieval.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rocket.retrieval.GameManager.GameStateManager;

public class PlayState extends GameState {
    public PlayState(GameStateManager gsm){
        super(gsm);

    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


    }

    @Override
    public void dispose() {

    }
}
