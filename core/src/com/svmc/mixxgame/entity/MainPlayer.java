package com.svmc.mixxgame.entity;

import utils.interfaces.IGameEvent;
import utils.listener.OnDoneListener;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svmc.mixxgame.GameSystem;
import com.svmc.mixxgame.Level;
import com.svmc.mixxgame.attribute.Constants;
import com.svmc.mixxgame.attribute.EventType;

public class MainPlayer extends Entity implements InputProcessor {
	public Vector2			target		= new Vector2(
												Constants.WIDTH_SCREEN / 2 - 40,
												Constants.HEIGHT_SCREEN / 2);
	public Vector2			iniposition	= new Vector2();

	private OnDoneListener	onReset;

	public MainPlayer() {
		velocity.set(0, 100);
		acceleration.set(0, 0);
		dimesion.set(36, 36);
	}

	public void reset() {
		setState(State.LIVE);
		target.set(iniposition);
	}

	public void reset(OnDoneListener onDoneListener) {
		setState(State.BLOCK);
		this.onReset = onDoneListener;
	}

	public boolean isAlive() {
		return getState() == State.LIVE;
	}

	public void update(float delta, IGameEvent event) {
		if (getState() == State.LIVE) {
			position.lerp(target, 0.04f);
			if (getRedBound().overlaps(getBlueBound())) {
				event.broadcastEvent(EventType.PLAYER_COLLISION,
						getRedBound().x / 2 + getRedBound().width / 4
								+ getBlueBound().x / 2 + getBlueBound().width
								/ 4, getRedBound().y / 2 + getRedBound().height
								/ 4 + getBlueBound().y / 2
								+ getBlueBound().height / 4);
			}
		}
		if (getState() == State.BLOCK) {
			target.set(iniposition);
			position.lerp(target, 0.06f);
			if (position.epsilonEquals(target, 1)) {
				if (onReset != null) {
					onReset.done();
					setState(State.LIVE);
				}
			}
		}
	}

	public void flip() {
		acceleration.x *= -1;
	}

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// target.set(screenX, Constants.HEIGHT_SCREEN- screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void pan(float x, float y, float deltaX, float deltaY) {
		if (getState() != State.LIVE)
			return;
		target.x -= deltaX;
		target.y += deltaY;
	}

	public Rectangle getRedBound() {
		if (GameSystem.redDone)
			return GameSystem.goalred;
		if (Level.getLevel() < 4)
			return new Rectangle(position.x - dimesion.x / 2, position.y
					- dimesion.y / 2, dimesion.x, dimesion.y);

		return new Rectangle(position.x - dimesion.x / 2, position.y
				- dimesion.y / 2, dimesion.x, dimesion.y);
	}

	public Rectangle getBlueBound() {
		if (GameSystem.blueDone)
			return GameSystem.goalblue;
		if (Level.getLevel() < 4)
			return new Rectangle((Constants.WIDTH_SCREEN - position.x)
					- dimesion.x / 2, Constants.HEIGHT_SCREEN - position.y
					- dimesion.y / 2, dimesion.x, dimesion.y);

		return new Rectangle(
				(Constants.WIDTH_SCREEN - position.x) - dimesion.x, position.y
						- dimesion.y / 2, dimesion.x, dimesion.y);
	}
	// public Rectangle getRedBound() {
	// return new Rectangle(position.x - dimesion.x / 2, position.y
	// - dimesion.y / 2, dimesion.x, dimesion.y);
	// }
	//
	// public Rectangle getBlueBound() {
	// return new Rectangle((Constants.WIDTH_SCREEN - position.x) - dimesion.x
	// / 2, Constants.HEIGHT_SCREEN - position.y - dimesion.y / 2,
	// dimesion.x, dimesion.y);
	// }

}
