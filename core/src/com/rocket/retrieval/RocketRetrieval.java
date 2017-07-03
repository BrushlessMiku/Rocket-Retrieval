package com.rocket.retrieval;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import javax.xml.soap.Text;

public class RocketRetrieval extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private OrthographicCamera camera;
	private World world;
	private Body rocket;
	private Body platform;
	private Box2DDebugRenderer boxDebug;
	private SpriteBatch rocketSprite;
	private Texture rocketTexture;
	private float scalefactor = 32;
	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/2,h/2);
		world = new World(new Vector2(0,-200f),false);
		boxDebug = new Box2DDebugRenderer();
		rocket = createPlayer();
		platform = createPlatform();
		rocketSprite = new SpriteBatch();
		rocketTexture = new Texture("falcon9-render.png");

	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		boxDebug.render(world, camera.combined.scl(scalefactor));
		rocketSprite.begin();
		/*rocketSprite.draw(rocketTexture,rocket.getPosition().x*scalefactor,rocket.getPosition().y*scalefactor,
				rocket.getPosition().x/2*scalefactor,rocket.getPosition().y/2*scalefactor,
				10,100,1,1,(float)Math.toDegrees(rocket.getAngle()),0,0,rocketTexture.getWidth(),rocketTexture.getHeight(),
				false,false);*/
		rocketSprite.draw(rocketTexture,(float)(rocket.getPosition().x*scalefactor-5.2),rocket.getPosition().y*scalefactor-50,
				(float)5.2,50,
				10,100,1,1,(float)Math.toDegrees(rocket.getAngle()),0,0,rocketTexture.getWidth(),rocketTexture.getHeight(),
				false,false);
		//System.out.println(rocket.getPosition().y);
		rocketSprite.end();

	}

	public void update(float delta){
		world.step(1/60f,5,2);
		cameraUpdater(delta);
		inputUpdater(delta);
		rocketSprite.setProjectionMatrix(camera.combined);


	}

	public void inputUpdater(float delta){
		int horizontalForce=0;
		int verticalForce=0;
		Vector2 forceVector = new Vector2(0,-50);
		Vector2 positionVector = new Vector2(rocket.getPosition().x,rocket.getPosition().y+20);

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

			horizontalForce+=1;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){

			horizontalForce-=1;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.UP)){

			verticalForce+=1;

		}

		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){

			verticalForce-=1;
		}

		rocket.applyTorque(torqCalc(),true);
		rocket.setLinearVelocity(0,verticalForce*6);
		rocket.setAngularVelocity((float)(-horizontalForce*1.6));
	}

	public float torqCalc() {
		//physics stuff goes here
		float angle = rocket.getAngle();
		float forceCalc = (float)(400*Math.sin(angle));



		return forceCalc;

	}

	public void cameraUpdater(float delta){

		Vector3 camPosition = camera.position;
		camPosition.x= rocket.getPosition().x*scalefactor;
		camPosition.y = rocket.getPosition().y*scalefactor;
		camera.position.set(camPosition);
		camera.update();

		}

	@Override

	public void resize(int width, int height){

		camera.setToOrtho(false, width/2,height/2);
	}

	public Body createPlayer(){
		//creates game object for rocket
		Body player;
		BodyDef define = new BodyDef();
		define.type=BodyDef.BodyType.DynamicBody;
		define.position.set(10,20);
		define.fixedRotation=false;
		player = world.createBody(define);


		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8/scalefactor,55/scalefactor);
		player.createFixture(shape,3.0f);
		return player;

	}

	public Body createPlatform(){

		Body platform;
		BodyDef define = new BodyDef();
		define.type= BodyDef.BodyType.StaticBody;
		define.position.set(10,10);
		define.fixedRotation=true;
		platform = world.createBody(define);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(400/scalefactor,6/scalefactor);
		platform.createFixture(shape,0.0f);

		return platform;

	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		boxDebug.dispose();
		world.dispose();
		rocketSprite.dispose();
	}
}
