package com.esisa.java.editor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Lecture / écriture des fichiers texte.
 * Méthodes de classe (static) : pas besoin d'instancier FileManager.
 *
 * Correction par rapport à l'ancien Editor : DataOutputStream.writeBytes()
 * perdait les caractères non ASCII (é, è, à, ç, €...). Ici on écrit en UTF-8.
 */
public class FileManager {
	// Encodage "ANSI" de Windows (fichiers créés par l'ancien Bloc-notes)
	private static final Charset ANSI = Charset.forName("windows-1252");

	public static String read(File file) throws IOException {
		Path path = file.toPath();
		String content;
		try {
			content = Files.readString(path, StandardCharsets.UTF_8);
		} catch (MalformedInputException e) {
			// Le fichier n'est pas en UTF-8 => on le relit en ANSI
			content = Files.readString(path, ANSI);
		}
		// Supprimer le BOM UTF-8 éventuel
		if (content.startsWith("﻿")) content = content.substring(1);
		// Fins de ligne Windows (\r\n) ou anciennes Mac (\r) => \n
		// String est immuable (p03) : replace() retourne une nouvelle chaîne
		return content.replace("\r\n", "\n").replace('\r', '\n');
	}

	public static void write(File file, String content) throws IOException {
		String text = content.replace("\n", System.lineSeparator());
		Files.writeString(file.toPath(), text, StandardCharsets.UTF_8);
	}
}
