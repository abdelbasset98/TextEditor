# Java Text Editor (AWT/Swing)

A simple desktop text editor built with Java AWT and Swing, featuring undo/redo support, font customization, and clipboard operations.

## Features

- **File operations**: New, Open, Save, Exit
- **Edit operations**: Cut, Copy, Paste (via system clipboard)
- **Undo/Redo**: Full undo/redo history using `UndoManager` and `StateEdit`
- **Font customization**:
    - Font family (TemesRoman, Helvetica, Courier, Arial, Arial Black, Century)
    - Font style (Bold, Plain, Italic)
    - Font size (10–28)
- **Color customization**: Change text area background/foreground color using a built-in color picker (`JColorChooser`)
- Keyboard shortcuts for common actions (New: Ctrl+N, Open: Ctrl+O, Save: Ctrl+S, Exit: Ctrl+E, Cut: Ctrl+X, Copy: Ctrl+C, Paste: Ctrl+V)

## Project Structure
TextEditor.java       # Main file containing all classes
├── UndoableTextArea   # Custom TextArea with built-in undo/redo support
└── TextEditor         # Main application window (Frame) with menus and inner listener classes:
├── New, Open, Save, Exit   # File menu actions
├── Cut, Copy, Paste        # Edit menu actions
├── FontType, FM, Size      # Font menu actions
├── BC, ColorChooser, B     # Color menu actions
└── WW, Win, mylistener     # Undo/Redo and window-close handling

## Requirements

- Java JDK 8 or higher

## How to Run

```bash
javac TextEditor.java
java TextEditor
```

## Notes

- Built entirely with `java.awt` components (Frame, TextArea, MenuBar) plus `JColorChooser` from Swing for the color picker.
- This is a learning/practice project exploring AWT event handling, undo management, and clipboard integration.

## Author

Abdelbasset EL.Mountassir