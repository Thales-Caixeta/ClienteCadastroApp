import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class ClienteCadastroApp {
 private static final String DATABASE_URL = "jdbc:sqlite:clientes.db";
 public static void main(String[] args) {
 criarTabelaSeNaoExistir();
 SwingUtilities.invokeLater(ClienteCadastroApp::criarInterface);
 }
 private static void criarInterface() {
 JFrame frame = new JFrame("Cadastro de Clientes");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 300);
 JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
 JLabel lblNome = new JLabel("Nome:");
 JTextField txtNome = new JTextField();
 JLabel lblEmail = new JLabel("Email:");
 JTextField txtEmail = new JTextField();
 JLabel lblTelefone = new JLabel("Telefone:");
 JTextField txtTelefone = new JTextField();
 JButton btnSalvar = new JButton("Salvar");
 JButton btnMostrar = new JButton("Mostrar Clientes");
 panel.add(lblNome);
 panel.add(txtNome);
 panel.add(lblEmail);
 panel.add(txtEmail);
 panel.add(lblTelefone);
 panel.add(txtTelefone);
 panel.add(btnSalvar);
 panel.add(btnMostrar);
 frame.add(panel);
 btnSalvar.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 String nome = txtNome.getText();
 String email = txtEmail.getText();
 String telefone = txtTelefone.getText();
 if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
 JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos.",
"Erro", JOptionPane.ERROR_MESSAGE);
 return;
 }
 salvarCliente(nome, email, telefone);
 JOptionPane.showMessageDialog(frame, "Cliente salvo com sucesso!", "Sucesso",
JOptionPane.INFORMATION_MESSAGE);
 txtNome.setText("");
 txtEmail.setText("");
 txtTelefone.setText("");
 }
 });
 btnMostrar.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 mostrarClientes(frame);
 }
 });
 frame.setVisible(true);
 }
 private static void criarTabelaSeNaoExistir() {
 try (Connection conn = DriverManager.getConnection(DATABASE_URL);
 Statement stmt = conn.createStatement()) {
 String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
 "nome TEXT NOT NULL," +
 "email TEXT NOT NULL," +
 "telefone TEXT NOT NULL" +
 ");";
 stmt.execute(sql);
 } catch (SQLException e) {
 JOptionPane.showMessageDialog(null, "Erro ao criar a tabela: " + e.getMessage(),
"Erro", JOptionPane.ERROR_MESSAGE);
 }
 }
 private static void salvarCliente(String nome, String email, String telefone) {
 String sql = "INSERT INTO clientes(nome, email, telefone) VALUES(?, ?, ?);";
 try (Connection conn = DriverManager.getConnection(DATABASE_URL);
 PreparedStatement pstmt = conn.prepareStatement(sql)) {
 pstmt.setString(1, nome);
 pstmt.setString(2, email);
 pstmt.setString(3, telefone);
 pstmt.executeUpdate();
 } catch (SQLException e) {
 JOptionPane.showMessageDialog(null, "Erro ao salvar cliente: " + e.getMessage(),
"Erro", JOptionPane.ERROR_MESSAGE);
 }
 }
 private static void mostrarClientes(JFrame frame) {
 String sql = "SELECT * FROM clientes;";
 try (Connection conn = DriverManager.getConnection(DATABASE_URL);
 Statement stmt = conn.createStatement();
 ResultSet rs = stmt.executeQuery(sql)) {
 StringBuilder clientes = new StringBuilder();
 while (rs.next()) {
 clientes.append("ID: ").append(rs.getInt("id"))
 .append(", Nome: ").append(rs.getString("nome"))
 .append(", Email: ").append(rs.getString("email"))
 .append(", Telefone: ").append(rs.getString("telefone"))
 .append("\n");
 }
 if (clientes.length() == 0) {
 clientes.append("Nenhum cliente cadastrado.");
 }
 JOptionPane.showMessageDialog(frame, clientes.toString(), "Clientes Cadastrados",
JOptionPane.INFORMATION_MESSAGE);
 } catch (SQLException e) {
 JOptionPane.showMessageDialog(frame, "Erro ao buscar clientes: " + e.getMessage(),
"Erro", JOptionPane.ERROR_MESSAGE);
 }
 }
} 