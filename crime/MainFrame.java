package crime;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static crime.Theme.*;

public class MainFrame extends JFrame {

    CrimeDAO dao = new CrimeDAO();

    // ── Dashboard labels ──────────────────────────────────────────────────────
    JLabel dCrimes, dOpen, dSuspects, dOfficers, dVictims, dEvidence;

    // ── Crimes ────────────────────────────────────────────────────────────────
    JTextField cTitle, cLocation, cDate, cDesc, cSearch;
    JComboBox<String> cType, cStatus, cSeverity, cOfficer;
    JTable cTable; DefaultTableModel cModel;

    // ── Suspects ──────────────────────────────────────────────────────────────
    JTextField sName, sAge, sPhone, sAddress, sNationality, sNotes, sSearch;
    JComboBox<String> sGender, sPrior, sStatus, sCrime;
    JTable sTable; DefaultTableModel sModel;

    // ── Officers ──────────────────────────────────────────────────────────────
    JTextField oName, oBadge, oDept, oPhone, oEmail, oSearch;
    JComboBox<String> oRank, oStatus;
    JTable oTable; DefaultTableModel oModel;

    // ── Victims ───────────────────────────────────────────────────────────────
    JTextField vName, vAge, vContact, vAddress, vStatement, vSearch;
    JComboBox<String> vGender, vInjury, vCrime;
    JTable vTable; DefaultTableModel vModel;

    // ── Evidence ──────────────────────────────────────────────────────────────
    JTextField eDesc, eCollectedBy, eDate, eLocation, eSearch;
    JComboBox<String> eType, eStatus, eLabStatus, eCrime;
    JTable eTable; DefaultTableModel eModel;

    // ── Dropdown data ─────────────────────────────────────────────────────────
    static final String[] CRIME_TYPES = {
        "Select Type","Murder","Robbery","Assault","Burglary","Fraud","Cybercrime",
        "Drug Trafficking","Kidnapping","Domestic Violence","Sexual Assault","Vandalism",
        "Arson","Human Trafficking","Money Laundering","Terrorism","Extortion","Other"
    };
    static final String[] CRIME_STATUSES = {
        "Select Status","Open","Under Investigation","Closed","Solved","Unsolved","Cold Case"
    };
    static final String[] SEVERITIES = {
        "Select Severity","Critical","High","Medium","Low"
    };
    static final String[] SUSPECT_STATUSES = {
        "Select Status","Detained","Arrested","Released","Convicted","Acquitted","Fugitive","At Large","Deceased"
    };
    static final String[] GENDERS = { "Select Gender","Male","Female","Other" };
    static final String[] PRIOR_RECORDS = {
        "Select","No Prior Record","Minor Offenses","Repeat Offender","Violent Offender"
    };
    static final String[] OFFICER_RANKS = {
        "Select Rank","Constable","Head Constable","Assistant Sub-Inspector","Sub-Inspector",
        "Inspector","Deputy Superintendent","Superintendent","Deputy Inspector General",
        "Inspector General","Director General"
    };
    static final String[] OFFICER_STATUSES = { "Select Status","Active","On Leave","Suspended","Retired" };
    static final String[] INJURY_LEVELS = {
        "Select Injury Level","None","Minor","Moderate","Severe","Critical","Fatal"
    };
    static final String[] EVIDENCE_TYPES = {
        "Select Type","Weapon","Document","Digital","Biological","Chemical","Fingerprint",
        "Photograph","Video Footage","Witness Testimony","Clothing","Vehicle","Other"
    };
    static final String[] EVIDENCE_STATUSES = {
        "Select Status","Collected","In Lab","Stored","Destroyed","Transferred"
    };
    static final String[] LAB_STATUSES = {
        "Select Lab Status","Pending","In Analysis","Analyzed","Report Ready","N/A"
    };

