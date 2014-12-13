package com.svmc.mixxgame;

import utils.interfaces.IGameEvent;
import utils.listener.OnDoneListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svmc.mixxgame.attribute.EventType;
import com.svmc.mixxgame.attribute.R;
import com.svmc.mixxgame.entity.Entity.State;
import com.svmc.mixxgame.entity.FollowEntity;
import com.svmc.mixxgame.entity.FollowEntity.FollowType;
import com.svmc.mixxgame.entity.Line;
import com.svmc.mixxgame.entity.MainPlayer;
import com.svmc.mixxgame.entity.RectangleEntity;

public class GameSystem {
	public TiledMap					map;
	public static Rectangle			goalred, goalblue;
	public Array<RectangleEntity>	rects		= new Array<RectangleEntity>();

	private MainPlayer				mainPlayer;
	public static boolean			redDone		= false;
	public static boolean			blueDone	= false;
	Array<FollowEntity>				follows		= new Array<FollowEntity>();
	Array<Line>						lines		= new Array<Line>();
	Line							gameBound;

	public GameSystem() {
	}

	public void updateWorld(float delta, IGameEvent event) {
		if (gameBound != null
				&& (gameBound.intersect(mainPlayer.getBlueBound()) || gameBound
						.intersect(mainPlayer.getBlueBound()))) {
			event.broadcastEvent(EventType.GAME_OVER, mainPlayer.position.x,
					mainPlayer.position.y);
			return;
		}
		for (RectangleEntity entity : rects) {
			entity.update(delta);
		}

		for (FollowEntity followEntity : follows) {
			followEntity.update(delta);
		}
		
		
		for (RectangleEntity entity : rects) {
			Rectangle rectangle = entity.getRectangle();
			if (rectangle.overlaps(mainPlayer.getBlueBound())
					|| rectangle.overlaps(mainPlayer.getRedBound())) {
				event.broadcastEvent(EventType.GAME_OVER,
						mainPlayer.position.x, mainPlayer.position.y);
				break;
			}
		}

		for (Line line : lines) {
			if (line.intersect(mainPlayer.getBlueBound())
					|| line.intersect(mainPlayer.getRedBound())) {
				event.broadcastEvent(EventType.GAME_OVER,
						mainPlayer.position.x, mainPlayer.position.y);
				break;
			}
		}

		for (FollowEntity followEntity : follows) {
			Rectangle followRect = followEntity.getRectangle();
			Rectangle blueRect = mainPlayer.getBlueBound();
			Rectangle redRect = mainPlayer.getRedBound();
			if (followRect.overlaps(blueRect)) {
				event.broadcastEvent(EventType.GAME_OVER, followRect.x / 2
						+ followRect.width / 4 + blueRect.x / 2
						+ blueRect.width / 4, followRect.y / 2
						+ followRect.height / 4 + blueRect.y / 2
						+ blueRect.height / 4);
				break;
			}

			if (followRect.overlaps(redRect)) {
				event.broadcastEvent(EventType.GAME_OVER, followRect.x / 2
						+ followRect.width / 4 + redRect.x / 2 + redRect.width
						/ 4, followRect.y / 2 + followRect.height / 4
						+ redRect.y / 2 + redRect.height / 4);
				break;
			}

			for (Line line : lines) {
				if (line.intersect(followRect)) {
					followEntity.reset(new OnDoneListener() {
						@Override
						public void done() {

						}
					});
					break;
				}
			}
		}
		autoCreateFollow();
	}

	public void render(SpriteBatch batch, float delta) {
		for (RectangleEntity entity : rects) {
			entity.render(batch, delta);
		}
		for (Line line : lines) {
			line.render(batch, delta);
		}
		for (FollowEntity follow : follows) {
			follow.render(batch, delta);
			for (RectangleEntity rect : rects) {
				if (follow.getRectangle().overlaps(rect.getRectangle())) {
					follow.reset(new OnDoneListener() {
						@Override
						public void done() {

						}
					});
				}
			}
		}
		

	}

	int		count		= 0;
	boolean	isFollowRed	= false;

	void autoCreateFollow() {
		if(Level.getLevel() <=4) return;
		if (follows.size >= 2)
			return;
		if (mainPlayer != null)
			count++;
		if (count == 300) {
			FollowEntity followEntity = new FollowEntity(
					isFollowRed ? FollowType.RED : FollowType.BLUE);
			isFollowRed = !isFollowRed;
			followEntity.registerPlayer(mainPlayer);
			follows.add(followEntity);
			count = 0;
		}
	}

	public boolean isGameComplete() {
		return isBlueDone() && isRedDone();
	}

	public void registerMainPlayer(MainPlayer mainPlayer) {
		this.mainPlayer = mainPlayer;
	}

	public boolean isRedDone() {
		if (mainPlayer == null)
			return false;
		if (mainPlayer.getState() == State.LIVE && !redDone
				&& goalred.overlaps(mainPlayer.getRedBound())) {
			redDone = true;
		}
		return redDone;
	}

	public boolean isBlueDone() {
		if (mainPlayer == null)
			return false;

		if (mainPlayer.getState() == State.LIVE && !blueDone
				&& goalblue.overlaps(mainPlayer.getBlueBound())) {
			blueDone = true;
		}
		return blueDone;
	}

	public void reset() {
		blueDone = false;
		redDone = false;
	}

	public void createWorld() {
		rects.clear();
		Assets.instance.assetMap.loadLevel(Level.getLevel());
		map = Assets.instance.assetMap.getMap();
		for (MapObject object : map.getLayers().get(R.LAYER_ENVIROMENT)
				.getObjects()) {
			if (object instanceof RectangleMapObject) {
				if (object.getName() != null
						&& object.getName().equalsIgnoreCase("goalred")) {
					goalred = ((RectangleMapObject) object).getRectangle();
				} else if (object.getName() != null
						&& object.getName().equalsIgnoreCase("goalblue")) {
					goalblue = ((RectangleMapObject) object).getRectangle();
				} else {
					rects.add(new RectangleEntity((RectangleMapObject) object));
				}
			}
			if (object instanceof PolylineMapObject) {
				Polyline polyline = ((PolylineMapObject) object).getPolyline();
				float[] vts = polyline.getTransformedVertices();
				Array<Vector2> points = new Array<Vector2>();
				for (int i = 0; i < vts.length - 1; i++) {
					if (i % 2 == 0) {
						Vector2 point = new Vector2(vts[i], vts[i + 1]);
						points.add(point);
					}
				}
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

				Line line = (color == null) ? new Line(points) : new Line(
						points, color);
				lines.add(line);
				if (object.getName() != null
						&& object.getName().equalsIgnoreCase("gamebound")) {
					gameBound = line;
				}
			}
		}
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

	public void resetFollow() {
		for (FollowEntity follow : follows) {
			follow.reset(new OnDoneListener() {
				@Override
				public void done() {
				}
			});
		}
	}

}
