package frame;

import com.mysql.cj.protocol.Resultset;
import helpers.koneksi;
import org.bouncycastle.jcajce.provider.drbg.DRBG;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DataMahasiswaFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPanel;
    private JPanel buttonPanel;
    private JTextField textField1;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton tutupButton;

    private void init() {
        setContentPane(mainPanel);
        setTitle("Data Mahasiswa");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable() {
        Connection c = koneksi.getConnection();
        String selectSQL = "SELECT * FROM data_mahasiswa";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"ID", "Nama", "NPM", "Tempat Tanggal Lahir", "Jenis Kelamin", "Email", "Nomor Telepon", "Alamat", "Status", "Foto Anggota"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0) {
                public Class getColumnClass(int colomn) {
                    return getValueAt(0, colomn).getClass();
                }
            };
            viewTable.setModel(dtm);
            viewTable.setPreferredScrollableViewportSize(viewTable.getPreferredSize());
            viewTable.setRowHeight(100);
            Object[] row = new Object[10];
            while (rs.next()) {
                Icon icon = new ImageIcon(getBufferedImage(rs.getBlob("foto_anggota")));
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("npm");
                row[3] = rs.getString("tempat_tanggal_lahir");
                row[4] = rs.getString("jenis_kelamin");
                row[5] = rs.getString("email");
                row[6] = rs.getString("nomor_telepon");
                row[7] = rs.getString("alamat");
                row[8] = rs.getString("status");
                row[9] = icon;
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getBufferedImage(Blob imageBlob) {
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

    public DataMahasiswaFrame() {
        tambahButton.addActionListener(e -> {
            inputframe inputframe = new inputframe();
            inputframe.setVisible(true);
        });

        tutupButton.addActionListener(e -> {
            dispose();
        });

        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
        @Override
        public void windowActivated(WindowEvent e) {
            isiTable();
        }
        });

        cariButton.addActionListener(e ->  {
            Connection c = koneksi.getConnection();
            String keyword = "%" + textField1.getText() + "%";
            String searchSQL = "SELECT * FROM data_mahasiswa WHERE nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[10];
                while (rs.next()){
                    Icon icon = new ImageIcon(getBufferedImage(rs.getBlob("foto_anggota")));
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("npm");
                    row[3] = rs.getString("tempat_tanggal_lahir");
                    row[4] = rs.getString("jenis_kelamin");
                    row[5] = rs.getString("email");
                    row[6] = rs.getString("nomor_telepon");
                    row[7] = rs.getString("alamat");
                    row[8] = rs.getString("status");
                    row[9] = icon;
                    dtm.addRow(row);
                }
            } catch (SQLException ex){
                throw new RuntimeException(ex);
            }
        });

        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null,"Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,"Yakin mau hapus ?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION );
            if(pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
                Connection c = koneksi.getConnection();
                String deleteSQL = "DELETE FROM data_mahasiswa WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            inputframe inputframe = new inputframe();
            inputframe.setId(id);
            inputframe.isikomponen();
            inputframe.setVisible(true);
        });

        isiTable();
        init();
    }

}



