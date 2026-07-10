import javax.print.attribute.HashAttributeSet;
import javax.sound.sampled.Clip;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.*;
import java.util.Hashtable;

abstract class UndoableTextArea extends TextArea implements StateEditable {
    private final static String KEY_STATE = "UndoableTextAreaKey";
    private boolean textChanged = false;
    private UndoManager undoManager;
    private StateEdit currentEdit;

    public UndoableTextArea() {
        super();
        initUndoable();
    }
    public UndoableTextArea(String string) {
        super(string);
        initUndoable();
    }
    public UndoableTextArea(int rows, int columns) {
        super(rows, columns);
        initUndoable();
    }
    public UndoableTextArea(String string, int rows, int columns) {
        super(string, rows, columns);
        initUndoable();
    }
    public UndoableTextArea(String string, int rows, int columns, int scrollbars) {
        super(string, rows, columns, scrollbars);
        initUndoable();
    }
    public boolean undo() {
        try {
            undoManager.undo();
            return true;
        } catch (CannotUndoException e) {
            System.out.println("Invalid");
            return false;
        }
    }
    public boolean redo() {
        try {
            undoManager.redo();
            return true;
        } catch (CannotRedoException e) {
            System.out.println("Invalid");
            return false;
        }
    }
    public void restoreState(Hashtable state){
        Object data=state.get(KEY_STATE);
        if(data!=null){
            setText((String)data);
        }
    }
    public void storeState(Hashtable state){
        state.put(KEY_STATE, getText());
    }
    private void takeSnapshot(){
        if(textChanged){
            currentEdit.end();
            undoManager.addEdit(currentEdit);
            textChanged=false;
            currentEdit=new StateEdit(this);
        }
    }
    private void initUndoable() {
        undoManager=new UndoManager();
        currentEdit=new StateEdit(this);
        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent event){
                if(event.isActionKey()){
                    takeSnapshot();
                }
            }
        });
        addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent fe){
                takeSnapshot();
            }
        });
        addTextListener(new TextListener() {
            public void textValueChanged(TextEvent e){
                textChanged=true;
                takeSnapshot();
            }
        });
    }
}

class FontType implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class FM implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Size implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class New implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Open implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Save implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Exit implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Cut implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Copy implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Paste implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class BC implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class WW implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

class Win extends WindowAdapter {
}

public class TextEditor extends Frame{
    boolean b=true;
    Frame fm;
    FileDialog fd;
    Font f;
    int style=Font.PLAIN;
    int fsize=12;
    UndoableTextArea txt;
    String filename,st,fn="untitled",dn;
    Clipboard clip=getToolkit().getSystemClipboard();
    TextEditor(){
        f=new Font("Courier",style,fsize);
        setLayout(new GridLayout(1,1));
        txt=new UndoableTextArea(80,25){};

        txt.setFont(f);
        add(txt);
        MenuBar mn=new MenuBar();
        Menu fonttype=new Menu("FontType");
        MenuItem one,two,three,four,five,six;
        one=new MenuItem("TemesRoman");
        two=new MenuItem("Helvetica");
        three=new MenuItem("Courier");
        four=new MenuItem("Arial");
        five=new MenuItem("Arial Black");
        six=new MenuItem("Century");

        fonttype.add(one);
        fonttype.add(two);
        fonttype.add(three);
        fonttype.add(four);
        fonttype.add(five);
        fonttype.add(six);
        one.addActionListener(new FontType());
        two.addActionListener(new FontType());
        three.addActionListener(new FontType());
        four.addActionListener(new FontType());
        five.addActionListener(new FontType());
        six.addActionListener(new FontType());

        Menu fontmenu=new Menu("Font");
        MenuItem boldmenu=new MenuItem("Bold");
        MenuItem plainmenu=new MenuItem("Plain");
        MenuItem italicmenu=new MenuItem("Italic");
        fontmenu.add(boldmenu);
        fontmenu.add(plainmenu);
        fontmenu.add(italicmenu);
        boldmenu.addActionListener(new FM());
        plainmenu.addActionListener(new FM());
        italicmenu.addActionListener(new FM());
        Menu size=new Menu("Size");
        MenuItem s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        s1=new MenuItem("10");
        s2=new MenuItem("12");
        s3=new MenuItem("14");
        s4=new MenuItem("16");
        s5=new MenuItem("18");
        s6=new MenuItem("20");
        s7=new MenuItem("22");
        s8=new MenuItem("24");
        s9=new MenuItem("26");
        s10=new MenuItem("28");
        size.add(s1);
        size.add(s2);
        size.add(s3);
        size.add(s4);
        size.add(s5);
        size.add(s6);
        size.add(s7);
        size.add(s8);
        size.add(s9);
        size.add(s10);

        s1.addActionListener(new Size());
        s2.addActionListener(new Size());
        s3.addActionListener(new Size());
        s4.addActionListener(new Size());
        s5.addActionListener(new Size());
        s6.addActionListener(new Size());
        s7.addActionListener(new Size());
        s8.addActionListener(new Size());
        s9.addActionListener(new Size());
        s10.addActionListener(new Size());
        fontmenu.add(size);
        Menu file=new Menu("File");
        MenuItem n=new MenuItem("New",new MenuShortcut(KeyEvent.VK_N));
        MenuItem o=new MenuItem("Open",new MenuShortcut(KeyEvent.VK_O));
        MenuItem s=new MenuItem("Save",new MenuShortcut(KeyEvent.VK_S));
        MenuItem e=new MenuItem("Exit",new MenuShortcut(KeyEvent.VK_E));
        n.addActionListener(new New());
        file.add(n);
        o.addActionListener(new Open());
        file.add(o);
        s.addActionListener(new Save());
        file.add(s);
        e.addActionListener(new Exit());
        file.add(e);
        mn.add(file);
        addWindowFocusListener(new Win());
        Menu edit=new Menu("Edit");
        MenuItem cut=new MenuItem("Cut",new MenuShortcut(KeyEvent.VK_X));
        MenuItem copy=new MenuItem("Copy",new MenuShortcut(KeyEvent.VK_C));
        MenuItem paste=new MenuItem("Paste",new MenuShortcut(KeyEvent.VK_V));

        cut.addActionListener(new Cut());
        edit.add(cut);
        copy.addActionListener(new Copy());
        edit.add(copy);
        paste.addActionListener(new Paste());
        edit.add(paste);
        Menu color=new Menu("Color");
        MenuItem Bg=new MenuItem("Background");
        MenuItem Fg=new MenuItem("Foreground");
        Bg.addActionListener(new BC());
        Fg.addActionListener(new BC());
        Menu undo=new Menu("Undo&Redo");
        MenuItem un=new MenuItem("Undo");
        MenuItem re=new MenuItem("Redo");
        un.addActionListener(new WW());
        re.addActionListener(new WW());
        undo.add(un);
        undo.add(re);
        color.add(Bg);
        color.add(Fg);
        mn.add(edit);
        mn.add(fontmenu);
        mn.add(fonttype);
        mn.add(color);
        mn.add(undo);
        setMenuBar(mn);
    }

    public static void main(String[] args) {
        TextEditor te = new TextEditor();
        te.setSize(700, 500);
        te.setVisible(true);
    }
}