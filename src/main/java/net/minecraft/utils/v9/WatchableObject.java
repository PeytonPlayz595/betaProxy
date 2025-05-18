package net.minecraft.utils.v9;

public class WatchableObject {
	private final int objectType;
	private final int dataValueId;
	private Object watchedObject;

	public WatchableObject(int var1, int var2, Object var3) {
		this.dataValueId = var2;
		this.watchedObject = var3;
		this.objectType = var1;
	}

	public int getDataValueId() {
		return this.dataValueId;
	}

	public void setObject(Object var1) {
		this.watchedObject = var1;
	}

	public Object getObject() {
		return this.watchedObject;
	}

	public int getObjectType() {
		return this.objectType;
	}
}
