package com.dbruce.box2dlightshelloworld;

import java.util.Iterator;

import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class InputHandler implements InputProcessor {

	private World world;
	private Iterator<Light> lightIter;
	private Vector2 gravity;
	private RayHandler rayHandler;
	Vector3 touch = new Vector3();
	Vector2 v2Touch;
	private OrthographicCamera camera;
	Light light, light2;
	Boolean shift = false;
	Array<Body> bullets = new Array<Body>();

	Vector2 v2Clicked = new Vector2();
	Vector3 clicked = new Vector3();

	float bulletWidth = 0, bulletHeight = 0;
	boolean dragged;

	public InputHandler(World world, RayHandler rayHandler,
			OrthographicCamera camera) {
		this.world = world;
		this.rayHandler = rayHandler;
		this.camera = camera;
		gravity = world.getGravity();
		light = rayHandler.lightList.get(0);
		light2 = rayHandler.lightList.get(1);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.SHIFT_LEFT:
			shift = true;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.SHIFT_LEFT:
			shift = false;
			break;
		}
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
		case '-':
			light.setDistance(light.getDistance() - 5);
			break;
		case '=':
			light.setDistance(light.getDistance() + 5);
			break;
		case '_':
			light2.setDistance(light2.getDistance() - 5);
			break;
		case '+':
			light2.setDistance(light2.getDistance() + 5);
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
		if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
				|| !Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			clicked.x = screenX;
			clicked.y = screenY;
			camera.unproject(clicked);
			v2Clicked.x = clicked.x;
			v2Clicked.y = clicked.y;

		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (dragged) {
			if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| !Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
				BodyDef bulletDef = new BodyDef();
				bulletDef.type = BodyType.DynamicBody;
				bulletDef.bullet = true;
				bulletDef.position.set(v2Clicked);

				Body bullet = world.createBody(bulletDef);

				PolygonShape bulletShape = new PolygonShape();
				bulletShape.setAsBox(0.5f, 0.2f);

				FixtureDef bulletFixtureDef = new FixtureDef();
				bulletFixtureDef.shape = bulletShape;
				bulletFixtureDef.density = 10000;
				bulletFixtureDef.restitution = 0.5f;
				
				bullet.createFixture(bulletFixtureDef);

				bullet.applyLinearImpulse(new Vector2(3000, 3000), bullet.getLocalCenter(), false);
				
				bullets.add(bullet);

			}
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		dragged = true;
		if (pointer == 0) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);

			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				light2.setPosition(touch.x, touch.y);
			else if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				light.setPosition(touch.x, touch.y);
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (camera.zoom + amount > 0) {
			camera.zoom += amount;
			camera.update();
			rayHandler.setCombinedMatrix(camera.combined);
			rayHandler.updateAndRender();
			Gdx.app.log("zoom: ", "" + camera.zoom);
		}
		return true;
	}
}
