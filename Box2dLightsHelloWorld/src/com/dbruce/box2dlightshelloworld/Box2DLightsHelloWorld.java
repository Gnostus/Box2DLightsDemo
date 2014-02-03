package com.dbruce.box2dlightshelloworld;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DLightsHelloWorld implements ApplicationListener {

	/*
	 * 1. BodyDef - type, position 2. Body = world.createBody(body); 3. Shape 4.
	 * FixtureDef - set shape, density, friction, restitution, etc. 5.
	 * body.createFixture(fixtureDef);
	 * 
	 * createFixture(fixtureDef, density); //for static bodies
	 */

	private OrthographicCamera camera;
	World world;
	Box2DDebugRenderer renderer;

	float width, height;

	FPSLogger logger;

	Body circleBody;

	RayHandler rayHandler;

	float accelX, accelY;

	@Override
	public void create() {
		width = Gdx.graphics.getWidth() / 5;
		height = Gdx.graphics.getHeight() / 5;

		camera = new OrthographicCamera(width, height);
		camera.position.set(width * 0.5f, height * 0.5f, 0);
		camera.update();

		world = new World(new Vector2(0, -9.8f), false);

		renderer = new Box2DDebugRenderer();

		logger = new FPSLogger();

		BodyDef circleDef = new BodyDef();
		circleDef.type = BodyType.DynamicBody;
		circleDef.position.set(width / 2f, height / 2f);

		circleBody = world.createBody(circleDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(6f);

		FixtureDef circleFixture = new FixtureDef();
		circleFixture.shape = circleShape;
		circleFixture.density = 2f;
		circleFixture.friction = 0.2f;
		circleFixture.restitution = 0.8f;

		circleBody.createFixture(circleFixture);

		setUpWalls();

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(camera.combined);

		new PointLight(rayHandler, 5000, Color.CYAN, 400, (width / 2 - 50),
				(height / 2) + 15);
		new ConeLight(rayHandler, 5000, Color.PINK, 400, (width / 2) + 50,
				(height / 2) + 15, 270, 35);

	}

	private void setUpWalls() {
		// ground
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 3);

		Body groundBody = world.createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth * 2, 3.0f);

		groundBody.createFixture(groundBox, 0.0f);

		// leftwall
		BodyDef leftWallDef = new BodyDef();
		leftWallDef.position.set(3, 3);

		Body leftWall = world.createBody(leftWallDef);

		PolygonShape leftWallShape = new PolygonShape();
		leftWallShape.setAsBox(3.0f, camera.viewportHeight * 2);

		leftWall.createFixture(leftWallShape, 0.0f);

		// rightWall
		BodyDef rightWallDef = new BodyDef();
		rightWallDef.position.set(camera.viewportWidth - 3, 3);

		Body rightWall = world.createBody(rightWallDef);

		PolygonShape rightWallShape = new PolygonShape();
		rightWallShape.setAsBox(3.0f, camera.viewportHeight * 2);

		rightWall.createFixture(rightWallShape, 0.0f);

		// ceiling
		BodyDef ceilingDef = new BodyDef();
		ceilingDef.position.set(0, camera.viewportHeight - 3);

		Body ceiling = world.createBody(ceilingDef);

		PolygonShape ceilingShape = new PolygonShape();
		ceilingShape.setAsBox(camera.viewportWidth * 2, 3.0f);

		ceiling.createFixture(ceilingShape, 0.0f);
	}

	@Override
	public void dispose() {
		world.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		checkInput();
		renderer.render(world, camera.combined);
		rayHandler.updateAndRender();

		world.step(1 / 60f, 8, 3);

		logger.log();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void checkInput() {
		// if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
		accelX = -Gdx.input.getAccelerometerX();
		accelY = -Gdx.input.getAccelerometerY();

		Gdx.app.log("accelerometer: ", accelX + ", " + accelY);
		circleBody.applyForceToCenter(accelX * 1000, accelY * 1000, true);
		// }

	}
}
