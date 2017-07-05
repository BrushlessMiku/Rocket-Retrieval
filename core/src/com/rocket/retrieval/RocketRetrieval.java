package com.rocket.retrieval;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocket.retrieval.GameManager.GameStateManager;

public class RocketRetrieval extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private GameStateManager gsm;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager(this);
		camera = new OrthographicCamera();
	}
	@Override
	public void render () {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}
	@Override
	public void resize(int w, int h){
		gsm.resize(w,h);
	}
	public OrthographicCamera getCamera(){
		return camera;
	}

	public SpriteBatch getBatch(){
		return batch;
	}
	@Override
	public void dispose() {
		gsm.dispose();
		batch.dispose();

	}
}
