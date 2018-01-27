package org.schema.nintendospy.gui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import gnu.io.CommPortIdentifier;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class GUISettings extends JPanel {

	/**
	 * Create the panel.
	 */
	public GUISettings(final GUIMain main, final JDialog d) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		JPanel panel_settings = new JPanel();
		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.insets = new Insets(0, 0, 5, 0);
		gbc_panel_settings.fill = GridBagConstraints.BOTH;
		gbc_panel_settings.gridx = 0;
		gbc_panel_settings.gridy = 0;
		add(panel_settings, gbc_panel_settings);
		GridBagLayout gbl_panel_settings = new GridBagLayout();
		gbl_panel_settings.columnWidths = new int[]{0, 0};
		gbl_panel_settings.rowHeights = new int[]{0, 0, 0};
		gbl_panel_settings.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_settings.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_settings.setLayout(gbl_panel_settings);
		
		JPanel panel_port = new JPanel();
		panel_port.setBorder(new TitledBorder(null, "Port", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_port = new GridBagConstraints();
		gbc_panel_port.insets = new Insets(0, 0, 5, 0);
		gbc_panel_port.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_port.gridx = 0;
		gbc_panel_port.gridy = 0;
		panel_settings.add(panel_port, gbc_panel_port);
		GridBagLayout gbl_panel_port = new GridBagLayout();
		gbl_panel_port.columnWidths = new int[]{0, 0};
		gbl_panel_port.rowHeights = new int[]{0, 0};
		gbl_panel_port.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_port.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_port.setLayout(gbl_panel_port);
		
		final JComboBox comboBoxPort = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel_port.add(comboBoxPort, gbc_comboBox);
		
		JPanel panel_skin = new JPanel();
		panel_skin.setBorder(new TitledBorder(null, "Skin", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_skin = new GridBagConstraints();
		gbc_panel_skin.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_skin.gridx = 0;
		gbc_panel_skin.gridy = 1;
		panel_settings.add(panel_skin, gbc_panel_skin);
		GridBagLayout gbl_panel_skin = new GridBagLayout();
		gbl_panel_skin.columnWidths = new int[]{0, 0};
		gbl_panel_skin.rowHeights = new int[]{0, 0};
		gbl_panel_skin.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_skin.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_skin.setLayout(gbl_panel_skin);
		
		final JComboBox comboBoxSkin = new JComboBox();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 0;
		gbc_comboBox_1.gridy = 0;
		panel_skin.add(comboBoxSkin, gbc_comboBox_1);
		
		JPanel panel_ok_cancel = new JPanel();
		GridBagConstraints gbc_panel_ok_cancel = new GridBagConstraints();
		gbc_panel_ok_cancel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_ok_cancel.gridx = 0;
		gbc_panel_ok_cancel.gridy = 1;
		add(panel_ok_cancel, gbc_panel_ok_cancel);
		GridBagLayout gbl_panel_ok_cancel = new GridBagLayout();
		gbl_panel_ok_cancel.columnWidths = new int[]{0, 0, 0};
		gbl_panel_ok_cancel.rowHeights = new int[]{0, 0};
		gbl_panel_ok_cancel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_ok_cancel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_ok_cancel.setLayout(gbl_panel_ok_cancel);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setupSerial((CommPortIdentifier)comboBoxPort.getSelectedItem(), (String)comboBoxSkin.getSelectedItem());
				d.dispose();
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.weighty = 1.0;
		gbc_btnStart.weightx = 1.0;
		gbc_btnStart.anchor = GridBagConstraints.WEST;
		gbc_btnStart.insets = new Insets(0, 0, 0, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 0;
		panel_ok_cancel.add(btnStart, gbc_btnStart);
		
		JButton btnEnd = new JButton("End");
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
		GridBagConstraints gbc_btnEnd = new GridBagConstraints();
		gbc_btnEnd.weighty = 1.0;
		gbc_btnEnd.weightx = 1.0;
		gbc_btnEnd.anchor = GridBagConstraints.EAST;
		gbc_btnEnd.gridx = 1;
		gbc_btnEnd.gridy = 0;
		panel_ok_cancel.add(btnEnd, gbc_btnEnd);
		
		
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		Vector<CommPortIdentifier> m = new Vector<CommPortIdentifier>();
		while(portEnum.hasMoreElements()){
			m.add(portEnum.nextElement());
		}
		comboBoxPort.setModel(new DefaultComboBoxModel<CommPortIdentifier>(m));
		
		comboBoxPort.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, ((CommPortIdentifier)value).getName(), index, isSelected, cellHasFocus);
			}

		});
		
		File skins = new File("./data/skins/");
		
		comboBoxSkin.setModel(new DefaultComboBoxModel<String>(skins.list()));
		
	}

}
