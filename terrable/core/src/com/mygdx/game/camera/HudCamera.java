package com.mygdx.game.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class HudCamera {
    private OrthographicCamera cam;
	private StretchViewport viewport;
	
	public HudCamera (int width, int height) {
		cam = new OrthographicCamera();

        cam.setToOrtho(false, width , height);

		cam.position.set(width / 2, height / 2, 0);
		cam.update();
	}
	
	public Matrix4 combined() {
		return cam.combined;
	}

    public void resize(int width, int height) {
		viewport.update(width, height);
	}

    public OrthographicCamera getCamera(){
        return cam;
    }

    public void setPosition(float x, float y){
        cam.position.set(x,y, 0);
    }

	
	public void update () {
		cam.update();
	}

	public Vector2 getInputInGameWorld () {
		Vector3 inputScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 unprojected = cam.unproject(inputScreen);
		return new Vector2(unprojected.x, unprojected.y);
	}
}
