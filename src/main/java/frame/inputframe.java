package frame;

import helpers.koneksi;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

public class inputframe extends JFrame {
    private JTextField idtextField;
    private JTextField namatextField;
    private JTextField npmtextField;
    private JTextField ttltextField;
    private JTextField jktextField;
    private JTextField emailtextField;
    private JTextField notlptextField;
    private JTextField alamattextField;
    private JTextField statustextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel mainPanel;
    private JLabel imageLabel;
    private JButton pilihGambarButton;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    JFileChooser fChooser = new JFileChooser();
    private BufferedImage resizeImage(BufferedImage originalImage, int type){
        int IMG_HEIGHT = 100;
        int IMG_WIDTH = 100;
        BufferedImage resizeImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizeImage.createGraphics();
        g.drawImage(originalImage, 100, 100, IMG_WIDTH, IMG_HEIGHT, null);
        dispose();
        return resizeImage;
    }

    public inputframe() {
        pilihGambarButton.addActionListener(e -> {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
            fChooser.setFileFilter(filter);
            int result = fChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION){
                File file = fChooser.getSelectedFile();
                imageLabel.setIcon(new ImageIcon(String.valueOf(file)));
            }

            File selectedFile = fChooser.getSelectedFile();
            String filename = selectedFile.getName();
            System.out.println(""+filename);

            if (filename.endsWith(".jpg")||filename.endsWith(".JPG")||filename.endsWith(".PNG")||filename.endsWith(".png")){
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = selectedFile.getAbsolutePath();
                    ImageIcon myImage = new ImageIcon(path);

                    Image img = myImage.getImage();
                    Image newImage = img.getScaledInstance(imageLabel.getWidth(),imageLabel.getHeight(), Image.SCALE_SMOOTH);

                    ImageIcon image = new ImageIcon(newImage);
                    imageLabel.setIcon(image);

                    simpanButton.addActionListener(e1 -> {
                        String nama = namatextField.getText();
                        String npm = npmtextField.getText();
                        String tempat_tanggal_lahir = ttltextField.getText();
                        String jenis_kelamin = jktextField.getText();
                        String email = emailtextField.getText();
                        String nomor_telepon = notlptextField.getText();
                        String alamat = alamattextField.getText();
                        String status = statustextField.getText();
                        FileInputStream fis = null;
                        Connection c = koneksi.getConnection();
                        PreparedStatement ps;
                        try {
                            if (id == 0) {
                                fis = new FileInputStream(path);
                                String insertSQL = "INSERT INTO data_mahasiswa VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                ps = c.prepareStatement(insertSQL);
                                ps.setString(1, nama);
                                ps.setString(2, npm);
                                ps.setString(3, tempat_tanggal_lahir);
                                ps.setString(4, jenis_kelamin);
                                ps.setString(5, email);
                                ps.setString(6, nomor_telepon);
                                ps.setString(7, alamat);
                                ps.setString(8, status);
                                ps.setBinaryStream(9, fis);
                                ps.executeUpdate();
                                dispose();
                            } else {
                                String updateSQL = "UPDATE data_mahasiswa SET nama = ?, npm = ?, tempat_tanggal_lahir = ?, jenis_kelamin = ?, email = ?, nomor_telepon = ?, alamat = ?, status =?, foto_anggota = ? WHERE id = ? ";
                                ps = c.prepareStatement(updateSQL);
                                ps.setString(1, nama);
                                ps.setString(2, npm);
                                ps.setString(3, tempat_tanggal_lahir);
                                ps.setString(4, jenis_kelamin);
                                ps.setString(5, email);
                                ps.setString(6, nomor_telepon);
                                ps.setString(7, alamat);
                                ps.setString(8, status);
                                ps.setBinaryStream(9, fis);
                                ps.setInt(10,id);
                                ps.executeUpdate();
                                dispose();
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                }
            }


        });

        batalButton.addActionListener(e -> {
            dispose();
        });


        init();
    }

    public void isikomponen() {
        Connection c = koneksi.getConnection();
        String findSQL = "SELECT * FROM data_mahasiswa WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idtextField.setText(String.valueOf(rs.getInt("id")));
                namatextField.setText(rs.getString("nama"));
                npmtextField.setText(rs.getString("npm"));
                ttltextField.setText(rs.getString("tempat_tanggal_lahir"));
                jktextField.setText(rs.getString("jenis_kelamin"));
                emailtextField.setText(rs.getString("email"));
                notlptextField.setText(rs.getString("nomor_telepon"));
                alamattextField.setText(rs.getString("alamat"));
                statustextField.setText(rs.getString("status"));
                imageLabel.setIcon(new ImageIcon(getBufferedImage(rs.getBlob("foto_anggota"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getBufferedImage(Blob imageBlob){
        InputStream binaryStream = null;
        BufferedImage b = null;
        try {
            binaryStream = imageBlob.getBinaryStream();
            b = ImageIO.read(binaryStream);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Data Mahasiswa");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
