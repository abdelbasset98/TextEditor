# TextEditor (Java Swing)

A desktop text editor written in Java with Swing. It's an Eclipse project and a rewrite of my earlier single-file AWT editor.

## Features

- **File**: New, Open, Save, Save As, Quit. If the document has unsaved changes, the editor asks whether to save before New, Open, Quit or closing the window.
- **Edit**: Undo, Redo, Cut, Copy, Paste, Select All
- **Font**: family, style (Normal, Bold, Italic, Bold Italic) and size (10 to 28)
- **Color**: background and text color (`JColorChooser`)
- **Status bar**: word, line and character counts, plus the current font
- **UTF-8 files**: accented characters (é, à, ç, €…) are preserved. Older ANSI files (Windows-1252) still open correctly.
- **Keyboard shortcuts**: Ctrl+N, Ctrl+O, Ctrl+S, Ctrl+Shift+S, Ctrl+Q, Ctrl+Z, Ctrl+Y, Ctrl+X, Ctrl+C, Ctrl+V, Ctrl+A

The interface is in French (Fichier, Édition, Police, Couleur).

## Project structure

```
src/com/esisa/java/editor/
├── TextEditor.java        # Main window (JFrame): menus, actions, status bar
├── UndoableTextArea.java  # JTextArea with an UndoManager (undo/redo)
├── TextFile.java          # The edited file: name, path, modified flag, "Sans titre N" counter
├── TextStats.java         # Word / line / character counting
└── FileManager.java       # Reading and writing files (UTF-8, with an ANSI fallback when reading)
```

## Java concepts used

| Concept | Where |
|---|---|
| Encapsulation, overloaded constructors, `static` members | `TextFile` |
| Inheritance and `@Override` | `TextEditor extends JFrame`, `UndoableTextArea extends JTextArea` |
| Polymorphism through interfaces | One inner `ActionListener` class per menu group |
| Wrapper classes | `Integer.parseInt()` for font sizes |
| `String` immutability, `equals()` vs `==` | `FileManager`, menu commands |
| Arrays, for-each, varargs | Font menus built from arrays with `addItems(menu, listener, String... labels)` |

## Requirements

- JDK 25 or later. The Eclipse project is configured for Java SE 25.

## How to run

**Eclipse:** File → Import → Existing Projects into Workspace, select this folder, then run `TextEditor.java` as a Java Application.

**Command line** (on macOS or Linux, write `bin:` instead of `bin;` in the second command):

```bash
javac -d bin src/com/esisa/java/editor/*.java
java -cp bin com.esisa.java.editor.TextEditor
```

## Author

Abdelbasset EL.Mountassir
