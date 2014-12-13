package com.svmc.mixxgame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * @author Coder5560
 */
public class LineLaser {

	public Vector2	startPoint	= new Vector2();
	public Vector2	endPoint	= new Vector2();
	public float	degrees;
	public Color	color		= new Color(0 / 255f, 0 / 255f, 220 / 255f, 1f);
	public Color	rayColor	= new Color(Color.WHITE);

	public Image	begin, beginRay, mid, midRay, end, endRay;

	public LineLaser() {
		super();
		setup();
	}

	public LineLaser(Vector2 startPoint, Vector2 endPoint, Color color,
			Color rayColor, Image begin, Image beginRay, Image mid,
			Image midRay, Image end, Image endRay) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
		this.rayColor = rayColor;
		this.begin = begin;
		this.beginRay = beginRay;
		this.mid = mid;
		this.midRay = midRay;
		this.end = end;
		this.endRay = endRay;
	}

	public LineLaser(Vector2 startPoint, Vector2 endPoint) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;

		setup();
	}

	void setup() {
		Texture texLaserS1 = new Texture(
				Gdx.files.internal("data/beamstart1.png"));
		Texture texLaserS2 = new Texture(
				Gdx.files.internal("data/beamstart2.png"));
		Texture texLaserM1 = new Texture(
				Gdx.files.internal("data/beammid1.png"));
		Texture texLaserM2 = new Texture(
				Gdx.files.internal("data/beammid2.png"));

		beginRay = new Image(texLaserS2);
		begin = new Image(texLaserS1);
		midRay = new Image(texLaserM2);
		mid = new Image(texLaserM1);
		endRay = new Image(texLaserS2);
		end = new Image(texLaserS1);
	}

	public void render(SpriteBatch batch, float delta) {
		float distance = Vector2.dst(startPoint.x, startPoint.y, endPoint.x,
				endPoint.y);
		degrees = getAngle(startPoint, endPoint);
		begin.setColor(color);
		beginRay.setColor(rayColor);
		mid.setColor(color);
		midRay.setColor(rayColor);
		end.setColor(color);
		endRay.setColor(rayColor);

		mid.setSize(mid.getWidth(), distance - begin.getWidth());
		midRay.setSize(midRay.getWidth(), distance - beginRay.getWidth());

		begin.setPosition(startPoint.x, startPoint.y, Align.center);
		beginRay.setPosition(startPoint.x, startPoint.y, Align.center);
		end.setPosition(endPoint.x, endPoint.y, Align.center);
		endRay.setPosition(endPoint.x, endPoint.y, Align.center);
		mid.setPosition(startPoint.x / 2 + endPoint.x / 2, startPoint.y / 2
				+ endPoint.y / 2, Align.center);
		midRay.setPosition(startPoint.x / 2 + endPoint.x / 2, startPoint.y / 2
				+ endPoint.y / 2, Align.center);

		mid.setOrigin(Align.center);
		midRay.setOrigin(Align.center);
		begin.setOrigin(Align.center);
		beginRay.setOrigin(Align.center);
		end.setOrigin(Align.center);
		endRay.setOrigin(Align.center);

		begin.setRotation(degrees);
		beginRay.setRotation(degrees);
		mid.setRotation(degrees);
		midRay.setRotation(degrees);
		end.setRotation(degrees + 180);
		endRay.setRotation(degrees + 180);

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		begin.draw(batch, 1f);
		beginRay.draw(batch, 1f);
		mid.draw(batch, 1f);
		midRay.draw(batch, 1f);
		end.draw(batch, 1f);
		endRay.draw(batch, 1f);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public float getAngle(Vector2 start, Vector2 end) {
		float angle = (float) Math.toDegrees(Math.atan2(-end.x + start.x, end.y
				- start.y));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	public boolean intersect(Rectangle bound) {
		Vector2 point = new Vector2(bound.x + bound.width / 2, bound.y
				+ bound.height / 2);

		if (Intersector.distanceSegmentPoint(startPoint, endPoint, point) <= bound.height / 2) {
			return true;
		}
		return false;
	}
}
