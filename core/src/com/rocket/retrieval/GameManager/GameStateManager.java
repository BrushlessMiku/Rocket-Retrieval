package com.rocket.retrieval.GameManager;
import com.rocket.retrieval.RocketRetrieval;
import com.rocket.retrieval.State.GameState;
import com.rocket.retrieval.State.PlayState;
import com.rocket.retrieval.State.SplashState;
import javafx.application.Application;
import java.util.Stack;

public class GameStateManager {
    private final RocketRetrieval app;
    private Stack<GameState> states;

    public enum State{

        SPLASH,
        PLAY,
    }

    public GameStateManager(final RocketRetrieval app){
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.SPLASH);
    }

    public RocketRetrieval application(){
        return app;
    }

    public void update(float delta){
        states.peek().update(delta);

    }

    public void render(){
        states.peek().render();

    }

    public void dispose(){

        for (GameState gs : states){
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h){

        states.peek().resize(w, h);

    }

    public void setState(State state){
        if(states.size()>=1){
            states.pop().dispose();
        }
        states.push(getState(state));

    }

    private GameState getState(State state){

        switch(state){
            case SPLASH: return new SplashState(this);
            case PLAY: return new PlayState(this);
        }
        return null;
    }
}