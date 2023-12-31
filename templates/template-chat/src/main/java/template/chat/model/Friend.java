package template.chat.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {
	private long id;
	private String name;
	private int photo;

	public Friend(long id, String name, int photo) {
		this.id = id;
		this.name = name;
		this.photo = photo;
	}

	public Friend(String name, int photo) {
		this.name = name;
		this.photo = photo;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPhoto() {
		return photo;
	}
}
