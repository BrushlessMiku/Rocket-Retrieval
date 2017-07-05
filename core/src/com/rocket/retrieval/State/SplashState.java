package com.rocket.retrieval.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.rocket.retrieval.GameManager.GameStateManager;

public class SplashState extends GameState {

    float acc=0f;
    Texture tex;

    public SplashState(GameStateManager gsm){

        super(gsm);
        tex = new Texture("splash.jpg");

    }

    @Override
    public void update(float delta) {

        acc +=delta;
        if(acc>=5){

            gsm.setState(GameStateManager.State.PLAY);
        }

    }

    @Override
    public void render() {

        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.getBatch().setProjectionMatrix(app.getCamera().combined);
        app.getBatch().begin();
        app.getBatch().draw(tex,0,0);
        app.getBatch().end();


    }

    @Override
    public void dispose() {

    }
}
