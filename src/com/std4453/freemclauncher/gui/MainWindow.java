package com.std4453.freemclauncher.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.std4453.freemclauncher.i18n.I18NHelper;

public class MainWindow {

	private JFrame frmFreeMclauncherV;
	private JTabbedPane tabbedPane;
	private PanelStartGame panelStartGame;
	private PanelDownloadGame panelDownloadGame;
	private PanelSettings panelSettings;
	private PanelManageUsers panelManageUsers;
	private PanelAbout panelAbout;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmFreeMclauncherV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Another constructor which does not initializes the frame when
	 * constructed. As in the code, all the panels are accessed via
	 * {@codeGuiManager.mainWindow.getPanelXXX()}, if any panel tries to access
	 * it while initialization, it will cause an {@code NullPointerException} to
	 * be thrown, because GuiManager.mainWindow is {@code null} before the
	 * constructor returns.<br>
	 * Instead of the {@code MainWindow(void)} constructor, this one accepts a
	 * parameter, but this is only for the purpose to separate the two
	 * constructor and is never used, as WindowBuilder required the first one.<br>
	 * To avoid this kind of problems mentioned before, The whole construction
	 * should look like this:
	 * <p>
	 * <code>
	 * MainWindow mainWindow=new MainWindow(null);<br>
	 * mainWindow.initialize();
	 * </code>
	 */
	public MainWindow(GuiManager unused) {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	protected void initialize() {
		frmFreeMclauncherV = new JFrame();
		frmFreeMclauncherV.setMinimumSize(new Dimension(588, 530));
		frmFreeMclauncherV.setTitle("Free MCLauncher v1.01");
		BorderLayout borderLayout = (BorderLayout) frmFreeMclauncherV
				.getContentPane().getLayout();
		borderLayout.setVgap(5);
		borderLayout.setHgap(10);
		frmFreeMclauncherV.setBounds(100, 100, 588, 530);
		frmFreeMclauncherV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFreeMclauncherV.getContentPane()
				.add(tabbedPane, BorderLayout.CENTER);

		panelStartGame = new PanelStartGame();
		tabbedPane.addTab(I18NHelper
				.getFormattedLocalization("gui.tabs.selectProfile.name"),
				panelStartGame);
		panelDownloadGame = new PanelDownloadGame();
		tabbedPane.addTab(I18NHelper
				.getFormattedLocalization("gui.tabs.downloadGameFiles.name"),
				panelDownloadGame);
		panelSettings = new PanelSettings();
		tabbedPane.add(
				I18NHelper.getFormattedLocalization("gui.tabs.settings.name"),
				panelSettings);
		panelManageUsers = new PanelManageUsers();
		tabbedPane.add(I18NHelper
				.getFormattedLocalization("gui.tabs.manageUsers.name"),
				panelManageUsers);
		panelAbout = new PanelAbout();
		tabbedPane.add(
				I18NHelper.getFormattedLocalization("gui.tabs.about.name"),
				panelAbout);

		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frmFreeMclauncherV.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(5, 5));

		lblNewLabel = new JLabel("CURRENT_ACTION");
		lblNewLabel.setBorder(new EmptyBorder(3, 3, 3, 0));
		panel.add(lblNewLabel, BorderLayout.CENTER);

		progressBar = new JProgressBar();
		progressBar.setBorder(new EmptyBorder(3, 0, 3, 3));
		panel.add(progressBar, BorderLayout.EAST);

		clearAction();
	}

	public JFrame getFrame() {
		return frmFreeMclauncherV;
	}

	public PanelManageUsers getPanelManageUsers() {
		return panelManageUsers;
	}

	public PanelSettings getPanelSettings() {
		return panelSettings;
	}

	public PanelDownloadGame getPanelDownloadGame() {
		return panelDownloadGame;
	}

	public PanelStartGame getPanelStartGame() {
		return panelStartGame;
	}

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void clearAction() {
		actionStrings.clear();

		refreshAction();
	}

	protected void refreshAction() {
		if (actionStrings.size() == 0) {
			this.lblNewLabel.setText(I18NHelper
					.getFormattedLocalization("general.none"));
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (String action : actionStrings) {
			sb.append(action);
			if (action != actionStrings.get(actionStrings.size() - 1))
				sb.append(" - ");
		}

		this.lblNewLabel.setText(sb.toString());
	}

	protected Stack<String> actionStrings = new Stack<String>();

	public void pushAction(String str) {
		actionStrings.push(str == null ? I18NHelper
				.getFormattedLocalization("general.none") : str);

		refreshAction();
	}

	public void pushAction() {
		pushAction(null);

		refreshAction();
	}

	public void popAction() {
		if (actionStrings.size() > 0)
			actionStrings.pop();

		refreshAction();
	}

	public void replaceAction(String str) {
		if (actionStrings.size() > 0)
			actionStrings.set(actionStrings.size() - 1, str);

		refreshAction();
	}

	public void setProgress(int progress) {
		this.progressBar.setValue(progress);
	}

	public int getProgress() {
		return this.progressBar.getValue();
	}
}
