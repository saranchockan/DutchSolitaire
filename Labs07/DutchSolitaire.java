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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DutchSolitaire {
	public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
			JFrame frame = new JFrame();
			Panel myPanel = new Panel();
	        frame.add(myPanel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setSize(1050, 420);
			frame.setVisible(true);
			frame.setResizable(false);
        }
    });
  }
}

class Panel extends JPanel implements MouseListener  {

  BufferedImage image;
  private Cards [][] cards;
  private File directory;
  private File [] images;
  private ArrayList<File> files;

  private Cards[] aceCards;
  private File aceDirectory;
  private File [] aceImages;

  private boolean firstcardClicked = false;
  private Cards firstclickedCard = null;

  private boolean secondcardClicked = false;
  private Cards secondclickedCard = null;


  private Cards rightCard = null;
  private Cards leftCard = null;
  private boolean rbool = false;
  private boolean lbool = false;
  private boolean aces = false;

  private boolean swappable = false;

  public Panel() {


		cards = new Cards[4][13];
		directory = new File("/Users/Saran/Desktop/Clements HS/Clements HS 17-18/CS3 Labs/Student version - DutchSolitaire/Labs07/CardImages");
		if(directory.isDirectory()){
			images = directory.listFiles();
		}
		files = new ArrayList<File>(Arrays.asList(images));
		Collections.shuffle(files);
		images = files.toArray(new File[files.size()]);
		createCards();

		aceCards = new Cards[4];
		aceDirectory = new File("/Users/Saran/Desktop/Clements HS/Clements HS 17-18/CS3 Labs/Student version - DutchSolitaire/Labs07/Ace Images");
		aceImages = aceDirectory.listFiles();
		createAceCards();

		addMouseListener(this);

  }

  @Override
  public void paintComponent(Graphics g) {
			super.paintComponent(g);

			int count = 1;

			for(int r = 0;r<cards.length;r++){
				for(int c = 0;c<cards[r].length;c++){

					try{
						image = ImageIO.read(cards[r][c].getFile());


						g.drawRect(cards[r][c].getX(),cards[r][c].getY(),73,97);
						g.drawImage(image,cards[r][c].getX(),cards[r][c].getY(),null);


						
					}catch(Exception e){
						e.printStackTrace();
				}
			}
		}

			for(int k = 0;k<aceCards.length;k++){
				try{
					image = ImageIO.read(aceCards[k].getFile());
					g.drawImage(image,aceCards[k].getX(),aceCards[k].getY(),null);
				}catch(Exception e){
					e.printStackTrace();
			}
		}

		if(firstcardClicked){
			Graphics2D g2 = (Graphics2D) g;
			g.setColor(Color.RED);
			float thickness = 4;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(thickness));
			g.drawRect(firstclickedCard.getX(),firstclickedCard.getY(), 73, 97);
			g2.setStroke(oldStroke);
		}

