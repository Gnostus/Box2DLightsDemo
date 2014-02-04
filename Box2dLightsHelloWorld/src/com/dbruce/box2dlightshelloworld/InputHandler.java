package com.dbruce.box2dlightshelloworld;

import java.util.Iterator;

import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class InputHandler implements InputProcessor {

	private World world;
	private Iterator<Light> lightIter;
	private Vector2 gravity;
	private RayHandler rayHandler;
	Vector3 touch = new Vector3();
	Vector2 v2Touch;
	private Camera camera;

	public InputHandler(World world, RayHandler rayHandler, Camera camera) {
		this.world = world;
		this.rayHandler = rayHandler;
		this.camera = camera;
		gravity = world.getGravity();
	}

	@Override
	public boolean keyDown(int keycode) {
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		switch (character) {
		case 'w':
			if (gravity.y <= 10)
				gravity.y += 0.25f;
			break;
		case 's':
			if (gravity.y >= -10)
				gravity.y -= 0.25f;
			break;
		case 'a':
			if (gravity.x >= -10)
				gravity.x -= 0.25f;
			break;
		case 'd':
			if (gravity.x <= 10)
				gravity.x += 0.25f;
			break;
		default:
			break;
		}
		world.setGravity(gravity);
		Gdx.app.log("KeyDown: ", gravity.x + ", " + gravity.y);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Gdx.app.log("Pointer: ", ""+pointer);
		if(pointer == 0){
			touch.set(screenX, screenY, 0);
		camera.unproject(touch);
		
		Light light = rayHandler.lightList.get(0);		
		light.setPosition(touch.x, touch.y);
		}
		if(pointer == 1){
			touch.set(screenX, screenY, 0);
		camera.unproject(touch);
		
		Light light = rayHandler.lightList.get(1);		
		light.setPosition(touch.x, touch.y);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Gdx.app.log("Mouse: ", screenX + ", " + screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		Light light = rayHandler.lightList.get(0);
		light.setDistance(light.getDistance() + 5*amount);
		return false;
	}
}
