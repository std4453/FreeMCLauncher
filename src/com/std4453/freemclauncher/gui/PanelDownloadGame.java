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

public class PanelDownloadGame extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4140573850594024763L;
	private JLabel lblVersionCode;
	private JLabel lblVersionCatrgory;
	private JLabel lblPublicationDate;
	private JLabel lblCompileDate;
	private JButton button;
	private JButton button_1;
	private JButton button_2;

	/**
	 * Create the panel.
	 */
	public PanelDownloadGame() {
		setLayout(new BorderLayout(10, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 2));
		add(scrollPane, BorderLayout.WEST);
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Minecraft") {
				/**
				 * 
				 */
				private static final long serialVersionUID = -3666761813622617552L;

				{
					add(new DefaultMutableTreeNode("\u6700\u65B0\u7248\u672C"));
					add(new DefaultMutableTreeNode("\u6240\u6709\u7248\u672C"));
				}
			}
		));
		scrollPane.setViewportView(tree);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u7248\u672C\u63CF\u8FF0\uFF1A");
		label.setBounds(10, 10, 319, 15);
		panel.add(label);
		
		JLabel label_1 = new JLabel("\u7248\u672C\u53F7  \uFF1A");
		label_1.setBounds(10, 46, 76, 15);
		panel.add(label_1);
		
		lblVersionCode = new JLabel("VERSION_CODE");
		lblVersionCode.setBounds(96, 46, 233, 15);
		panel.add(lblVersionCode);
		
		JLabel label_2 = new JLabel("\u7F16\u8BD1\u65F6\u95F4\uFF1A");
		label_2.setBounds(10, 71, 76, 15);
		panel.add(label_2);
		
		lblCompileDate = new JLabel("COMPILE_DATE");
		lblCompileDate.setBounds(96, 71, 233, 15);
		panel.add(lblCompileDate);
		
		JLabel label_3 = new JLabel("\u53D1\u5E03\u65F6\u95F4\uFF1A");
		label_3.setBounds(10, 96, 76, 15);
		panel.add(label_3);
		
		lblPublicationDate = new JLabel("PUBLICATION_DATE");
		lblPublicationDate.setBounds(96, 96, 233, 15);
		panel.add(lblPublicationDate);
		
		JLabel label_4 = new JLabel("\u7248\u672C\u7C7B\u578B\uFF1A");
		label_4.setBounds(10, 121, 76, 15);
		panel.add(label_4);
		
		lblVersionCatrgory = new JLabel("VERSION_CATRGORY");
		lblVersionCatrgory.setBounds(96, 121, 233, 15);
		panel.add(lblVersionCatrgory);
		
		button = new JButton("\u4E0B\u8F7D\u6E38\u620F\u4E3B\u6587\u4EF6");
		button.setBounds(10, 174, 169, 23);
		panel.add(button);
		
		button_1 = new JButton("\u4E0B\u8F7D\u6E38\u620F\u8D44\u6E90\u6587\u4EF6");
		button_1.setBounds(10, 207, 169, 23);
		panel.add(button_1);
		
		button_2 = new JButton("\u4E0B\u8F7D\u6E38\u620F\u8BED\u8A00\u5305");
		button_2.setBounds(10, 240, 169, 23);
		panel.add(button_2);
		
		JLabel label_5 = new JLabel("\u6CE8\uFF1A\u4E0D\u4E0B\u8F7D\u8D44\u6E90\u6587\u4EF6\u5219\u65E0\u6CD5\u64AD\u653E\u97F3\u6548\uFF0C");
		label_5.setBounds(10, 313, 319, 15);
		panel.add(label_5);
		
		JLabel label_6 = new JLabel("\u4E0D\u4E0B\u8F7D\u8BED\u8A00\u5305\u5219\u53EA\u6709\u82F1\u6587\u3002");
		label_6.setBounds(10, 338, 319, 15);
		panel.add(label_6);

	}
}
