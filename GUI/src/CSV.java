import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSV {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chọn Tệp Dữ Liệu CSV");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Điều chỉnh kích thước cửa sổ
        frame.setLocationRelativeTo(null);  // Center the frame

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        frame.add(panel);

        // Nút chọn tệp
        JButton chooseFileButton = new JButton("Chọn Tệp CSV");
        chooseFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(chooseFileButton, BorderLayout.NORTH);

        // JLabel để hiển thị đường dẫn tệp đã chọn
        JLabel filePathLabel = new JLabel("Chưa chọn tệp nào.");
        filePathLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(filePathLabel, BorderLayout.CENTER);

        // JTable để hiển thị dữ liệu từ tệp CSV
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(173, 216, 230));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Action Listener cho nút chọn tệp
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tạo JFileChooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn Tệp CSV");
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // Lấy tệp đã chọn
                    File selectedFile = fileChooser.getSelectedFile();
                    String selectedFilePath = selectedFile.getAbsolutePath();
                    filePathLabel.setText("Tệp đã chọn: " + selectedFilePath);

                    // Đọc dữ liệu từ tệp CSV và hiển thị
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        String line;
                        List<String[]> data = new ArrayList<>();
                        String[] columnNames = null;

                        // Đọc dòng đầu tiên để lấy tên cột
                        if ((line = br.readLine()) != null) {
                            columnNames = line.split(","); // Tách bằng dấu phẩy
                        }

                        // Đọc các dòng còn lại
                        while ((line = br.readLine()) != null) {
                            data.add(line.split(",")); // Tách bằng dấu phẩy
                        }

                        // Chuyển đổi danh sách dữ liệu thành mảng 2 chiều
                        String[][] rowData = data.toArray(new String[0][]);

                        // Tạo TableModel và đặt cho JTable
                        table.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi đọc tệp: " + ex.getMessage());
                    }
                } else {
                    filePathLabel.setText("Chưa chọn tệp nào.");
                }
            }
        });

        frame.setVisible(true);
    }
}