		if(secondcardClicked){
			if(rbool){
				if(rightCardValid()){
					swappable = true;
				}
			}
			else if(aces){
				swappable = true;
			}

			if(lbool){
				if(leftCardValid()){
					swappable = true;
				}
			}

			if(swappable){
				swap(firstclickedCard, secondclickedCard);
			}
			aces = false;
			swappable = false;
			rbool = false;
			lbool = false;
			firstcardClicked = false;
			firstclickedCard = null;
			secondcardClicked = false;
			secondclickedCard = null;
			rightCard = null;
			leftCard = null;
		}
	  }
	public void showFiles(){

			int count = 0;
			for(File file: images){
				System.out.println(file.getName());
				count++;
			}

			System.out.println(count);
  }

  public void createCards(){

  	showFiles();
	int index = 0;
	int x = 0;
	int y = 0;

	for(int r = 0;r<cards.length;r++){
			for(int c = 0;c<cards[r].length;c++){

				boolean isBlank = false;
				if(images[index].getName().substring(0,2).equals("15")){
					isBlank = true;
				}
				Cards temp = new Cards(images[index], x, y,isBlank);
				cards[r][c] = temp;
				x+=75;
				index++;
			}
			x=0;
			y+=100;
		}
	}

	public void createAceCards(){
			int x = 975;
			int y = 0;

			for(int i = 0;i<aceCards.length;i++){
				Cards temp = new Cards(aceImages[i],x,y,false);
				aceCards[i] = temp;
				y+=100;
			}
	}

	public void mousePressed(MouseEvent e) {
	
		int x = e.getX();
		int y = e.getY();
		if(!firstcardClicked){
			for(int r = 0;r<cards.length;r++){
				for(int c = 0;c<cards[r].length;c++){
					Rectangle check = new Rectangle(cards[r][c].getX(),cards[r][c].getY(),73,97);
					if(check.contains(x, y) && !cards[r][c].isBlank()){
						firstcardClicked = true;
						firstclickedCard = cards[r][c];
						repaint();
					}
				}
			}
		}

		else if(!secondcardClicked){

			for(int r = 0;r<cards.length;r++){
				for(int c = 0;c<cards[r].length;c++){
					Rectangle check = new Rectangle(cards[r][c].getX(),cards[r][c].getY(),73,97);
					if(check.contains(x, y) && cards[r][c].isBlank()){
						secondcardClicked = true;
						secondclickedCard = cards[r][c];
						checkRightCard(secondclickedCard,r,c);
						checkLeftCard(secondclickedCard, r, c);
						repaint();
					}
					else if(check.contains(x, y)){
						firstcardClicked = true;
						firstclickedCard = cards[r][c];
						repaint();
					}
				}
			}
		}

	}

	 public void mouseReleased(MouseEvent e) {}
	 public void mouseEntered(MouseEvent e) {}
	 public void mouseExited(MouseEvent e) {}
	 public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(!firstcardClicked){
			for(int r = 0;r<cards.length;r++){
				for(int c = 0;c<cards[r].length;c++){
					Rectangle check = new Rectangle(cards[r][c].getX(),cards[r][c].getY(),73,97);
					if(check.contains(x, y) && !cards[r][c].isBlank()){
						firstcardClicked = true;
						firstclickedCard = cards[r][c];
						repaint();
					}
				}
			}
		}
	 }

	 public void swap(Cards card, Cards blank){
		File image = card.getFile();
		Boolean b = card.isBlank();
		card.setFile(blank.getFile());
		card.setBlank(blank.isBlank());
		blank.setFile(image);
		blank.setBlank(b);
		repaint();
	 }

	 public boolean inBounds(int r, int c){
        return ((r > -1 && r < cards.length) && (c > -1 && c < cards[0].length));
	}

	public void checkRightCard(Cards card, int r, int c){
		int row = r;
		int col = c+1;

		if(inBounds(row, col)){
			rbool = true;
		}
		else{
			checkAces(row);
		}

		if(rbool){
			rightCard = cards[row][col];
		}
	}

	public void checkLeftCard(Cards card, int r, int c){
		int row = r;
		int col = c-1;

		if(inBounds(row, col)){
			lbool = true;
		}
		else{
			if(c==0){

				if(firstclickedCard.getFile().getName().substring(0,1).equals("2"))
				{
					String n = firstclickedCard.getFile().getName().substring(1);
					int num = 14;
					String name = String.valueOf(num) + n;
					if(name.equals(aceCards[row].getFile().getName())){
						swappable = true;
					}
				}
			}
		}

		if(lbool){
			leftCard = cards[row][col];
		}
	}

	public boolean rightCardValid(){

		String n1 = firstclickedCard.getFile().getName();
		String n2  = "";
		if(n1.length()==6){
			int num = Integer.parseInt(n1.substring(0,1))+1;
			n2 = num + n1.substring(1);
		}
		else{
			int num = Integer.parseInt(n1.substring(0,2))+1;
			n2 = num + n1.substring(2);
		}
		if(n2.equals(rightCard.getFile().getName())){

			return true;
		}
		return false;
	}

	public boolean leftCardValid(){

		String n1 = firstclickedCard.getFile().getName();
		String n2  = "";
		if(n1.length()==6){
			int num = Integer.parseInt(n1.substring(0,1))-1;
			n2 = num + n1.substring(1);
		}
		else{
			int num = Integer.parseInt(n1.substring(0,2))-1;
			n2 = num + n1.substring(2);
		}

		if(n2.equals(leftCard.getFile().getName())){
			return true;
		}
		return false;
	}

	public void checkAces(int r){
		String n1 = firstclickedCard.getFile().getName();
		String n2  = "";
		if(n1.length()==6){
			int num = Integer.parseInt(n1.substring(0,1))+1;
			n2 = num + n1.substring(1);
		}
		else{
			int num = Integer.parseInt(n1.substring(0,2))+1;
			n2 = num + n1.substring(2);
		}
		if(n2.equals(aceCards[r].getFile().getName())){
			aces = true;
		}
	}
}
