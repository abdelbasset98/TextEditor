package com.esisa.java.editor;

/*
 * Statistiques d'un texte : nombre de caractères, de mots et de lignes.
 *
 * Amélioration de Data.wordCount() (p03) : au lieu de compter les espaces
 * (ce qui donne un mauvais résultat pour "Java   EE" ou un texte sur plusieurs lignes),
 * on compte les débuts de mots : un caractère non blanc qui suit un blanc.
 */
public class TextStats {
	private int chars, words, lines;

	public TextStats(String text) {
		chars = text.length();
		lines = 1;
		words = 0;
		boolean inWord = false;
		for (int i = 0; i < chars; i++) {
			char c = text.charAt(i);
			if (c == '\n') lines++;
			if (Character.isWhitespace(c)) {
				inWord = false;
			} else if (!inWord) {
				inWord = true;
				words++;
			}
		}
	}

	public int getChars() {
		return chars;
	}

	public int getWords() {
		return words;
	}

	public int getLines() {
		return lines;
	}

	@Override
	public String toString() {
		return "Mots : " + words + "  |  Lignes : " + lines + "  |  Caractères : " + chars;
	}
}
