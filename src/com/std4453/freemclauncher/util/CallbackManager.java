package com.std4453.freemclauncher.util;

import static com.std4453.freemclauncher.logging.Logger.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.std4453.freemclauncher.util.CallbackManager.Callback;

public class CallbackManager<T extends Enum<?>> implements
		Iterable<Callback<T>> {
	protected List<Callback<T>> listeners;

	public CallbackManager() {
		this.listeners = new Vector<>();
	}

	public void addCallBack(Callback<T> callback) {
		this.listeners.add(callback);
	}

	public int size() {
		return this.listeners.size();
	}

	public void clear() {
		this.listeners.clear();
	}

	public Callback<T> getCallbackAt(int index) {
		return this.listeners.get(index);
	}

	public Callback<T> removeCallbackAt(int index) {
		return this.listeners.remove(index);
	}

	public void removeCallback(Callback<T> callback) {
		this.listeners.remove(callback);
	}

	public void finish() {
		broadcast(null);
		this.listeners.clear();
	}

	public void broadcast(T t) {
		if (DEBUG_FLAG && t != null)
			log(INFO, t.toString());

		for (Callback<T> callback : this)
			if (callback != null)
				try {
					callback.execute(t);
				} catch (Throwable throwable) {
					log(ERROR, throwable);
				}
	}

	@Override
	public Iterator<Callback<T>> iterator() {
		return new ArrayList<>(this.listeners).iterator();
	}

	public static interface Callback<T extends Enum<?>> {
		public void execute(T t);
	}
}
