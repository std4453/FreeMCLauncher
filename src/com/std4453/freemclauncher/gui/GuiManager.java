package com.std4453.freemclauncher.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.std4453.freemclauncher.i18n.I18NHelper;

public class GuiManager {
	public static MainWindow mainWindow;

	public static void create() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}	

		mainWindow = new MainWindow(null);
		mainWindow.initialize();
		mainWindow.getFrame().setVisible(true);

		I18NHelper.localizeTexts(mainWindow.getFrame());
	}

	public static void main(String[] args) {
		GuiManager.create();
	}
}
