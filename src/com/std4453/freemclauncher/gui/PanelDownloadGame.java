package com.std4453.freemclauncher.gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTree;
import javax.swing.JScrollPane;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.EmptyBorder;

import com.std4453.freemclauncher.i18n.I18NHelper;

public class PanelDownloadGame extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4140573850594024763L;
	private JLabel lblVersionCode;
	private JLabel lblVersionCatrgory;
	private JLabel lblPublicationDate;
	private JLabel lblCompileDate;
	private JButton btnInguitabsdownloadgamefilesdownloadprimarygamefiles;
	private JButton btnInguitabsdownloadgamefilesdownloadgameresourcefiles;

	/**
	 * Create the panel.
	 */
	public PanelDownloadGame() {
		setMinimumSize(new Dimension(450, 300));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(5, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 2));
		add(scrollPane, BorderLayout.WEST);
		
		JTree tree = new JTree();
		tree.setBorder(new EmptyBorder(3, 3, 3, 3));
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Minecraft") {
				/**
				 * 
				 */
				private static final long serialVersionUID = -3666761813622617552L;

				{
					add(new DefaultMutableTreeNode(I18NHelper.getFormattedLocalization("gui.tabs.downloadGameFiles.latestVersions")));
					add(new DefaultMutableTreeNode(I18NHelper.getFormattedLocalization("gui.tabs.downloadGameFiles.allVersions")));
				}
			}
		));
		scrollPane.setViewportView(tree);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblInguitabsdownloadgamefilesversiondescription = new JLabel("i18n:gui.tabs.downloadGameFiles.versionDescription");
		lblInguitabsdownloadgamefilesversiondescription.setBounds(10, 10, 319, 15);
		panel.add(lblInguitabsdownloadgamefilesversiondescription);
		
		JLabel lblInguitabsdownloadgamefilesversion = new JLabel("i18n:gui.tabs.downloadGameFiles.version");
		lblInguitabsdownloadgamefilesversion.setBounds(10, 46, 76, 15);
		panel.add(lblInguitabsdownloadgamefilesversion);
		
		lblVersionCode = new JLabel("VERSION_CODE");
		lblVersionCode.setBounds(96, 46, 233, 15);
		panel.add(lblVersionCode);
		
		JLabel lblInguitabsdownloadgamefilescompiletime = new JLabel("i18n:gui.tabs.downloadGameFiles.compileTime");
		lblInguitabsdownloadgamefilescompiletime.setBounds(10, 71, 76, 15);
		panel.add(lblInguitabsdownloadgamefilescompiletime);
		
		lblCompileDate = new JLabel("COMPILE_DATE");
		lblCompileDate.setBounds(96, 71, 233, 15);
		panel.add(lblCompileDate);
		
		JLabel lblInguitabsdownloadgamefilesreleasetime = new JLabel("i18n:gui.tabs.downloadGameFiles.releaseTime");
		lblInguitabsdownloadgamefilesreleasetime.setBounds(10, 96, 76, 15);
		panel.add(lblInguitabsdownloadgamefilesreleasetime);
		
		lblPublicationDate = new JLabel("PUBLICATION_DATE");
		lblPublicationDate.setBounds(96, 96, 233, 15);
		panel.add(lblPublicationDate);
		
		JLabel lblInguitabsdownloadgamefilesversiontype = new JLabel("i18n:gui.tabs.downloadGameFiles.versionType");
		lblInguitabsdownloadgamefilesversiontype.setBounds(10, 121, 76, 15);
		panel.add(lblInguitabsdownloadgamefilesversiontype);
		
		lblVersionCatrgory = new JLabel("VERSION_CATRGORY");
		lblVersionCatrgory.setBounds(96, 121, 233, 15);
		panel.add(lblVersionCatrgory);
		
		btnInguitabsdownloadgamefilesdownloadprimarygamefiles = new JButton("i18n:gui.tabs.downloadGameFiles.downloadPrimaryGameFiles");
		btnInguitabsdownloadgamefilesdownloadprimarygamefiles.setBounds(10, 175, 169, 23);
		panel.add(btnInguitabsdownloadgamefilesdownloadprimarygamefiles);
		
		btnInguitabsdownloadgamefilesdownloadgameresourcefiles = new JButton("i18n:gui.tabs.downloadGameFiles.downloadGameResourceFiles");
		btnInguitabsdownloadgamefilesdownloadgameresourcefiles.setBounds(10, 207, 169, 23);
		panel.add(btnInguitabsdownloadgamefilesdownloadgameresourcefiles);
		
		JLabel lblInguitabsdownloadgamefilesnote = new JLabel("i18n:gui.tabs.downloadGameFiles.note");
		lblInguitabsdownloadgamefilesnote.setBounds(10, 331, 319, 15);
		panel.add(lblInguitabsdownloadgamefilesnote);

	}
}
