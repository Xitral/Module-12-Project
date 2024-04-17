import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StatusPanel {
    private JPanel statusPanel;
    private Map<String, JProgressBar> statusBars;
    private JLabel petNameLabel;
    private JLabel petAgeLabel;

    public StatusPanel(Pet pet) {
        this.statusPanel = new JPanel();
        this.statusBars = new HashMap<>();

        String[] statuses = {"Health", "Happiness", "Hunger", "Thirst", "Energy", "Cleanliness"};
        this.statusPanel.setLayout(new GridLayout(3, statuses.length + 1));
        this.statusPanel.setBackground(Color.DARK_GRAY);
        this.statusPanel.setPreferredSize(new Dimension(200, 50));

        for (String status : statuses) {
            JLabel label = new JLabel(status);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            this.statusPanel.add(label);

            JProgressBar progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
            progressBar.setStringPainted(false);
            progressBar.setBorderPainted(false);

            progressBar.addChangeListener(e -> {
                JProgressBar source = (JProgressBar) e.getSource();
                int percentage = source.getValue();

                float hue = 0.35f * (percentage / 100f);
                Color color = Color.getHSBColor(hue, 1f, 1f);
                source.setForeground(color);
            });

            this.statusBars.put(status, progressBar);
            this.statusPanel.add(progressBar);
        }
    }

    public JPanel getStatusPanel() {
        return this.statusPanel;
    }

    public void adjustStatus(String status, int value) {
        JProgressBar progressBar = this.statusBars.get(status);
        if (progressBar != null) {
            progressBar.setValue(value);
        }
    }

    public void updateStatus(Pet pet) {
        adjustStatus("Health", pet.getHealth());
        adjustStatus("Happiness", pet.getHappiness());
        adjustStatus("Hunger", pet.getHunger());
        adjustStatus("Thirst", pet.getThirst());
        adjustStatus("Energy", pet.getEnergy());
        adjustStatus("Cleanliness", pet.getCleanliness());
    }

    public void clearStatus() {
        for (String status : this.statusBars.keySet()) {
            this.adjustStatus(status, 0);
        }
    }
}