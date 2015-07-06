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
	private JButton button;
	private JButton btnReload;

	/**
	 * Create the panel.
	 */
	public PanelStartGame() {
		setLayout(new BorderLayout(10, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 2));
		add(scrollPane, BorderLayout.WEST);
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 10, 295, 105);
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(7, 0, 0, 0));
		
		JLabel label = new JLabel("\u7248\u672C\u4FE1\u606F\uFF1A");
		panel_2.add(label);
		
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
		
		JLabel label_2 = new JLabel("\u542F\u52A8\u8BBE\u7F6E\uFF1A");
		panel_3.add(label_2);
		
		JLabel label_3 = new JLabel("");
		panel_3.add(label_3);
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(10, 10));
		
		JLabel label_4 = new JLabel("\u9009\u62E9\u767B\u9646\u7528\u6237\uFF1A");
		panel_4.add(label_4, BorderLayout.WEST);
		
		comboBox = new JComboBox<String>();
		panel_4.add(comboBox, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 204, 295, 69);
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel label_5 = new JLabel("\u64CD\u4F5C\uFF1A");
		panel_5.add(label_5);
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_6.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_6);
		
		btnforge = new JButton("\u5B89\u88C5Forge");
		panel_6.add(btnforge);
		
		btnmod = new JButton("\u7BA1\u7406Mod");
		panel_6.add(btnmod);
		
		btnexe = new JButton("\u5BFC\u51FA\u542F\u52A8EXE");
		panel_6.add(btnexe);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
		btnReload = new JButton("\u91CD\u65B0\u626B\u63CF\u5B58\u6863");
		panel_1.add(btnReload);
		
		button = new JButton("\u542F\u52A8\u6E38\u620F");
		panel_1.add(button);

		postInit();
	}
	
	protected void postInit() {
		lblDetectedMods.setText("已知Mod：无");
		lblHasResources.setText("资源包已下载：未知");
		lblIsForged.setText("Forge已安装：未知");
		lblSelectedArchive.setText("版本名：无");
		lblGameVersion.setText("游戏主文件版本：未知");
	}
}
