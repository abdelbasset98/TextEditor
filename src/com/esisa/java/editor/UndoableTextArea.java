package com.esisa.java.editor;

import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

/*
 * Zone de texte avec historique Annuler / Rétablir.
 *
 * Héritage (p04) : un UndoableTextArea EST UN JTextArea avec en plus un UndoManager.
 * => Affectation Fille -> Mère possible : JTextArea t = new UndoableTextArea();
 *
 * Par rapport à l'ancienne version (TextArea AWT + StateEdit) : le document Swing
 * signale lui-même chaque modification à l'UndoManager. L'ancienne version
 * enregistrait aussi le setText() fait pendant un Undo comme une nouvelle
 * modification, ce qui cassait Redo.
 */
public class UndoableTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;

	private UndoManager undoManager;

	public UndoableTextArea() {
		super();
		initUndoable();
	}

	public UndoableTextArea(int rows, int columns) {
		super(rows, columns);
		initUndoable();
	}

	private void initUndoable() {
		undoManager = new UndoManager();
		getDocument().addUndoableEditListener(undoManager);
	}

	public boolean undo() {
		if (!undoManager.canUndo()) return false;
		undoManager.undo();
		return true;
	}

	public boolean redo() {
		if (!undoManager.canRedo()) return false;
		undoManager.redo();
		return true;
	}

	// Vider l'historique (après Nouveau / Ouvrir)
	public void clearHistory() {
		undoManager.discardAllEdits();
	}
}
