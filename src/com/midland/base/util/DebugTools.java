package com.midland.base.util;

import java.util.ListIterator;
import java.util.Vector;

import android.graphics.Bitmap;
import android.os.Debug;

public class DebugTools {
	
	static Vector<PageInfo> list = new Vector<PageInfo>();
	
	public static class PageInfo {
		String id;
		String name;
	}
	
	public static void onCreate(String id, String name) {
		PageInfo info = new PageInfo();
		info.id = id;
		info.name = name;
		list.add(info);
		printList();
	}
	
	public static void onDestroy(String id, String name) {
		ListIterator<PageInfo> iterator = list.listIterator();
		while (iterator != null && iterator.hasNext()) {
			PageInfo pi = iterator.next();
			if (pi.id.equals(id)) {
				iterator.remove();
			}
		}
		printList();
	}
	
	public static void printList() {
		heapSize();
		Common.v("Page List Size: " + list.size());
		for (int i = 0; i < list.size(); i++) {
			PageInfo info = list.get(i);
			Common.v("Page : " + info.name + ":" + info.id);
		}
	}
	
	public static void heapSize() {
		Common.v("Native Heap Total: "
				+ humanReadableByteCount(Debug.getNativeHeapSize(), true)
				+ " Allocated: "
				+ humanReadableByteCount(Debug.getNativeHeapAllocatedSize(), true)
				+ " Free: "
				+ humanReadableByteCount(Debug.getNativeHeapFreeSize(), true));
	}
	
	public static void logBitmap(String tag, Bitmap bitmap) {
		Common.d(tag + " :" + bitmap.getWidth() + ":" + bitmap.getHeight() + ":" + humanReadableByteCount(bitmap.getByteCount(), true));
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
