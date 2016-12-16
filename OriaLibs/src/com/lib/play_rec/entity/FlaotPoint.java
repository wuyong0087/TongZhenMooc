package com.lib.play_rec.entity;

public class FlaotPoint {
	float x, y;

	public FlaotPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FlaotPoint) {
			FlaotPoint p = (FlaotPoint) o;
			if ((int) x == (int) p.getX() && (int) p.getY() == (int) y) {
				return true;
			}
		}
		return false;
	}
}
