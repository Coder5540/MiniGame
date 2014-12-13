package com.svmc.mixxgame.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.svmc.mixxgame.Assets;
import com.svmc.mixxgame.attribute.MoveType;
import com.svmc.mixxgame.attribute.R;
import com.svmc.mixxgame.move.MoveHarmonicHorizontal;
import com.svmc.mixxgame.move.MoveRotate;

public class RectangleEntity extends Entity {
	Image	img;

	public RectangleEntity(RectangleMapObject object) {
		super();
		Color color = null;
		if (object.getProperties().containsKey("color")) {
			color = getColor((String) object.getProperties().get(
					"color"));
		} else if (object.getProperties().containsKey("color_r")) {
			color = new Color(Float.parseFloat((String) object
					.getProperties().get("color_r")) / 255f,
					Float.parseFloat((String) object.getProperties()
							.get("color_g")) / 255f,
					Float.parseFloat((String) object.getProperties()
							.get("color_b")) / 255f,
					Float.parseFloat((String) object.getProperties()
							.get("color_a")));
		}
		
		Rectangle rectangle = object.getRectangle();
		dimesion.set(rectangle.width, rectangle.height);
		position = new Vector2(rectangle.x + rectangle.width / 2, rectangle.y
				+ rectangle.height / 2);
		createUi(color);
		createMove(object);
	}
	Color getColor(String colorName) {
		if (colorName.equalsIgnoreCase("black"))
			return Color.BLACK;
		if (colorName.equalsIgnoreCase("blue"))
			return Color.BLUE;
		if (colorName.equalsIgnoreCase("clear"))
			return Color.CLEAR;
		if (colorName.equalsIgnoreCase("cyan"))
			return Color.CYAN;
		if (colorName.equalsIgnoreCase("dark_gray"))
			return Color.DARK_GRAY;
		if (colorName.equalsIgnoreCase("gray"))
			return Color.GRAY;
		if (colorName.equalsIgnoreCase("green"))
			return Color.GREEN;
		if (colorName.equalsIgnoreCase("light_gray"))
			return Color.LIGHT_GRAY;
		if (colorName.equalsIgnoreCase("magenta"))
			return Color.MAGENTA;
		if (colorName.equalsIgnoreCase("maroon"))
			return Color.MAROON;
		if (colorName.equalsIgnoreCase("navy"))
			return Color.NAVY;
		if (colorName.equalsIgnoreCase("olive"))
			return Color.OLIVE;
		if (colorName.equalsIgnoreCase("orange"))
			return Color.ORANGE;
		if (colorName.equalsIgnoreCase("pink"))
			return Color.PINK;
		if (colorName.equalsIgnoreCase("purple"))
			return Color.PURPLE;
		if (colorName.equalsIgnoreCase("red"))
			return Color.RED;
		if (colorName.equalsIgnoreCase("teal"))
			return Color.TEAL;
		if (colorName.equalsIgnoreCase("white"))
			return Color.WHITE;
		if (colorName.equalsIgnoreCase("yellow"))
			return Color.YELLOW;

		return null;
	}
	
	public void update(float delta) {
		if (move != null) {
			move.update(delta);
			move.apply(position);
			img.setPosition(position.x, position.y, Align.center);
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		img.draw(batch, 1f);
	}

	public Rectangle getRectangle() {
		return new Rectangle(position.x - dimesion.x / 2, position.y
				- dimesion.y / 2, dimesion.x, dimesion.y);
	}

	void createUi(Color color) {
		NinePatch ninePatch = new NinePatch(Assets.instance.ui.reg_ninepatch);
		img = new Image(ninePatch);
		if(color != null) img.setColor(color);
		img.setSize(dimesion.x, dimesion.y);
		img.setPosition(position.x, position.y, Align.center);
		img.addAction(Actions.visible(false));
	}

	void createMove(RectangleMapObject object) {
		MapProperties properties = object.getProperties();
		if (properties.containsKey(R.MOVE_TYPE)) {
			String moveType = (String) (properties.get(R.MOVE_TYPE));
			if (moveType.equalsIgnoreCase(MoveType.HARMONIC_X.getCode())) {
				float minX = Float.parseFloat((String) properties.get(R.MIN_X));
				float maxX = Float.parseFloat((String) properties.get(R.MAX_X));
				float minY = Float.parseFloat((String) properties.get(R.MIN_Y));
				float maxY = Float.parseFloat((String) properties.get(R.MAX_Y));

				float speed = Float
						.parseFloat((String) properties.get(R.SPEED));
				move = new MoveHarmonicHorizontal(speed, new Vector2(position.x
						+ minX, position.y + minY), new Vector2(position.x
						+ maxX, position.y + maxY), position);
				move.active();
			}

			if (moveType.equalsIgnoreCase(MoveType.HARMONIC_Y.getCode())) {

			}
			if (moveType.equalsIgnoreCase(MoveType.ROTATE.getCode())) {
				System.out.println("Rotation");
				move = new MoveRotate(0, 1);
				move.active();
			}

		}

	}
}
