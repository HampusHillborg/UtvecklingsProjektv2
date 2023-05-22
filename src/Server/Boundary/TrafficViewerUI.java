package Server.Boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class TrafficViewerUI extends JFrame {
    private JTextArea logTextArea;
    private JButton viewButton;
    private JTextField startTimeField;
    private JTextField endTimeField;

    public TrafficViewerUI() {
        super("Message Traffic Viewer");
        initializeComponents();
        setupListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);

        JLabel startTimeLabel = new JLabel("Start Time:");
        JLabel endTimeLabel = new JLabel("End Time:");
        startTimeField = new JTextField(15);
        endTimeField = new JTextField(15);
        viewButton = new JButton("View");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeField);
        inputPanel.add(endTimeLabel);
        inputPanel.add(endTimeField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startTimeStr = startTimeField.getText();
                String endTimeStr = endTimeField.getText();

                // Perform data retrieval based on start and end times
                Date startTime = parseDateTime(startTimeStr);
                Date endTime = parseDateTime(endTimeStr);
                String trafficLog = retrieveTrafficLog(startTime, endTime);

                logTextArea.setText(trafficLog);
            }
        });
    }

    private Date parseDateTime(String dateTimeStr) {
        // Implement your own parsing logic here
        // Convert the date/time string to a Date object
        // Example: SimpleDateFormat or DateTimeFormatter

        return null; // Replace with the parsed Date object
    }

    private String retrieveTrafficLog(Date startTime, Date endTime) {
        // Implement your own logic to retrieve traffic log
        // based on the given start and end times
        // Example: Access logs or stored log files

        return "Traffic log between " + startTime + " and " + endTime;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TrafficViewerUI();
            }
        });
    }
}
