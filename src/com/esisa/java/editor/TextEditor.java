package com.esisa.java.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * TextEditor : éditeur de texte (Swing), nouvelle version du projet "Editor".
 *
 * Fonctionnalités :
 *   + Fichier : Nouveau, Ouvrir, Enregistrer, Enregistrer sous, Quitter
 *     (avec demande d'enregistrement si le document a été modifié)
 *   + Édition : Annuler, Rétablir, Couper, Copier, Coller, Tout sélectionner
 *   + Police  : type, style et taille
 *   + Couleur : arrière-plan et texte (JColorChooser)
 *   + Barre d'état : nombre de mots, lignes, caractères et police courante
 *
 * Notions du cours utilisées :
 *   p01 : encapsulation, constructeurs surchargés, propriété static (TextFile)
 *   p02 : classes wrappers : Integer.parseInt() pour les tailles
 *   p03 : String, equals() au lieu de ==, immutabilité (FileManager, TextStats)
 *   p04 : héritage (UndoableTextArea extends JTextArea, TextEditor extends JFrame),
 *         redéfinition (@Override), polymorphisme (ActionListener)
 *   p05 : tableaux + for each (polices, tailles) et liste d'arguments variable (addItems)
 */
public class TextEditor extends JFrame {
	private static final long serialVersionUID = 1L;

	// 5.1 Tableaux : remplacent les variables one, two, ..., s1, ..., s10 de l'ancienne version
	private static final String FONT_NAMES[] = {
			"Times New Roman", "Arial", "Arial Black", "Courier New", "Century", "Consolas", "Monospaced"
	};
	private static final String STYLE_NAMES[] = { "Normal", "Gras", "Italique", "Gras italique" };
	private static final int STYLES[] = { Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC };
	private static final int FONT_SIZES[] = { 10, 12, 14, 16, 18, 20, 22, 24, 26, 28 };

	private UndoableTextArea txt;
	private JLabel status;
	private JFileChooser chooser;
	private TextFile textFile;

	private String fontName = "Consolas";
	private int fontStyle = Font.PLAIN;
	private int fontSize = 16;

	public TextEditor() {
		textFile = new TextFile();
		chooser = new JFileChooser();

		txt = new UndoableTextArea(25, 80);
		txt.setTabSize(4);
		txt.getDocument().addDocumentListener(new TextChange());
		add(new JScrollPane(txt), BorderLayout.CENTER);

		status = new JLabel();
		status.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
		add(status, BorderLayout.SOUTH);

		setJMenuBar(createMenuBar());
		applyFont();
		updateTitle();

		// Fermeture de la fenêtre : on demande d'abord s'il faut enregistrer
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		setSize(800, 600);
		setLocationRelativeTo(null); // centrer à l'écran
	}

	// ======================= Construction des menus =======================

	private JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();

		JMenu file = new JMenu("Fichier");
		FileAction fileAction = new FileAction();
		addItem(file, "Nouveau", shortcut(KeyEvent.VK_N), fileAction);
		addItem(file, "Ouvrir...", shortcut(KeyEvent.VK_O), fileAction);
		addItem(file, "Enregistrer", shortcut(KeyEvent.VK_S), fileAction);
		addItem(file, "Enregistrer sous...",
				KeyStroke.getKeyStroke(KeyEvent.VK_S, menuMask() | InputEvent.SHIFT_DOWN_MASK), fileAction);
		file.addSeparator();
		addItem(file, "Quitter", shortcut(KeyEvent.VK_Q), fileAction);

		JMenu edit = new JMenu("Édition");
		EditAction editAction = new EditAction();
		addItem(edit, "Annuler", shortcut(KeyEvent.VK_Z), editAction);
		addItem(edit, "Rétablir", shortcut(KeyEvent.VK_Y), editAction);
		edit.addSeparator();
		addItem(edit, "Couper", shortcut(KeyEvent.VK_X), editAction);
		addItem(edit, "Copier", shortcut(KeyEvent.VK_C), editAction);
		addItem(edit, "Coller", shortcut(KeyEvent.VK_V), editAction);
		edit.addSeparator();
		addItem(edit, "Tout sélectionner", shortcut(KeyEvent.VK_A), editAction);

		JMenu font = new JMenu("Police");
		JMenu type = new JMenu("Type");
		addItems(type, new FontNameAction(), FONT_NAMES); // un tableau passé à un paramètre variable
		JMenu style = new JMenu("Style");
		addItems(style, new FontStyleAction(), STYLE_NAMES);
		JMenu size = new JMenu("Taille");
		String sizeLabels[] = new String[FONT_SIZES.length];
		for (int i = 0; i < FONT_SIZES.length; i++) {
			sizeLabels[i] = "" + FONT_SIZES[i]; // int -> String
		}
		addItems(size, new FontSizeAction(), sizeLabels);
		font.add(type);
		font.add(style);
		font.add(size);

		JMenu color = new JMenu("Couleur");
		addItems(color, new ColorAction(), "Arrière-plan...", "Texte..."); // liste d'arguments variable

		bar.add(file);
		bar.add(edit);
		bar.add(font);
		bar.add(color);
		return bar;
	}

	private JMenuItem addItem(JMenu menu, String label, KeyStroke key, ActionListener listener) {
		JMenuItem item = new JMenuItem(label);
		item.setAccelerator(key);
		item.addActionListener(listener);
		menu.add(item);
		return item;
	}

	// 5.1.4 Liste d'arguments variable : addItems(menu, l, "A", "B") ou addItems(menu, l, tableau)
	private void addItems(JMenu menu, ActionListener listener, String... labels) {
		for (String label : labels) {
			addItem(menu, label, null, listener);
		}
	}

	private int menuMask() {
		return Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); // Ctrl sous Windows
	}

	private KeyStroke shortcut(int keyCode) {
		return KeyStroke.getKeyStroke(keyCode, menuMask());
	}

	// ======================= Écouteurs (classes internes) =======================
	// Polymorphisme : chaque classe fournit sa propre implémentation de actionPerformed()

	class FileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			// Comparaison de chaînes avec equals() et non == (p02, p03)
			if (cmd.equals("Nouveau")) newFile();
			else if (cmd.equals("Ouvrir...")) openFile();
			else if (cmd.equals("Enregistrer")) save();
			else if (cmd.equals("Enregistrer sous...")) saveAs();
			else if (cmd.equals("Quitter")) exit();
		}
	}

	class EditAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("Annuler")) txt.undo();
			else if (cmd.equals("Rétablir")) txt.redo();
			else if (cmd.equals("Couper")) txt.cut();
			else if (cmd.equals("Copier")) txt.copy();
			else if (cmd.equals("Coller")) txt.paste();
			else if (cmd.equals("Tout sélectionner")) txt.selectAll();
			txt.requestFocusInWindow();
		}
	}

	class FontNameAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fontName = e.getActionCommand();
			applyFont();
		}
	}

	class FontStyleAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			for (int i = 0; i < STYLE_NAMES.length; i++) {
				if (STYLE_NAMES[i].equals(cmd)) fontStyle = STYLES[i];
			}
			applyFont();
		}
	}

	class FontSizeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fontSize = Integer.parseInt(e.getActionCommand()); // Classe wrapper Integer (p02)
			applyFont();
		}
	}

	class ColorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Arrière-plan...")) {
				Color c = JColorChooser.showDialog(TextEditor.this, "Couleur d'arrière-plan", txt.getBackground());
				if (c != null) txt.setBackground(c);
			} else {
				Color c = JColorChooser.showDialog(TextEditor.this, "Couleur du texte", txt.getForeground());
				if (c != null) {
					txt.setForeground(c);
					txt.setCaretColor(c);
				}
			}
		}
	}

	// Écoute les modifications du texte pour l'étoile du titre et la barre d'état
	class TextChange implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			textChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			textChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// changement d'attributs uniquement (pas de texte) : rien à faire
		}
	}

	// ======================= Actions =======================

	private void textChanged() {
		if (!textFile.isModified()) {
			textFile.setModified(true);
			updateTitle();
		}
		updateStatus();
	}

	private void newFile() {
		if (!confirmSave()) return;
		txt.setText("");
		textFile = new TextFile(); // "Sans titre 2", "Sans titre 3", ...
		txt.clearHistory();
		updateTitle();
	}

	private void openFile() {
		if (!confirmSave()) return;
		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
		File file = chooser.getSelectedFile();
		try {
			String content = FileManager.read(file);
			txt.setText(content);
			txt.setCaretPosition(0);
			textFile = new TextFile(file);
			txt.clearHistory();
			updateTitle();
		} catch (IOException e) {
			showError("Impossible d'ouvrir le fichier :\n" + file + "\n\n" + e.getMessage());
		}
	}

	// Retourne true si le document a bien été enregistré
	private boolean save() {
		if (!textFile.hasFile()) return saveAs();
		return writeTo(textFile.getFile());
	}

	private boolean saveAs() {
		if (textFile.hasFile()) chooser.setSelectedFile(textFile.getFile());
		else chooser.setSelectedFile(new File(textFile.getName() + ".txt"));

		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return false;
		File file = chooser.getSelectedFile();
		if (file.exists() && !file.equals(textFile.getFile())) {
			int answer = JOptionPane.showConfirmDialog(this,
					"Le fichier \"" + file.getName() + "\" existe déjà.\nVoulez-vous le remplacer ?",
					"Confirmer", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer != JOptionPane.YES_OPTION) return false;
		}
		return writeTo(file);
	}

	private boolean writeTo(File file) {
		try {
			FileManager.write(file, txt.getText());
			textFile.setFile(file);
			textFile.setModified(false);
			updateTitle();
			return true;
		} catch (IOException e) {
			showError("Impossible d'enregistrer le fichier :\n" + file + "\n\n" + e.getMessage());
			return false;
		}
	}

	// Retourne true si on peut continuer (document enregistré ou modifications abandonnées)
	private boolean confirmSave() {
		if (!textFile.isModified()) return true;
		int answer = JOptionPane.showConfirmDialog(this,
				"Voulez-vous enregistrer les modifications de \"" + textFile.getName() + "\" ?",
				"TextEditor", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.YES_OPTION) return save();
		return answer == JOptionPane.NO_OPTION;
	}

	private void exit() {
		if (confirmSave()) System.exit(0);
	}

	private void applyFont() {
		txt.setFont(new Font(fontName, fontStyle, fontSize));
		updateStatus();
	}

	private void updateTitle() {
		setTitle(textFile.getTitle());
	}

	private void updateStatus() {
		TextStats stats = new TextStats(txt.getText());
		status.setText(stats + "  |  " + fontName + " " + fontSize);
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		try {
			// Apparence native de Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
		// Les composants Swing doivent être créés dans le thread graphique (EDT)
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TextEditor().setVisible(true);
			}
		});
	}
}
