package com.rocket.retrieval.State;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.rocket.retrieval.GameManager.GameStateManager;
public class PlayState extends GameState {

    private World world;
    private Body rocket,platform,pylon;
    private Box2DDebugRenderer boxDebug;
    private SpriteBatch rocketSprite, skyTexture, skyTexture2,droneShipTexture;
    //private Sprite droneShipTexture;
    private Texture rocketTexture,sky,droneShip,sky2;
    private float scalefactor = 32;
    private int posXFrame1=-500;
    private int posXFrame2=0;
    public PlayState(GameStateManager gsm){
        super(gsm);

        world = new World(new Vector2(0,-0.0f),false);
        boxDebug = new Box2DDebugRenderer();
        rocket = createPlayer();
        platform = createPlatform();
        rocketSprite = new SpriteBatch();
        rocketTexture = new Texture("falcon9-render.png");
        skyTexture = new SpriteBatch();
        sky = new Texture("skyMirror.png");
        skyTexture2 = new SpriteBatch();
        sky2 = new Texture("sky.png");
        droneShip = new Texture("droneship.png");
        droneShipTexture = new SpriteBatch();

    }
    @Override
    public void update(float delta) {

        world.step(1/60f,5,2);
        cameraUpdater(delta);
        inputUpdater(delta);
        skyTexture.setProjectionMatrix(camera.combined);
        skyTexture2.setProjectionMatrix(camera.combined);
        droneShipTexture.setProjectionMatrix(camera.combined);
        rocketSprite.setProjectionMatrix(camera.combined);

    }

    public void PhaseUpdate(){
        System.out.println(rocket.getPosition().x);
        if(rocket.getLinearVelocity().x>0) {
            posXFrame2 = (posXFrame1 + 2000);
            if (camera.position.x>= posXFrame2 + camera.viewportWidth /2) {
                posXFrame1 = posXFrame2;
            }
        }

        if(rocket.getLinearVelocity().x<0){
            posXFrame1 = (posXFrame2-2000);
            if(camera.position.x<=posXFrame1+camera.viewportWidth/1.5){
                posXFrame2=posXFrame1;
            }
        }
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        boxDebug.render(world, camera.combined.scl(scalefactor));
        PhaseUpdate();
        skyTexture.begin();
        skyTexture.draw(sky,posXFrame1,-650);
        skyTexture.draw(sky,posXFrame2,-650);
        skyTexture.end();

        if(rocket.getPosition().y>280){
            //loads upper atmosphere texture
            skyTexture2.begin();
            skyTexture2.draw(sky2,posXFrame1,300*scalefactor);
            skyTexture2.draw(sky2,posXFrame2,300*scalefactor);
            skyTexture2.end();
        }
        droneShipTexture.begin();
        droneShipTexture.draw(droneShip,platform.getPosition().x*scalefactor-500,platform.getPosition().y*scalefactor-10,0,0,
                1000,50,1,1,0,0,0,droneShip.getWidth(),droneShip.getHeight(),false,false);
        droneShipTexture.end();
        rocketSprite.begin();
        rocketSprite.draw(rocketTexture,(rocket.getPosition().x*scalefactor-5),rocket.getPosition().y*scalefactor-50,
                (float)5,50,
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
            for(int i = 0; i<=100;i++) {
                horizontalForce += 1;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            for(int i = 0;i<=100;i++) {
                horizontalForce -= 1;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){

            for(int i =0;i<=50;i++) {
                verticalForce += 1;
            }

        }
        /*
       Vector2 thrust = new Vector2((float)(-verticalForce*1.8*(Math.sin(Math.toRadians(getRocketAngle2())))),
                (float)(verticalForce*1.8*Math.cos(Math.toRadians(getRocketAngle()))));
        rocket.applyForce(thrust, rocket.getWorldCenter(),true);
        */
        //rocket.applyTorque((float)(-horizontalForce*.8)+rocketP,true);
        rocket.setLinearVelocity(horizontalForce,0);
        //rocket.getAngularDamping();
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
        define.position.set(10,10);
        define.fixedRotation=false;
        define.angularDamping=5.0f;
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
        //platform.setTransform(10,10,1);

        return platform;

    }

    @Override
    public void dispose() {

        skyTexture.dispose();
        sky.dispose();
        boxDebug.dispose();
        world.dispose();
        rocketSprite.dispose();
        rocketTexture.dispose();

    }
}
