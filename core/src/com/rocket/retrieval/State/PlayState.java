package com.rocket.retrieval.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.rocket.retrieval.GameManager.GameStateManager;

public class PlayState extends GameState {

    private World world;
    private Body rocket;
    private Body platform;
    private Body pylon;
    private Box2DDebugRenderer boxDebug;
    private SpriteBatch rocketSprite;
    private Texture rocketTexture;
    private float scalefactor = 32;
    private Texture img;
    public PlayState(GameStateManager gsm){
        super(gsm);
        //float w = Gdx.graphics.getWidth();
        //float h = Gdx.graphics.getHeight();
        //camera.setToOrtho(false,w/2,h/2);
        world = new World(new Vector2(0,-10f),false);
        boxDebug = new Box2DDebugRenderer();
        rocket = createPlayer();
        platform = createPlatform();
        //pylon = createPylon();
        rocketSprite = new SpriteBatch();
        rocketTexture = new Texture("falcon9-render.png");

    }
    @Override
    public void update(float delta) {

        world.step(1/60f,5,2);
        cameraUpdater(delta);
        inputUpdater(delta);
        rocketSprite.setProjectionMatrix(camera.combined);

    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        boxDebug.render(world, camera.combined.scl(scalefactor));
        rocketSprite.begin();
        rocketSprite.draw(rocketTexture,(float)(rocket.getPosition().x*scalefactor-5.2),rocket.getPosition().y*scalefactor-50,
                (float)5.2,50,
                10,100,1,1,(float)Math.toDegrees(rocket.getAngle()),0,0,rocketTexture.getWidth(),rocketTexture.getHeight(),
                false,false);
        rocketSprite.end();


    }

    public int getRocketAngle() {
        int rocketAngle = (int)(Math.toDegrees(rocket.getAngle())%360);

        return Math.abs(rocketAngle);

    }

    public int getRocketAngle2(){
        int rocketAngle = (int)(Math.toDegrees(rocket.getAngle())%360);
        return rocketAngle;


    }

    public void inputUpdater(float delta){
        int horizontalForce=0;
        int verticalForce=0;
        float rocketP = rocket.getInertia()*rocket.getAngularVelocity();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            for(int i = 0; i<=10;i++) {
                horizontalForce += 1;

            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            for(int i = 0;i<=10;i++) {
                horizontalForce -= 1;

            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){

            for(int i =0;i<=100;i++) {

                verticalForce += 1;
            }

        }
        Vector2 thrust = new Vector2((float)(-verticalForce*1.5*(Math.sin(Math.toRadians(getRocketAngle2())))),
                (float)(verticalForce*1.5*Math.cos(Math.toRadians(getRocketAngle()))));
        rocket.applyForce(thrust, rocket.getWorldCenter(),true);
        rocket.applyTorque((float)(-horizontalForce*16)-rocketP,true);
        //System.out.println(rocket.getWorldCenter());
    }




    public void cameraUpdater(float delta){

        Vector3 camPosition = camera.position;
        camPosition.x= rocket.getPosition().x*scalefactor;
        camPosition.y = rocket.getPosition().y*scalefactor;
        camera.position.set(camPosition);
        camera.update();

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
        player.createFixture(shape,5.0f);
        return player;

    }

    public Body createPylon(){

        Body pylon;
        BodyDef define = new BodyDef();
        define.type=BodyDef.BodyType.DynamicBody;
        define.position.set(rocket.getPosition().x,rocket.getPosition().y);
        define.fixedRotation=false;
        pylon = world.createBody(define);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2/scalefactor, 10/scalefactor);
        pylon.createFixture(shape,1.0f);

        return pylon;

    }

    public Body createPlatform(){

        Body platform;
        BodyDef define = new BodyDef();
        define.type= BodyDef.BodyType.StaticBody;
        define.position.set(10,10);
        define.fixedRotation=true;
        platform = world.createBody(define);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(600/scalefactor,6/scalefactor);
        platform.createFixture(shape,0.0f);

        return platform;

    }

    @Override
    public void dispose() {

        img.dispose();
        boxDebug.dispose();
        world.dispose();
        rocketSprite.dispose();
        rocketTexture.dispose();

    }
}
