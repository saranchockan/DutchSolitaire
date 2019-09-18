import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.io.*;

public class Cards{
    private File image;
    private int x;
    private int y;
    private boolean isBlank;

    public Cards(File image, int x, int y, boolean isBlank){
        this.image = image;
        this.x = x;
        this.y = y;
        this.isBlank = isBlank;
    }

    public File getFile(){
        return image;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public boolean isBlank(){
        return isBlank;
    }

    public void setFile(File image){
        this.image = image;
    }

    public void setBlank(boolean blank){
        this.isBlank = blank;
    }   


}