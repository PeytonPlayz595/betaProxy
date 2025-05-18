package net.minecraft.utils.v9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.utils.ItemStack;

public class DataWatcher {
	
	public static List<WatchableObject> readWatchableObjects(DataInputStream var0) throws IOException {
		ArrayList<WatchableObject> var1 = null;

		for(byte var2 = var0.readByte(); var2 != 127; var2 = var0.readByte()) {
			if(var1 == null) {
				var1 = new ArrayList<WatchableObject>();
			}

			int var3 = (var2 & 224) >> 5;
			int var4 = var2 & 31;
			WatchableObject var5 = null;
			switch(var3) {
			case 0:
				var5 = new WatchableObject(var3, var4, Byte.valueOf(var0.readByte()));
				break;
			case 1:
				var5 = new WatchableObject(var3, var4, Short.valueOf(var0.readShort()));
				break;
			case 2:
				var5 = new WatchableObject(var3, var4, Integer.valueOf(var0.readInt()));
				break;
			case 3:
				var5 = new WatchableObject(var3, var4, Float.valueOf(var0.readFloat()));
				break;
			case 4:
				var5 = new WatchableObject(var3, var4, var0.readUTF());
				break;
			case 5:
				short var6 = var0.readShort();
				byte var7 = var0.readByte();
				short var8 = var0.readShort();
				new WatchableObject(var3, var4, new ItemStack(var6, var7, var8));
			case 6:
				int var9 = var0.readInt();
				int var10 = var0.readInt();
				int var11 = var0.readInt();
				var5 = new WatchableObject(var3, var4, new ChunkCoordinates(var9, var10, var11));
			}

			var1.add(var5);
		}

		return var1;
	}
	
	public static void writeObjectsInListToStream(List<WatchableObject> var0, DataOutputStream var1) throws IOException {
		if(var0 != null) {
			Iterator<WatchableObject> var2 = var0.iterator();

			while(var2.hasNext()) {
				WatchableObject var3 = var2.next();
				writeWatchableObject(var1, var3);
			}
		}

		var1.writeByte(127);
	}
	
	private static void writeWatchableObject(DataOutputStream var0, WatchableObject var1) throws IOException {
		int var2 = (var1.getObjectType() << 5 | var1.getDataValueId() & 31) & 255;
		var0.writeByte(var2);
		switch(var1.getObjectType()) {
		case 0:
			var0.writeByte(((Byte)var1.getObject()).byteValue());
			break;
		case 1:
			var0.writeShort(((Short)var1.getObject()).shortValue());
			break;
		case 2:
			var0.writeInt(((Integer)var1.getObject()).intValue());
			break;
		case 3:
			var0.writeFloat(((Float)var1.getObject()).floatValue());
			break;
		case 4:
			var0.writeUTF((String)var1.getObject());
			break;
		case 5:
			ItemStack var3 = (ItemStack)var1.getObject();
			var0.writeShort(var3.itemID); //no need to get shifted index
			//The "itemID" and "shiftedIndex" are both (256 + id)
			
			var0.writeByte(var3.stackSize);
			var0.writeShort(var3.itemDamage);
		case 6:
			ChunkCoordinates var4 = (ChunkCoordinates)var1.getObject();
			var0.writeInt(var4.field_22395_a);
			var0.writeInt(var4.field_22394_b);
			var0.writeInt(var4.field_22396_c);
		}

	}

}
