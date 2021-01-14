package shop.utils;

import shop.db.ConnectionManager;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DesktopRender {

	public static final String FONT_FAMILY ="HelveticaNeue" ;
	public static final Color JTF_COLOR = new Color(169, 204, 227 );
	public static final String DATE_FORMAT = "dd/MM/yyyy";




	public static String formatButton(String first) {
		return "<html><center>" + first + "</center></html>";
	}

	public static String formatButton(String first, String second) {
		return "<html><center>" + first + "<br>" + second + "</center></html>";
	}

	public static String formatButton(String sup, String first, String second) {
		return "<html><sup style=\"color:#FFDEAD; font-size: 9px; font-weight: bold; \">" + sup + "</sup><center>"
				+ first + "<br>" + second + "</center></html>";
	}










	public static void resizeColumnWidth(JTable table) {

		for (int column = 0; column < table.getColumnCount(); column++) {
			TableColumn tableColumn = table.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth() + 120;
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
				Component c = table.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				//  We've exceeded the maximum width, no need to check other rows

				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setResizable(false);



/*
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 40; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
            table.getColumnModel().getColumn(column).setResizable(false);
        }
        */
	}
}
