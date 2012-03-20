package com.barcicki.trio.core;

import android.graphics.drawable.Drawable;

public class GameType {

	private int id;
	private Drawable name;
	private Drawable icon;
	private String description;
	
	public GameType(int id, Drawable name, Drawable icon, String description) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Drawable getName() {
		return name;
	}

	public void setName(Drawable name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