    public MainFrame() {
        setTitle("Crime Record Management System — SNJB");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1050, 680));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ACCENT));

        JPanel hLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        hLeft.setOpaque(false);
        JLabel ico = new JLabel("🚔");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel title = new JLabel("Crime Record Management System");
        title.setFont(F_TITLE); title.setForeground(ACCENT);
        JLabel sub = new JLabel("  |  SNJB College of Engineering");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13)); sub.setForeground(TEXT_DIM);
        hLeft.add(ico); hLeft.add(title); hLeft.add(sub);

        JPanel hRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 16));
        hRight.setOpaque(false);
        JLabel dateLabel = new JLabel("📅  " + LocalDate.now());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(TEXT_DIM);
        hRight.add(dateLabel);
        header.add(hLeft, BorderLayout.WEST);
        header.add(hRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Tabs ──────────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(F_TAB);
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_MAIN);
        String[] tabTitles = {
            "  📊 Dashboard  ", "  🔴 Crimes  ", "  🕵️ Suspects  ",
            "  👮 Officers  ", "  🩺 Victims  ", "  🔬 Evidence  "
        };
        tabs.addTab(tabTitles[0], buildDashboard());
        tabs.addTab(tabTitles[1], buildCrimePanel());
        tabs.addTab(tabTitles[2], buildSuspectPanel());
        tabs.addTab(tabTitles[3], buildOfficerPanel());
        tabs.addTab(tabTitles[4], buildVictimPanel());
        tabs.addTab(tabTitles[5], buildEvidencePanel());

        for (int i = 0; i < tabs.getTabCount(); i++) {
            JLabel lbl = new JLabel(tabs.getTitleAt(i));
            lbl.setFont(F_TAB);
            lbl.setForeground(i == 0 ? ACCENT : TEXT_MAIN);
            lbl.setBorder(new EmptyBorder(7, 14, 7, 14));
            tabs.setTabComponentAt(i, lbl);
        }
        tabs.addChangeListener(e -> {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                JLabel lbl = (JLabel) tabs.getTabComponentAt(i);
                lbl.setForeground(i == tabs.getSelectedIndex() ? ACCENT : TEXT_MAIN);
            }
            if (tabs.getSelectedIndex() == 0) refreshDashboard();
        });
        add(tabs, BorderLayout.CENTER);

        // ── Status bar ────────────────────────────────────────────────────────
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 5));
        statusBar.setBackground(BG_CARD);
        JLabel conn = new JLabel("🟢  Connected to crime_management  •  MySQL");
        conn.setFont(new Font("Segoe UI", Font.PLAIN, 12)); conn.setForeground(GREEN);
        JLabel ver = new JLabel("v1.0  |  DBMSL Mini Project");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 12)); ver.setForeground(TEXT_DIM);
        statusBar.add(ver); statusBar.add(conn);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DASHBOARD
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildDashboard() {
        JPanel root = panel(new BorderLayout(0, 18));
        root.setBorder(new EmptyBorder(22, 22, 22, 22));

        // ── Stat cards row ────────────────────────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 6, 12, 0));
        statsRow.setOpaque(false);
        dCrimes   = new JLabel("0"); dOpen    = new JLabel("0");
        dSuspects = new JLabel("0"); dOfficers = new JLabel("0");
        dVictims  = new JLabel("0"); dEvidence = new JLabel("0");
        statsRow.add(statCard("Total Crimes",   dCrimes,   ACCENT,  "🔴"));
        statsRow.add(statCard("Open Cases",      dOpen,     GOLD,    "📂"));
        statsRow.add(statCard("Suspects",        dSuspects, ORANGE,  "🕵️"));
        statsRow.add(statCard("Officers",        dOfficers, BLUE,    "👮"));
        statsRow.add(statCard("Victims",         dVictims,  PURPLE,  "🩺"));
        statsRow.add(statCard("Evidence Items",  dEvidence, TEAL,    "🔬"));
        root.add(statsRow, BorderLayout.NORTH);

        // ── Center: recent crimes + crime type breakdown ───────────────────────
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        // Recent crimes card
        JPanel recentCard = card();
        recentCard.setLayout(new BorderLayout(0, 10));
        recentCard.add(sectionLabel("🔴  Recent Crime Reports"), BorderLayout.NORTH);
        DefaultTableModel recentModel = new DefaultTableModel(
            new String[]{"Title", "Type", "Location", "Status", "Severity", "Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable recentTable = styledTable(recentModel, 3);
        recentTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        recentCard.add(scrollPane(recentTable), BorderLayout.CENTER);

        // Crime breakdown card
        JPanel breakCard = card();
        breakCard.setLayout(new BorderLayout(0, 10));
        breakCard.add(sectionLabel("📊  Crime Type Breakdown"), BorderLayout.NORTH);
        DefaultTableModel breakModel = new DefaultTableModel(
            new String[]{"Crime Type", "Count"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable breakTable = styledTable(breakModel, -1);
        breakCard.add(scrollPane(breakTable), BorderLayout.CENTER);

        center.add(recentCard); center.add(breakCard);
        root.add(center, BorderLayout.CENTER);

        // ── Refresh button ─────────────────────────────────────────────────────
        JButton refreshBtn = btn("🔄  Refresh Dashboard", ACCENT);
        refreshBtn.addActionListener(e -> {
            refreshDashboard();
            recentModel.setRowCount(0);
            List<Object[]> all = dao.getAllCrimes();
            int max = Math.min(12, all.size());
            for (int i = 0; i < max; i++) {
                Object[] r = all.get(i);
                recentModel.addRow(new Object[]{ r[1], r[2], r[3], r[5], r[6], r[4] });
            }
            breakModel.setRowCount(0);
            dao.getCrimeTypeCounts().forEach((k, v) -> breakModel.addRow(new Object[]{ k, v }));
        });
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setOpaque(false); btnRow.add(refreshBtn);
        root.add(btnRow, BorderLayout.SOUTH);

        refreshBtn.doClick();
        return root;
    }

    void refreshDashboard() {
        dCrimes.setText(String.valueOf(dao.count("crimes")));
        dOpen.setText(String.valueOf(dao.countWhere("crimes", "status", "Open") +
                      dao.countWhere("crimes", "status", "Under Investigation")));
        dSuspects.setText(String.valueOf(dao.count("suspects")));
        dOfficers.setText(String.valueOf(dao.count("officers")));
        dVictims.setText(String.valueOf(dao.count("victims")));
        dEvidence.setText(String.valueOf(dao.count("evidence")));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CRIMES TAB
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildCrimePanel() {
        JPanel root = panel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel formCard = card();
        formCard.setLayout(new BorderLayout(0, 12));
        formCard.add(sectionLabel("🔴  Crime Record Details"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
        grid.setOpaque(false);
        cTitle    = addField(grid, "Crime Title");
        cType     = addCombo(grid, "Crime Type", CRIME_TYPES);
        cLocation = addField(grid, "Location");
        cDate     = addField(grid, "Crime Date (YYYY-MM-DD)");
        cSeverity = addCombo(grid, "Severity", SEVERITIES);
        cStatus   = addCombo(grid, "Status",   CRIME_STATUSES);
        cOfficer  = addCombo(grid, "Assigned Officer", dao.getOfficerOptions());
        cDesc     = addField(grid, "Description / Notes");
        cDate.setText(LocalDate.now().toString());
        formCard.add(grid, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        JButton bAdd  = btn("➕ Add Crime",       GREEN);
        JButton bLoad = btn("📂 Load Selected",   PURPLE);
        JButton bUpd  = btn("✏️ Update Selected", ORANGE);
        JButton bDel  = btn("🗑️ Delete Selected", ACCENT);
        JButton bClr  = btn("🧹 Clear",           new Color(50,60,90));
        JButton bRef  = btn("🔄 Reload Officers", BLUE);

        bAdd.addActionListener(e -> {
            String type = comboVal(cType), sev = comboVal(cSeverity), stat = comboVal(cStatus);
            if (cTitle.getText().trim().isEmpty()) { msg("Title is required."); return; }
            if (type==null||sev==null||stat==null) { msg("Select Type, Severity, Status."); return; }
            int offId = parseId((String) cOfficer.getSelectedItem());
            if (dao.addCrime(cTitle.getText().trim(), type, cDesc.getText().trim(),
                    cLocation.getText().trim(), cDate.getText().trim(), stat, sev, offId)) {
                msg("Crime record added!"); clearCrime(); loadCrimes();
            } else msg("Failed — check DB connection.");
        });
        bLoad.addActionListener(e -> {
            int row = cTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            cTitle.setText(str(cModel, row, 1));
            setCombo(cType,     str(cModel, row, 2));
            cLocation.setText(str(cModel, row, 3));
            cDate.setText(str(cModel, row, 4));
            setCombo(cStatus,   str(cModel, row, 5));
            setCombo(cSeverity, str(cModel, row, 6));
            cDesc.setText(str(cModel, row, 8));
        });
        bUpd.addActionListener(e -> {
            int row = cTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            String type = comboVal(cType), sev = comboVal(cSeverity), stat = comboVal(cStatus);
            if (type==null||sev==null||stat==null) { msg("Select Type, Severity, Status."); return; }
            int offId = parseId((String) cOfficer.getSelectedItem());
            if (dao.updateCrime((int)cModel.getValueAt(row,0), cTitle.getText().trim(), type,
                    cDesc.getText().trim(), cLocation.getText().trim(), cDate.getText().trim(),
                    stat, sev, offId)) {
                msg("Updated!"); clearCrime(); loadCrimes();
            }
        });
        bDel.addActionListener(e -> {
            int row = cTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this crime record? This will also delete linked suspects, victims, and evidence.", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
            if (dao.deleteCrime((int)cModel.getValueAt(row,0))) { msg("Deleted!"); loadCrimes(); }
        });
        bClr.addActionListener(e -> clearCrime());
        bRef.addActionListener(e -> {
            String[] opts = dao.getOfficerOptions();
            cOfficer.removeAllItems();
            for (String o : opts) cOfficer.addItem(o);
        });

        btns.add(bAdd); btns.add(bLoad); btns.add(bUpd);
        btns.add(bDel); btns.add(bClr); btns.add(bRef);
        formCard.add(btns, BorderLayout.SOUTH);
        root.add(formCard, BorderLayout.NORTH);

        cSearch = new JTextField(22);
        JPanel bottom = panel(new BorderLayout(0, 8));
        bottom.add(buildSearchRow(cSearch,
            e -> fillTable(cModel, dao.searchCrimes(cSearch.getText().trim())),
            e -> loadCrimes(),
            new String[]{"All","Open","Under Investigation","Closed","Solved"},
            new Color[]{ACCENT, GOLD, ORANGE, GREEN, TEAL},
            new Runnable[]{
                () -> loadCrimes(),
                () -> fillTable(cModel, dao.filterCrimesByStatus("Open")),
                () -> fillTable(cModel, dao.filterCrimesByStatus("Under Investigation")),
                () -> fillTable(cModel, dao.filterCrimesByStatus("Closed")),
                () -> fillTable(cModel, dao.filterCrimesByStatus("Solved")),
            }
        ), BorderLayout.NORTH);

        cModel = new DefaultTableModel(
            new String[]{"ID","Title","Type","Location","Date","Status","Severity","Officer","Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cTable = styledTable(cModel, 5);
        cTable.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());
        cTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        bottom.add(scrollPane(cTable), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.CENTER);
        loadCrimes();
        return root;
    }

    void clearCrime() {
        cTitle.setText(""); cLocation.setText(""); cDesc.setText("");
        cDate.setText(LocalDate.now().toString());
        cType.setSelectedIndex(0); cSeverity.setSelectedIndex(0);
        cStatus.setSelectedIndex(0); cOfficer.setSelectedIndex(0);
    }
    void loadCrimes() { fillTable(cModel, dao.getAllCrimes()); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SUSPECTS TAB
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildSuspectPanel() {
        JPanel root = panel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel formCard = card();
        formCard.setLayout(new BorderLayout(0, 12));
        formCard.add(sectionLabel("🕵️  Suspect Details"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 5, 12, 12));
        grid.setOpaque(false);
        sName        = addField(grid, "Full Name");
        sAge         = addField(grid, "Age");
        sGender      = addCombo(grid, "Gender",        GENDERS);
        sNationality = addField(grid, "Nationality");
        sPhone       = addField(grid, "Phone");
        sAddress     = addField(grid, "Address");
        sPrior       = addCombo(grid, "Prior Record",  PRIOR_RECORDS);
        sStatus      = addCombo(grid, "Status",        SUSPECT_STATUSES);
        sCrime       = addCombo(grid, "Linked Crime",  dao.getCrimeOptions());
        sNotes       = addField(grid, "Notes / Description");
        formCard.add(grid, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        JButton bAdd  = btn("➕ Add Suspect",      GREEN);
        JButton bLoad = btn("📂 Load Selected",    PURPLE);
        JButton bUpd  = btn("✏️ Update Selected",  ORANGE);
        JButton bDel  = btn("🗑️ Delete Selected",  ACCENT);
        JButton bClr  = btn("🧹 Clear",            new Color(50,60,90));
        JButton bRefC = btn("🔄 Reload Crimes",    BLUE);

        bAdd.addActionListener(e -> {
            if (sName.getText().trim().isEmpty()) { msg("Name is required."); return; }
            String gender = comboVal(sGender), prior = comboVal(sPrior), stat = comboVal(sStatus);
            if (stat == null) { msg("Select a Status."); return; }
            int cId = parseId((String) sCrime.getSelectedItem());
            if (dao.addSuspect(sName.getText().trim(), sAge.getText().trim(),
                    gender == null ? "" : gender, sNationality.getText().trim(),
                    sAddress.getText().trim(), sPhone.getText().trim(),
                    prior == null ? "" : prior, stat, sNotes.getText().trim(), cId)) {
                msg("Suspect added!"); clearSuspect(); loadSuspects();
            }
        });
        bLoad.addActionListener(e -> {
            int row = sTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            sName.setText(str(sModel, row, 1));        sAge.setText(str(sModel, row, 2));
            setCombo(sGender,  str(sModel, row, 3));   sNationality.setText(str(sModel, row, 4));
            sPhone.setText(str(sModel, row, 5));
            setCombo(sPrior,   str(sModel, row, 6));   setCombo(sStatus,   str(sModel, row, 7));
            sNotes.setText(str(sModel, row, 9));
        });
        bUpd.addActionListener(e -> {
            int row = sTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            String stat = comboVal(sStatus);
            if (stat == null) { msg("Select a Status."); return; }
            int cId = parseId((String) sCrime.getSelectedItem());
            String gender = comboVal(sGender), prior = comboVal(sPrior);
            if (dao.updateSuspect((int)sModel.getValueAt(row,0),
                    sName.getText().trim(), sAge.getText().trim(),
                    gender == null ? "" : gender, sNationality.getText().trim(),
                    sAddress.getText().trim(), sPhone.getText().trim(),
                    prior == null ? "" : prior, stat, sNotes.getText().trim(), cId)) {
                msg("Updated!"); clearSuspect(); loadSuspects();
            }
        });
        bDel.addActionListener(e -> {
            int row = sTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this suspect?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
            if (dao.deleteSuspect((int)sModel.getValueAt(row,0))) { msg("Deleted!"); loadSuspects(); }
        });
        bClr.addActionListener(e -> clearSuspect());
        bRefC.addActionListener(e -> {
            String[] opts = dao.getCrimeOptions();
            sCrime.removeAllItems(); for (String o : opts) sCrime.addItem(o);
        });
        btns.add(bAdd); btns.add(bLoad); btns.add(bUpd);
        btns.add(bDel); btns.add(bClr); btns.add(bRefC);
        formCard.add(btns, BorderLayout.SOUTH);
        root.add(formCard, BorderLayout.NORTH);

        sSearch = new JTextField(22);
        JPanel bottom = panel(new BorderLayout(0, 8));
        bottom.add(buildSimpleSearch(sSearch,
            e -> fillTable(sModel, dao.searchSuspects(sSearch.getText().trim())),
            e -> loadSuspects()), BorderLayout.NORTH);

        sModel = new DefaultTableModel(
            new String[]{"ID","Name","Age","Gender","Nationality","Phone","Prior Record","Status","Linked Crime","Notes"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        sTable = styledTable(sModel, 7);
        sTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        bottom.add(scrollPane(sTable), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.CENTER);
        loadSuspects();
        return root;
    }

    void clearSuspect() {
        sName.setText(""); sAge.setText(""); sPhone.setText(""); sAddress.setText("");
        sNationality.setText(""); sNotes.setText("");
        sGender.setSelectedIndex(0); sPrior.setSelectedIndex(0);
        sStatus.setSelectedIndex(0); sCrime.setSelectedIndex(0);
    }
    void loadSuspects() { fillTable(sModel, dao.getAllSuspects()); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  OFFICERS TAB
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildOfficerPanel() {
        JPanel root = panel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel formCard = card();
        formCard.setLayout(new BorderLayout(0, 12));
        formCard.add(sectionLabel("👮  Officer Details"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
        grid.setOpaque(false);
        oName  = addField(grid, "Full Name");
        oBadge = addField(grid, "Badge Number");
        oRank  = addCombo(grid, "Rank",       OFFICER_RANKS);
        oDept  = addField(grid, "Department");
        oPhone = addField(grid, "Phone");
        oEmail = addField(grid, "Email");
        oStatus = addCombo(grid, "Status",    OFFICER_STATUSES);
        grid.add(new JLabel()); // placeholder
        formCard.add(grid, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        JButton bAdd  = btn("➕ Add Officer",      GREEN);
        JButton bLoad = btn("📂 Load Selected",    PURPLE);
        JButton bUpd  = btn("✏️ Update Selected",  ORANGE);
        JButton bDel  = btn("🗑️ Delete Selected",  ACCENT);
        JButton bClr  = btn("🧹 Clear",            new Color(50,60,90));

        bAdd.addActionListener(e -> {
            if (oName.getText().trim().isEmpty() || oBadge.getText().trim().isEmpty()) {
                msg("Name and Badge Number are required."); return;
            }
            String rank = comboVal(oRank), stat = comboVal(oStatus);
            if (rank == null || stat == null) { msg("Select Rank and Status."); return; }
            if (dao.addOfficer(oName.getText().trim(), oBadge.getText().trim(), rank,
                    oDept.getText().trim(), oPhone.getText().trim(),
                    oEmail.getText().trim(), stat)) {
                msg("Officer added!"); clearOfficer(); loadOfficers();
            }
        });
        bLoad.addActionListener(e -> {
            int row = oTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            oName.setText(str(oModel, row, 1));   oBadge.setText(str(oModel, row, 2));
            setCombo(oRank, str(oModel, row, 3)); oDept.setText(str(oModel, row, 4));
            oPhone.setText(str(oModel, row, 5));  oEmail.setText(str(oModel, row, 6));
            setCombo(oStatus, str(oModel, row, 7));
        });
        bUpd.addActionListener(e -> {
            int row = oTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            String rank = comboVal(oRank), stat = comboVal(oStatus);
            if (rank == null || stat == null) { msg("Select Rank and Status."); return; }
            if (dao.updateOfficer((int)oModel.getValueAt(row,0),
                    oName.getText().trim(), oBadge.getText().trim(), rank,
                    oDept.getText().trim(), oPhone.getText().trim(),
                    oEmail.getText().trim(), stat)) {
                msg("Updated!"); clearOfficer(); loadOfficers();
            }
        });
        bDel.addActionListener(e -> {
            int row = oTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this officer record?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
            if (dao.deleteOfficer((int)oModel.getValueAt(row,0))) { msg("Deleted!"); loadOfficers(); }
        });
        bClr.addActionListener(e -> clearOfficer());
        btns.add(bAdd); btns.add(bLoad); btns.add(bUpd); btns.add(bDel); btns.add(bClr);
        formCard.add(btns, BorderLayout.SOUTH);
        root.add(formCard, BorderLayout.NORTH);

        oSearch = new JTextField(22);
        JPanel bottom = panel(new BorderLayout(0, 8));
        bottom.add(buildSimpleSearch(oSearch,
            e -> fillTable(oModel, dao.searchOfficers(oSearch.getText().trim())),
            e -> loadOfficers()), BorderLayout.NORTH);

        oModel = new DefaultTableModel(
            new String[]{"ID","Name","Badge No","Rank","Department","Phone","Email","Status","Cases Assigned"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        oTable = styledTable(oModel, 7);
        oTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        bottom.add(scrollPane(oTable), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.CENTER);
        loadOfficers();
        return root;
    }

    void clearOfficer() {
        oName.setText(""); oBadge.setText(""); oDept.setText("");
        oPhone.setText(""); oEmail.setText("");
        oRank.setSelectedIndex(0); oStatus.setSelectedIndex(0);
    }
    void loadOfficers() { fillTable(oModel, dao.getAllOfficers()); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  VICTIMS TAB
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildVictimPanel() {
        JPanel root = panel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel formCard = card();
        formCard.setLayout(new BorderLayout(0, 12));
        formCard.add(sectionLabel("🩺  Victim Details"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
        grid.setOpaque(false);
        vName      = addField(grid, "Full Name");
        vAge       = addField(grid, "Age");
        vGender    = addCombo(grid, "Gender",       GENDERS);
        vContact   = addField(grid, "Contact Number");
        vAddress   = addField(grid, "Address");
        vInjury    = addCombo(grid, "Injury Level", INJURY_LEVELS);
        vCrime     = addCombo(grid, "Linked Crime", dao.getCrimeOptions());
        vStatement = addField(grid, "Statement / Notes");
        formCard.add(grid, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        JButton bAdd  = btn("➕ Add Victim",       GREEN);
        JButton bLoad = btn("📂 Load Selected",    PURPLE);
        JButton bUpd  = btn("✏️ Update Selected",  ORANGE);
        JButton bDel  = btn("🗑️ Delete Selected",  ACCENT);
        JButton bClr  = btn("🧹 Clear",            new Color(50,60,90));
        JButton bRefC = btn("🔄 Reload Crimes",    BLUE);

        bAdd.addActionListener(e -> {
            if (vName.getText().trim().isEmpty()) { msg("Name is required."); return; }
            String inj = comboVal(vInjury);
            if (inj == null) { msg("Select Injury Level."); return; }
            int cId = parseId((String) vCrime.getSelectedItem());
            String gender = comboVal(vGender);
            if (dao.addVictim(vName.getText().trim(), vAge.getText().trim(),
                    gender == null ? "" : gender, vContact.getText().trim(),
                    vAddress.getText().trim(), inj, vStatement.getText().trim(), cId)) {
                msg("Victim record added!"); clearVictim(); loadVictims();
            }
        });
        bLoad.addActionListener(e -> {
            int row = vTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            vName.setText(str(vModel, row, 1));      vAge.setText(str(vModel, row, 2));
            setCombo(vGender, str(vModel, row, 3));  vContact.setText(str(vModel, row, 4));
            setCombo(vInjury, str(vModel, row, 5));  vStatement.setText(str(vModel, row, 7));
        });
        bUpd.addActionListener(e -> {
            int row = vTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            String inj = comboVal(vInjury);
            if (inj == null) { msg("Select Injury Level."); return; }
            int cId = parseId((String) vCrime.getSelectedItem());
            String gender = comboVal(vGender);
            if (dao.updateVictim((int)vModel.getValueAt(row,0),
                    vName.getText().trim(), vAge.getText().trim(),
                    gender == null ? "" : gender, vContact.getText().trim(),
                    vAddress.getText().trim(), inj, vStatement.getText().trim(), cId)) {
                msg("Updated!"); clearVictim(); loadVictims();
            }
        });
        bDel.addActionListener(e -> {
            int row = vTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this victim record?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
            if (dao.deleteVictim((int)vModel.getValueAt(row,0))) { msg("Deleted!"); loadVictims(); }
        });
        bClr.addActionListener(e -> clearVictim());
        bRefC.addActionListener(e -> {
            String[] opts = dao.getCrimeOptions();
            vCrime.removeAllItems(); for (String o : opts) vCrime.addItem(o);
        });
        btns.add(bAdd); btns.add(bLoad); btns.add(bUpd);
        btns.add(bDel); btns.add(bClr); btns.add(bRefC);
        formCard.add(btns, BorderLayout.SOUTH);
        root.add(formCard, BorderLayout.NORTH);

        vSearch = new JTextField(22);
        JPanel bottom = panel(new BorderLayout(0, 8));
        bottom.add(buildSimpleSearch(vSearch,
            e -> fillTable(vModel, dao.searchVictims(vSearch.getText().trim())),
            e -> loadVictims()), BorderLayout.NORTH);

        vModel = new DefaultTableModel(
            new String[]{"ID","Name","Age","Gender","Contact","Injury Level","Linked Crime","Statement"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        vTable = styledTable(vModel, 5);
        vTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        bottom.add(scrollPane(vTable), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.CENTER);
        loadVictims();
        return root;
    }

    void clearVictim() {
        vName.setText(""); vAge.setText(""); vContact.setText(""); vAddress.setText("");
        vStatement.setText("");
        vGender.setSelectedIndex(0); vInjury.setSelectedIndex(0); vCrime.setSelectedIndex(0);
    }
    void loadVictims() { fillTable(vModel, dao.getAllVictims()); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  EVIDENCE TAB
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildEvidencePanel() {
        JPanel root = panel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel formCard = card();
        formCard.setLayout(new BorderLayout(0, 12));
        formCard.add(sectionLabel("🔬  Evidence Details"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
        grid.setOpaque(false);
        eType        = addCombo(grid, "Evidence Type",   EVIDENCE_TYPES);
        eDesc        = addField(grid, "Description");
        eCollectedBy = addField(grid, "Collected By (Officer)");
        eDate        = addField(grid, "Collection Date (YYYY-MM-DD)");
        eLocation    = addField(grid, "Location Found");
        eStatus      = addCombo(grid, "Custody Status",  EVIDENCE_STATUSES);
        eLabStatus   = addCombo(grid, "Lab Status",      LAB_STATUSES);
        eCrime       = addCombo(grid, "Linked Crime",    dao.getCrimeOptions());
        eDate.setText(LocalDate.now().toString());
        formCard.add(grid, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        JButton bAdd  = btn("➕ Add Evidence",     GREEN);
        JButton bLoad = btn("📂 Load Selected",    PURPLE);
        JButton bUpd  = btn("✏️ Update Selected",  ORANGE);
        JButton bDel  = btn("🗑️ Delete Selected",  ACCENT);
        JButton bClr  = btn("🧹 Clear",            new Color(50,60,90));
        JButton bRefC = btn("🔄 Reload Crimes",    BLUE);

        bAdd.addActionListener(e -> {
            String type = comboVal(eType), stat = comboVal(eStatus), lab = comboVal(eLabStatus);
            if (type == null) { msg("Select Evidence Type."); return; }
            if (stat == null) { msg("Select Custody Status."); return; }
            if (lab  == null) { msg("Select Lab Status."); return; }
            int cId = parseId((String) eCrime.getSelectedItem());
            if (dao.addEvidence(type, eDesc.getText().trim(), eCollectedBy.getText().trim(),
                    eDate.getText().trim(), eLocation.getText().trim(), stat, lab, cId)) {
                msg("Evidence added!"); clearEvidence(); loadEvidence();
            }
        });
        bLoad.addActionListener(e -> {
            int row = eTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            setCombo(eType,   str(eModel, row, 1));
            eDesc.setText(str(eModel, row, 2));
            eCollectedBy.setText(str(eModel, row, 3));
            eDate.setText(str(eModel, row, 4));
            eLocation.setText(str(eModel, row, 5));
            setCombo(eStatus,    str(eModel, row, 6));
            setCombo(eLabStatus, str(eModel, row, 7));
        });
        bUpd.addActionListener(e -> {
            int row = eTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            String type = comboVal(eType), stat = comboVal(eStatus), lab = comboVal(eLabStatus);
            if (type==null||stat==null||lab==null) { msg("Select Type, Status, Lab Status."); return; }
            int cId = parseId((String) eCrime.getSelectedItem());
            if (dao.updateEvidence((int)eModel.getValueAt(row,0), type, eDesc.getText().trim(),
                    eCollectedBy.getText().trim(), eDate.getText().trim(),
                    eLocation.getText().trim(), stat, lab, cId)) {
                msg("Updated!"); clearEvidence(); loadEvidence();
            }
        });
        bDel.addActionListener(e -> {
            int row = eTable.getSelectedRow();
            if (row < 0) { msg("Select a row."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this evidence record?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
            if (dao.deleteEvidence((int)eModel.getValueAt(row,0))) { msg("Deleted!"); loadEvidence(); }
        });
        bClr.addActionListener(e -> clearEvidence());
        bRefC.addActionListener(e -> {
            String[] opts = dao.getCrimeOptions();
            eCrime.removeAllItems(); for (String o : opts) eCrime.addItem(o);
        });
        btns.add(bAdd); btns.add(bLoad); btns.add(bUpd);
        btns.add(bDel); btns.add(bClr); btns.add(bRefC);
        formCard.add(btns, BorderLayout.SOUTH);
        root.add(formCard, BorderLayout.NORTH);

        eSearch = new JTextField(22);
        JPanel bottom = panel(new BorderLayout(0, 8));
        bottom.add(buildSimpleSearch(eSearch,
            e -> fillTable(eModel, dao.searchEvidence(eSearch.getText().trim())),
            e -> loadEvidence()), BorderLayout.NORTH);

        eModel = new DefaultTableModel(
            new String[]{"ID","Type","Description","Collected By","Date","Location Found","Status","Lab Status","Linked Crime"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        eTable = styledTable(eModel, 6);
        eTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        bottom.add(scrollPane(eTable), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.CENTER);
        loadEvidence();
        return root;
    }

    void clearEvidence() {
        eDesc.setText(""); eCollectedBy.setText(""); eLocation.setText("");
        eDate.setText(LocalDate.now().toString());
        eType.setSelectedIndex(0); eStatus.setSelectedIndex(0);
        eLabStatus.setSelectedIndex(0); eCrime.setSelectedIndex(0);
    }
    void loadEvidence() { fillTable(eModel, dao.getAllEvidence()); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SHARED HELPERS
    // ═══════════════════════════════════════════════════════════════════════════
    JPanel buildSearchRow(JTextField field, ActionListener onSearch, ActionListener onRefresh,
                           String[] filterLabels, Color[] filterColors, Runnable[] filterActions) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        p.setBackground(BG_DARK);
        styleSearchField(field);
        JLabel lbl = new JLabel("Search:"); lbl.setFont(F_LABEL); lbl.setForeground(TEXT_DIM);
        JButton srch = btn("🔍 Search", ACCENT);
        JButton ref  = smallBtn("🔄 Refresh", new Color(50,60,90));
        srch.addActionListener(onSearch);
        ref.addActionListener(onRefresh);
        p.add(lbl); p.add(field); p.add(srch); p.add(ref);
        JLabel flbl = new JLabel("   Filter:");
        flbl.setFont(F_LABEL); flbl.setForeground(TEXT_DIM);
        p.add(flbl);
        for (int i = 0; i < filterLabels.length; i++) {
            final int fi = i;
            JButton fb = smallBtn(filterLabels[i], filterColors[i]);
            fb.addActionListener(e -> filterActions[fi].run());
            p.add(fb);
        }
        return p;
    }

    JPanel buildSimpleSearch(JTextField field, ActionListener onSearch, ActionListener onRefresh) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        p.setBackground(BG_DARK);
        styleSearchField(field);
        JLabel lbl = new JLabel("Search:"); lbl.setFont(F_LABEL); lbl.setForeground(TEXT_DIM);
        JButton srch = btn("🔍 Search", ACCENT);
        JButton ref  = smallBtn("🔄 Refresh", new Color(50,60,90));
        srch.addActionListener(onSearch);
        ref.addActionListener(onRefresh);
        p.add(lbl); p.add(field); p.add(srch); p.add(ref);
        return p;
    }

    void fillTable(DefaultTableModel model, List<Object[]> rows) {
        model.setRowCount(0);
        for (Object[] r : rows) model.addRow(r);
    }

    void msg(String m) {
        JOptionPane.showMessageDialog(this, m, "Crime Management System", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
