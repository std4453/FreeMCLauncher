package com.std4453.freemclauncher.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.std4453.freemclauncher.i18n.I18NHelper;
import com.std4453.freemclauncher.profiles.Profile;
import com.std4453.freemclauncher.profiles.ProfileScanner;
import com.std4453.freemclauncher.util.ArrayListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class PanelStartGame extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4642274560121449326L;
	private JList<String> list;
	private JLabel lblHasResources;
	private JLabel lblDetectedMods;
	private JLabel lblIsForged;
	private JLabel lblGameVersion;
	private JLabel lblSelectedArchive;
	private JComboBox<String> comboBox;
	private JButton btnforge;
	private JButton btnmod;
	private JButton btnexe;
	private JButton btnLaunch;
	private JButton btnReload;

	/**
	 * Create the panel.
	 */
	public PanelStartGame() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 5));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 2));
		add(scrollPane, BorderLayout.WEST);

		list = new JList<String>();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshProfile();
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(model = new ArrayListModel());
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 10, 295, 105);
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(7, 0, 0, 0));

		JLabel lblInguitabsselectprofileversioninformation = new JLabel(
				"i18n:gui.tabs.selectProfile.versionInformation");
		panel_2.add(lblInguitabsselectprofileversioninformation);

		JLabel label_1 = new JLabel("");
		panel_2.add(label_1);

		lblSelectedArchive = new JLabel("SELECTED_ARCHIVE");
		panel_2.add(lblSelectedArchive);

		lblGameVersion = new JLabel("GAME_VERSION");
		panel_2.add(lblGameVersion);

		lblIsForged = new JLabel("IS_FORGED");
		panel_2.add(lblIsForged);

		lblDetectedMods = new JLabel("DETECTED_MODS");
		panel_2.add(lblDetectedMods);

		lblHasResources = new JLabel("HAS_RESOURCES");
		panel_2.add(lblHasResources);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 125, 295, 69);
		panel.add(panel_3);
		panel_3.setLayout(new GridLayout(3, 0, 0, 0));

		JLabel lblInguitabsselectprofilelaunchsettings = new JLabel(
				"i18n:gui.tabs.selectProfile.launchSettings");
		panel_3.add(lblInguitabsselectprofilelaunchsettings);

		JLabel label_3 = new JLabel("");
		panel_3.add(label_3);

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(10, 10));

		JLabel lblInguitabsselectprofileselectlaunchuser = new JLabel(
				"i18n:gui.tabs.selectProfile.selectLaunchUser");
		panel_4.add(lblInguitabsselectprofileselectlaunchuser,
				BorderLayout.WEST);

		comboBox = new JComboBox<String>();
		panel_4.add(comboBox, BorderLayout.CENTER);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 204, 295, 69);
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(2, 0, 0, 0));

		JLabel lblInguitabsselectprofileoperations = new JLabel(
				"i18n:gui.tabs.selectProfile.operations");
		panel_5.add(lblInguitabsselectprofileoperations);

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_6.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_6);

		btnforge = new JButton("i18n:gui.tabs.selectProfile.installForge");
		panel_6.add(btnforge);

		btnmod = new JButton("i18n:gui.tabs.selectProfile.manageMods");
		panel_6.add(btnmod);

		btnexe = new JButton("i18n:gui.tabs.selectProfile.exportLaunchEXE");
		panel_6.add(btnexe);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);

		btnReload = new JButton(
				"i18n:gui.tabs.selectProfile.refreshProfilesList");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadProfiles();
			}
		});
		panel_1.add(btnReload);

		btnLaunch = new JButton("i18n:gui.tabs.selectProfile.launchGame");
		panel_1.add(btnLaunch);

		postInit();
	}

	protected void postInit() {
		lblDetectedMods.setText("i18n:gui.tabs.selectProfile.knownModsNone");
		lblHasResources
				.setText("i18n:gui.tabs.selectProfile.resourcesDownloadedUnknown");
		lblIsForged
				.setText("i18n:gui.tabs.selectProfile.forgeInstalledUnknown");
		lblSelectedArchive
				.setText("i18n:gui.tabs.selectProfile.versionNameEmpty");
		lblGameVersion.setText("i18n:gui.tabs.selectProfile.mcVersionEmpty");

		loadProfiles();
	}

	ArrayListModel model;
	ProfileScanner scanner = new ProfileScanner();

	protected void loadProfiles() {
		if (GuiManager.mainWindow != null
				&& GuiManager.mainWindow.getLblNewLabel() != null)
			GuiManager.mainWindow
					.getLblNewLabel()
					.setText(
							I18NHelper
									.getFormattedLocalization("gui.action.scanningProfiles"));
		if (GuiManager.mainWindow != null
				&& GuiManager.mainWindow.getProgressBar() != null)
			GuiManager.mainWindow.getProgressBar().setValue(0);
		scanner.scanForProfiles();
		if (GuiManager.mainWindow != null
				&& GuiManager.mainWindow.getProgressBar() != null)
			GuiManager.mainWindow.getProgressBar().setValue(50);
		model.clear();
		for (Profile profile : scanner.getProfiles())
			model.add(profile.getName());
		if (GuiManager.mainWindow != null
				&& GuiManager.mainWindow.getLblNewLabel() != null)
			GuiManager.mainWindow.clearAction();
		if (GuiManager.mainWindow != null
				&& GuiManager.mainWindow.getProgressBar() != null)
			GuiManager.mainWindow.getProgressBar().setValue(100);
	}

	protected void refreshProfile() {
		int index = list.getSelectedIndex();
		Profile profile = scanner.getProfiles().get(index);
		lblSelectedArchive.setText(I18NHelper.getFormattedLocalization(
				"gui.tabs.selectProfile.versionName", profile.getName()));
		lblGameVersion.setText(I18NHelper.getFormattedLocalization(
				"gui.tabs.selectProfile.mcVersion", profile.getId()));
	}
}
