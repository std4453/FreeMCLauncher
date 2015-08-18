package com.std4453.freemclauncher.gui;

import com.std4453.freemclauncher.util.CallbackManager;
import com.std4453.freemclauncher.util.WeighableEnum;

public class WeightBasedCallback<T extends Enum<?>> implements
		CallbackManager.Callback<T> {
	protected int progress;
	protected int sum;
	protected int total;

	public WeightBasedCallback(int total) {
		progress = GuiManager.mainWindow.getProgress();
		sum = 0;
		this.total = total;
	}

	@Override
	public void execute(T t) {
		if (t == null) {
			GuiManager.mainWindow.setProgress(progress + total);
		} else if (t instanceof WeighableEnum) {
			WeighableEnum we = (WeighableEnum) t;
			sum += we.getWeight();
		}
	}

	protected void refreshProgress(WeighableEnum we) {
		GuiManager.mainWindow.setProgress(sum / we.getWeight());
	}
}
