import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class project1 extends JFrame implements ActionListener {

    // Form fields
    private JTextField tDate, tAmount, tNote;
    private JComboBox<String> cCategory;
    private JButton btnAdd;

    // List view components
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnSearch, btnClearSearch;
    private JTextField tSearch;

    // In-memory list for current session
    private final java.util.List<Expense> expenses = new ArrayList<>();

    // Color scheme
    private final Color BACKGROUND_BROWN = new Color(139, 90, 43);
    private final Color LIGHT_BROWN = new Color(205, 163, 118);
    private final Color CREAM = new Color(245, 235, 220);
    private final Color DARK_BROWN = new Color(101, 67, 33);
    private final Color TEXT_COLOR = new Color(45, 30, 15);

    public project1() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.setBackground(LIGHT_BROWN);
        tabs.addTab("Add Expense", buildAddPanel());
        tabs.addTab("Expense List", buildListPanel());

        setContentPane(tabs);
        setVisible(true);
    }

    
    private JPanel buildAddPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(CREAM);

        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(0, 0, 750, 80);
        titlePanel.setBackground(BACKGROUND_BROWN);
        titlePanel.setLayout(null);

        JLabel titleLabel = new JLabel("Add New Expense");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(CREAM);
        titleLabel.setBounds(250, 20, 300, 40);
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(100, 100, 550, 300);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BROWN, 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);

        JLabel l1 = new JLabel("Date (YYYY-MM-DD):");
        JLabel l2 = new JLabel("Category:");
        JLabel l3 = new JLabel("Amount (₹):");
        JLabel l4 = new JLabel("Notes:");

        l1.setFont(labelFont);
        l2.setFont(labelFont);
        l3.setFont(labelFont);
        l4.setFont(labelFont);

        l1.setForeground(TEXT_COLOR);
        l2.setForeground(TEXT_COLOR);
        l3.setForeground(TEXT_COLOR);
        l4.setForeground(TEXT_COLOR);

        tDate = new JTextField();
        tDate.setFont(fieldFont);
        

        String[] categories = {"Food", "Rent", "Self", "Clothes", "Grocery", 
                              "Stationary", "Fruits", "Coffee", "Outing", "Others"};
        cCategory = new JComboBox<>(categories);
        cCategory.setFont(fieldFont);
        cCategory.setBackground(Color.WHITE);
        cCategory.setBorder(BorderFactory.createLineBorder(LIGHT_BROWN, 1));

        tAmount = new JTextField();
        tAmount.setFont(fieldFont);
        

        tNote = new JTextField();
        tNote.setFont(fieldFont);
        tNote.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_BROWN, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        btnAdd = new JButton("Add Expense");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.setBackground(BACKGROUND_BROWN);
        btnAdd.setForeground(CREAM);
        btnAdd.setFocusPainted(false);
        
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        l1.setBounds(30, 20, 180, 30);
        tDate.setBounds(220, 20, 270, 35);
        l2.setBounds(30, 70, 180, 30);
        cCategory.setBounds(220, 70, 270, 35);
        l3.setBounds(30, 120, 180, 30);
        tAmount.setBounds(220, 120, 270, 35);
        l4.setBounds(30, 170, 180, 30);
        tNote.setBounds(220, 170, 270, 35);
        btnAdd.setBounds(180, 230, 200, 45);

        formPanel.add(l1); formPanel.add(tDate);
        formPanel.add(l2); formPanel.add(cCategory);
        formPanel.add(l3); formPanel.add(tAmount);
        formPanel.add(l4); formPanel.add(tNote);
        formPanel.add(btnAdd);

        p.add(titlePanel);
        p.add(formPanel);

        btnAdd.addActionListener(this);

        return p;
    }


    private JPanel buildListPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(CREAM);

        tableModel = new DefaultTableModel(new String[]{"Date", "Category", "Amount", "Notes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(LIGHT_BROWN);
        table.getTableHeader().setForeground(TEXT_COLOR);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 70, 690, 400);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(20, 25, 100, 32);
        styleButton(btnRefresh);

        tSearch = new JTextField();
        tSearch.setBounds(140, 25, 220, 32);
        tSearch.setToolTipText("Search date/category/notes");
        tSearch.setFont(new Font("Arial", Font.PLAIN, 13));

        btnSearch = new JButton("Search");
        btnSearch.setBounds(370, 25, 100, 32);
        styleButton(btnSearch);

        btnClearSearch = new JButton("Clear");
        btnClearSearch.setBounds(480, 25, 100, 32);
        styleButton(btnClearSearch);

        p.add(btnRefresh);
        p.add(tSearch);
        p.add(btnSearch);
        p.add(btnClearSearch);
        p.add(sp);

        // Load on open
        loadCsvToTable(null);

        // Actions
        btnRefresh.addActionListener(ev -> loadCsvToTable(null));
        btnSearch.addActionListener(ev -> {
            String q = tSearch.getText().trim();
            loadCsvToTable(q.isEmpty() ? null : q.toLowerCase());
        });
        btnClearSearch.addActionListener(ev -> {
            tSearch.setText("");
            loadCsvToTable(null);
        });

        return p;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(LIGHT_BROWN);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Add button handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addExpense();
        }
    }

    private void addExpense() {
        try {
            String date = tDate.getText().trim();
            String category = (String) cCategory.getSelectedItem();
            String amountText = tAmount.getText().trim();
            String notes = tNote.getText().trim();

            if (date.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Date and Amount are required.");
                return;
            }
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date must be in format YYYY-MM-DD.");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
                return;
            }
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            Expense exp = new Expense(date, category, amount, notes);
            expenses.add(exp);
            appendToCsv(exp);
            JOptionPane.showMessageDialog(this, "Expense Added Successfully!");

            // Clear fields
            tDate.setText("");
            cCategory.setSelectedIndex(0);
            tAmount.setText("");
            tNote.setText("");

            // Refresh table immediately
            loadCsvToTable(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void appendToCsv(Expense exp) {
        File file = new File("expenses.csv");
        boolean writeHeader = !file.exists();

        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            if (writeHeader) out.println("date,category,amount,notes");
            out.println(String.join(",",
                    exp.date,
                    exp.category.replace(",", " "),
                    String.valueOf(exp.amount),
                    exp.notes.replace(",", " ")
            ));
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Error writing to CSV: " + ioe.getMessage());
        }
    }

    private void loadCsvToTable(String queryLower) {
        tableModel.setRowCount(0);
        File file = new File("expenses.csv");
        if (!file.exists()) return;

        java.util.List<String[]> allRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                String[] cols = line.split(",", -1);
                if (cols.length < 4) continue;

                String date = cols[0].trim();
                String category = cols[1].trim();
                String amount = cols[2].trim();
                String notes = cols[3].trim();

                if (queryLower != null) {
                    String hay = (date + " " + category + " " + notes).toLowerCase();
                    if (!hay.contains(queryLower)) continue;
                }

                allRows.add(new String[]{date, category, amount, notes});
            }

            // Show latest entries first
            Collections.reverse(allRows);
            for (String[] r : allRows) tableModel.addRow(r);

        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Error reading CSV: " + ioe.getMessage());
        }
    }

    static class Expense {
        final String date;
        final String category;
        final double amount;
        final String notes;
        Expense(String date, String category, double amount, String notes) {
            this.date = date; this.category = category; this.amount = amount; this.notes = notes;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(project1::new);
    }
}
