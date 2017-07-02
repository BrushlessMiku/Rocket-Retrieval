package com.rocket.retrieval;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
		camera.setToOrtho(false, (w/2),h/2);
		world = new World(new Vector2(0,-98.1f),false);
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
		rocketSprite.draw(rocketTexture,rocket.getPosition().x*scalefactor-5,rocket.getPosition().y*scalefactor-30,13,100);
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

		rocket.setLinearVelocity(horizontalForce*3,verticalForce*3);
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
		define.position.set(10,10);
		define.fixedRotation=false;
		player = world.createBody(define);


		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4/scalefactor,32/scalefactor);
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
		shape.setAsBox(200/scalefactor,6/scalefactor);
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
