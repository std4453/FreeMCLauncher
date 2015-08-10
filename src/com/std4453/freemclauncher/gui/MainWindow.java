package com.std4453.freemclauncher.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;

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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		this.lblNewLabel.setText(I18NHelper
				.getFormattedLocalization("gui.action.none"));
	}
}
