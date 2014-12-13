package com.svmc.mixxgame.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Line {
	public Array<Vector2>	vertices;
	public Array<LineLaser>	lasers;
	Color color;
	public Line(Array<Vector2> vertices) {
		super();
		lasers = new Array<LineLaser>();
		this.vertices = vertices;
		createLaser(vertices);
	}
	public Line(Array<Vector2> vertices, Color color) {
		super();
		lasers = new Array<LineLaser>();
		this.color = color;
		this.vertices = vertices;
		createLaser(vertices);
	}

	void createLaser(Array<Vector2> vertice) {
		if (vertice.size < 2)
			return;
		for (int i = 0; i < vertice.size - 1; i++) {
			LineLaser line = new LineLaser(vertice.get(i), vertice.get(i + 1));
			if(color != null){
				line.color = color;
			}
			lasers.add(line);
		}
	}

	public void render(SpriteBatch batch, float delta) {
		for (LineLaser laser : lasers) {
			laser.render(batch, delta);
		}
	}
	
	public boolean intersect(Rectangle bound){
		for(LineLaser laser : lasers){
			if(laser.intersect(bound)) return true;
		}
		return false;
	}

}
