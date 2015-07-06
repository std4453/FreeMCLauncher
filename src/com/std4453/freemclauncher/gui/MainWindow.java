package com.std4453.freemclauncher.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class MainWindow {

	private JFrame frmFreeMclauncherV;
	private JTabbedPane tabbedPane;
	private PanelStartGame panelStartGame;
	private PanelDownloadGame panelDownloadGame;
	private PanelSettings panelSettings;
	private PanelManageUsers panelManageUsers;
	private PanelAbout panelAbout;

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
		frmFreeMclauncherV.setTitle("Free MCLauncher v1.01");
		BorderLayout borderLayout = (BorderLayout) frmFreeMclauncherV.getContentPane().getLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		frmFreeMclauncherV.setBounds(100, 100, 590, 474);
		frmFreeMclauncherV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFreeMclauncherV.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		panelStartGame = new PanelStartGame();
		tabbedPane.addTab("ѡ��浵", panelStartGame);
		panelDownloadGame = new PanelDownloadGame();
		tabbedPane.addTab("������Ϸ�ļ�", panelDownloadGame);
		panelSettings = new PanelSettings();
		tabbedPane.add("����", panelSettings);
		panelManageUsers = new PanelManageUsers();
		tabbedPane.add("�����û�",panelManageUsers);
		panelAbout = new PanelAbout();
		tabbedPane.add("����", panelAbout);
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
}
