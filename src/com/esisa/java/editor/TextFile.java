package com.esisa.java.editor;

import java.io.File;

/*
 * Représente le fichier texte en cours d'édition.
 *
 * Comme la classe Product (p01) : un compteur de classe (static) permet de
 * numéroter automatiquement les nouveaux documents :
 *   1er document "Sans titre 1", 2ème document "Sans titre 2", etc.
 *
 * Rq : on évite volontairement le nom "Document" car javax.swing.text.Document
 * existe déjà => risque de collision de noms (voir Test.java de p01 avec java.awt.Point).
 */
public class TextFile {
	// Propriété de classe : compteur des documents "Sans titre"
	private static int counter = 0;

	// Propriétés d'instance (encapsulation : toutes privées)
	private File file; // null tant que le document n'a jamais été enregistré
	private String name;
	private boolean modified;

	// Surdéfinition (surcharge) du constructeur :
	public TextFile() {
		counter++;
		name = "Sans titre " + counter;
	}

	public TextFile(File file) {
		setFile(file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		this.name = file.getName();
	}

	public boolean hasFile() {
		return file != null;
	}

	public String getName() {
		return name;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public static int getCounter() {
		return counter;
	}

	// Titre de la fenêtre : une étoile signale les modifications non enregistrées
	public String getTitle() {
		return (modified ? "*" : "") + this + " - TextEditor";
	}

	// Redéfinition (Override) : invoquée implicitement par "..." + this
	@Override
	public String toString() {
		if (file == null) return name;
		return file.getAbsolutePath();
	}
}
