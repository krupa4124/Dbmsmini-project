package crime;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Centralised theme + UI factory for the Crime Management System.
 * Dark navy base with deep-red accents and gold highlights — a police/forensics aesthetic.
 */
public class Theme {

    // ── Palette ───────────────────────────────────────────────────────────────
    public static final Color BG_DARK   = new Color(10, 11, 18);
    public static final Color BG_CARD   = new Color(18, 21, 33);
    public static final Color BG_INPUT  = new Color(27, 31, 48);
    public static final Color ACCENT    = new Color(220, 53, 69);    // police red
    public static final Color GOLD      = new Color(255, 193, 7);    // warning gold
    public static final Color GREEN     = new Color(40, 167, 69);
    public static final Color BLUE      = new Color(13, 110, 253);
    public static final Color PURPLE    = new Color(111, 66, 193);
    public static final Color TEAL      = new Color(32, 201, 151);
    public static final Color ORANGE    = new Color(253, 126, 20);
    public static final Color RED_DARK  = new Color(180, 30, 45);
    public static final Color TEXT_MAIN = new Color(220, 225, 255);
    public static final Color TEXT_DIM  = new Color(110, 125, 160);
    public static final Color ROW_ALT   = new Color(22, 26, 42);
    public static final Color SEL_ROW   = new Color(80, 20, 25);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font F_TAB    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font F_SECT   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_BTN    = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font F_TABLE  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_THEAD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_STAT_V = new Font("Segoe UI", Font.BOLD, 30);
    public static final Font F_STAT_L = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Status-color map ──────────────────────────────────────────────────────
    public static Color statusColor(String s) {
        if (s == null) return TEXT_DIM;
        return switch (s.toLowerCase()) {
            case "open", "active", "pending"     -> GOLD;
            case "under investigation", "ongoing" -> ORANGE;
            case "closed", "convicted", "solved"  -> GREEN;
            case "acquitted", "released"          -> TEAL;
            case "fugitive", "at large"           -> ACCENT;
            case "critical", "severe"             -> ACCENT;
            case "minor", "none"                  -> GREEN;
            default                               -> TEXT_DIM;
        };
    }

    // ── Panel / Card ──────────────────────────────────────────────────────────
    public static JPanel panel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_DARK);
        return p;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 20, 25), 1, true),
            new EmptyBorder(16, 18, 16, 18)
        ));
        return p;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_SECT);
        l.setForeground(ACCENT);
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 20, 25)),
            new EmptyBorder(0, 0, 8, 0)
        ));
        return l;
    }

    // ── Text field ────────────────────────────────────────────────────────────
    public static JTextField addField(JPanel p, String label) {
        JPanel wrap = new JPanel(new BorderLayout(0, 4));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL); lbl.setForeground(TEXT_DIM);
        JTextField tf = new JTextField();
        tf.setFont(F_INPUT);
        tf.setBackground(BG_INPUT); tf.setForeground(TEXT_MAIN); tf.setCaretColor(ACCENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 35, 40), 1, true),
            new EmptyBorder(7, 10, 7, 10)
        ));
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(tf, BorderLayout.CENTER);
        p.add(wrap);
        return tf;
    }

    // ── Combo box ─────────────────────────────────────────────────────────────
    public static JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JPanel wrap = new JPanel(new BorderLayout(0, 4));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL); lbl.setForeground(TEXT_DIM);
        JComboBox<String> cb = styledCombo(items);
        wrap.add(lbl, BorderLayout.NORTH); wrap.add(cb, BorderLayout.CENTER);
        p.add(wrap);
        return cb;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(F_INPUT);
        cb.setBackground(BG_INPUT); cb.setForeground(TEXT_MAIN);
        cb.setBorder(new LineBorder(new Color(60, 35, 40), 1, true));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, value, idx, sel, focus);
                setBackground(sel ? ACCENT.darker() : BG_INPUT);
                setForeground(sel ? Color.WHITE : TEXT_MAIN);
                setBorder(new EmptyBorder(5, 8, 5, 8));
                return this;
            }
        });
        return cb;
    }

    // ── Button ────────────────────────────────────────────────────────────────
    public static JButton btn(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(F_BTN); b.setForeground(Color.WHITE);
        b.setBackground(color.darker()); b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 16, 9, 16));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(color); }
            public void mouseExited(MouseEvent e)  { b.setBackground(color.darker()); }
        });
        return b;
    }

    public static JButton smallBtn(String text, Color color) {
        JButton b = btn(text, color);
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return b;
    }

    // ── Stat card ─────────────────────────────────────────────────────────────
    public static JPanel statCard(String label, JLabel valueLabel, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent.darker(), 1, true),
            new EmptyBorder(16, 18, 16, 18)
        ));
        JLabel top = new JLabel(emoji + "  " + label);
        top.setFont(F_STAT_L); top.setForeground(TEXT_DIM);
        valueLabel.setFont(F_STAT_V); valueLabel.setForeground(accent);
        card.add(top, BorderLayout.NORTH); card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ── Table ─────────────────────────────────────────────────────────────────
    public static JTable styledTable(DefaultTableModel model, int statusCol) {
        JTable t = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : ROW_ALT);
                    c.setForeground(TEXT_MAIN);
                } else {
                    c.setBackground(SEL_ROW);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        t.setFont(F_TABLE); t.setRowHeight(30);
        t.setBackground(BG_CARD); t.setForeground(TEXT_MAIN);
        t.setGridColor(new Color(35, 40, 60)); t.setShowGrid(true);
        t.setSelectionBackground(SEL_ROW); t.setSelectionForeground(Color.WHITE);
        t.setIntercellSpacing(new Dimension(1, 1));
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        if (statusCol >= 0) t.getColumnModel().getColumn(statusCol).setCellRenderer(new StatusCellRenderer());
        JTableHeader th = t.getTableHeader();
        th.setFont(F_THEAD);
        th.setBackground(new Color(35, 10, 15)); th.setForeground(ACCENT);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        th.setReorderingAllowed(false);
        return t;
    }

    public static JScrollPane scrollPane(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(new LineBorder(new Color(40, 20, 25), 1));
        return sp;
    }

    // ── Search bar ────────────────────────────────────────────────────────────
    public static void styleSearchField(JTextField tf) {
        tf.setFont(F_INPUT);
        tf.setBackground(BG_INPUT); tf.setForeground(TEXT_MAIN); tf.setCaretColor(ACCENT);
        tf.setPreferredSize(new Dimension(250, 34));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 35, 40), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    // ── Status renderer ───────────────────────────────────────────────────────
    public static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            String s = value == null ? "" : value.toString();
            setForeground(isSelected ? Color.WHITE : statusColor(s));
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(isSelected ? SEL_ROW : (row % 2 == 0 ? BG_CARD : ROW_ALT));
            return this;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    public static String comboVal(JComboBox<String> cb) {
        String v = (String) cb.getSelectedItem();
        return (v == null || v.startsWith("Select")) ? null : v;
    }

    public static void setCombo(JComboBox<String> cb, String value) {
        if (value == null) { cb.setSelectedIndex(0); return; }
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (value.equalsIgnoreCase(cb.getItemAt(i))) { cb.setSelectedIndex(i); return; }
        }
    }

    public static String str(DefaultTableModel m, int row, int col) {
        Object v = m.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    public static int parseId(String comboSelection) {
        if (comboSelection == null || comboSelection.startsWith("Select")) return 0;
        try { return Integer.parseInt(comboSelection.split(":")[0].trim()); }
        catch (Exception e) { return 0; }
    }
}
