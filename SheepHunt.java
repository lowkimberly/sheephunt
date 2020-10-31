import java.io.*;
import java.util.*;
import cs1.*;

public class SheepHunt {
    //This creates a new field of sheep and plays the game as long as the 
    //game is not over. When it is over, it shows the field one last time
    //and your score.
    public static void main (String[] args) {
	Field f = new Field(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
	while (!f.game_over) {
	    System.out.println(f);
	    f.wmove();
	    if (!f.game_over) {
		f.smove();
		if (f.sheepleft == 0) {
		    f.game_over = true;
		}
	    }
	}
	System.out.println(f);
	System.out.println("You ate "+f.score +" out of "+f.numbsheep+" sheep.");
    }
}

class Field {
    boolean game_over = false;
    char[][] f;
    int score=0;
    int numbsheep;
    int sheepleft;
    //wrow and wcol keep track of the wolf's coordinates
    int wrow=1,wcol=1;
    //This is how mean the sheep are
    int meanpercent;
    static Random r = new Random();
    public Field(int s,int m) {
	//set the number of sheep and sheepleft, which counts down to zero.
	//If sheepleft is 0,  the game ends.
	numbsheep = s; 
	sheepleft = s;
	meanpercent=m;
	f = new char[12][12];
	//Builds the fence
	for (int i = 0;i < 12;i++) {
	    for (int j = 0;j < 12;j++) {
		if ( i == 0 ||
		     i == 11 ||
		     j == 0 ||
		     j == 11) {
		    f[i][j] = 'X';
		}
		else { 
		    f[i][j] = ' ';
		}
	    }
	}
	//Sticks the wolf in the field and adds sheep
	f[1][1] = 'W';
	for (int i = 0;i < s;i++) {
	    int row = r.nextInt(12);
	    int col = r.nextInt(12);
	    while (f[row][col] != ' ') {
		row=r.nextInt(12);
		col=r.nextInt(12);
	    }
	    f[row][col]= 'S';
	} 
    }
    //This method accepts keyboard input to move the wolf around the field
    // and does something depending on where they end up
    public void wmove() {
	System.out.println("Choose n,s,e,w to go north, south, east or west: ");
	char m = Keyboard.readChar();
	if (m == 'n') {
	    wrow = wrow - 1;
	    if (f[wrow][wcol] == 'X') {
		f[wrow+1][wcol]= ' ';
		System.out.println("You commit suicide by running into the fence. Game over.");
		game_over = true;
	    }
	    else if (f[wrow][wcol] == 'S') {
		score++;
		System.out.println("You ate a sheep! Your score is now " + score +"/"+numbsheep);
		sheepleft--;
		f[wrow][wcol] = 'W';
		f[wrow+1][wcol] = ' ';
	    }
	    else {
		f[wrow][wcol] = 'W';
		f[wrow+1][wcol] = ' ';
	    }
	}
	else if (m == 's') {
	    wrow = wrow + 1;
	    if (f[wrow][wcol] == 'X') {
		f[wrow-1][wcol]= ' ';
		System.out.println("You commit suicide by running into the fence. Game over.");
		game_over = true;
	    }
	    else if (f[wrow][wcol] == 'S') {
		score++;
		System.out.println("You ate a sheep! Your score is now " + score + "/" + numbsheep);
		sheepleft--;
		f[wrow][wcol] = 'W';
		f[wrow-1][wcol] = ' ';
	    }
	    else {
		f[wrow][wcol] = 'W';
		f[wrow-1][wcol] = ' ';
	    }
	}
	else if (m == 'e') {
	    wcol = wcol + 1;
	    if (f[wrow][wcol] == 'X') {
		f[wrow][wcol-1]= ' ';
		System.out.println("You commit suicide by running into the fence. Game over.");
		game_over = true;
	    }
	    else if (f[wrow][wcol] == 'S') {
		score++;
		System.out.println("You ate a sheep! Your score is now " + score + "/" + numbsheep);
		sheepleft--;
		f[wrow][wcol] = 'W';
		f[wrow][wcol-1] = ' ';
	    }
	    else {
		f[wrow][wcol] = 'W';
		f[wrow][wcol-1] = ' ';
	    }
	}
	else {
	    wcol = wcol - 1;
	    if (f[wrow][wcol] == 'X') {
		f[wrow][wcol+1]= ' ';
		System.out.println("You commit suicide by running into the fence. Game over.");
		game_over = true;
	    }
	    else if (f[wrow][wcol] == 'S') {
		score++;
		System.out.println("You ate a sheep! Your score is now "+ score +"/" + numbsheep);
		sheepleft--;
		f[wrow][wcol] = 'W';
		f[wrow][wcol+1] = ' ';
	    }
	    else {
		f[wrow][wcol] = 'W';
		f[wrow][wcol+1] = ' ';
	    }
	}
	if (sheepleft == 0) {
	    game_over = true;
	}
    }
    //This moves the sheep, which goes in the opposite direction of the wolf
    //when the wolf is nearby or kills the wolf
    public void smove() {
	int i = r.nextInt(100);
	if (f[wrow-1][wcol] == 'S') {
	    if (i < meanpercent) {
		f[wrow-1][wcol] = ' ';
		f[wrow][wcol] = 'S';
		System.out.println("The mean sheep kills you.");
		game_over = true;
	    }
	    else if (f[wrow-2][wcol] == ' ') {
		f[wrow-1][wcol] = ' ';
		f[wrow-2][wcol] = 'S';
	    } 
	    else if (f[wrow-2][wcol] == 'S') {
		f[wrow-2][wcol] = ' ';
		f[wrow-1][wcol] = ' ';
		System.out.println("Oh no! The two sheep killed each other. Too bad.");
		sheepleft = sheepleft - 2;
	    }
	}
	if (f[wrow][wcol-1] == 'S' && !game_over) {
	    if (i < meanpercent) {
		f[wrow][wcol-1] = ' ';
		f[wrow][wcol] = 'S';
		System.out.println("The mean sheep kills you.");
		game_over = true;
	    }
	    else if (f[wrow][wcol-2] == ' ') {
		f[wrow][wcol-1] = ' ';
		f[wrow][wcol-2] = 'S';
	    } 
	    else if (f[wrow][wcol-2] == 'S') {
		f[wrow][wcol-2] = ' ';
		f[wrow][wcol-1] = ' ';
		System.out.println("Oh no! The two sheep killed each other. Too bad.");
		sheepleft = sheepleft - 2;
	    }
	}
	if (f[wrow][wcol+1] == 'S' && !game_over) {
	    if (i < meanpercent) {
		f[wrow][wcol+1] = ' ';
		f[wrow][wcol] = 'S';
		System.out.println("The mean sheep kills you.");
		game_over = true;
	    }
	    else if (f[wrow][wcol+2] == ' ') {
		f[wrow][wcol+1] = ' ';
		f[wrow][wcol+2] = 'S';
	    } 
	    else if (f[wrow][wcol+2] == 'S') {
		f[wrow][wcol+2] = ' ';
		f[wrow][wcol+1] = ' ';
		System.out.println("Oh no! The two sheep killed each other. Too bad.");
		sheepleft = sheepleft - 2;
	    }
	}
	if (f[wrow+1][wcol] == 'S' && !game_over) {
	    if (i < meanpercent) {
		f[wrow+1][wcol] = ' ';
		f[wrow][wcol] = 'S';
		System.out.println("The mean sheep kills you.");
		game_over = true;
	    }
	    else if (f[wrow+2][wcol] == ' ') {
		f[wrow+1][wcol] = ' ';
		f[wrow+2][wcol] = 'S';
	    } 
	    else if (f[wrow+2][wcol] == 'S') {
		f[wrow+2][wcol] = ' ';
		f[wrow+1][wcol] = ' ';
		System.out.println("Oh no! The two sheep killed each other. Too bad.");
		sheepleft = sheepleft - 2;
	    }
	}
    }
    public String toString () {
	String str="";
	for (int i = 0;i < 12;i++) {
	    for (int j = 0;j<12;j++) {
		str = str + f[i][j] + " ";
	    }
	    str = str + "\n";
	}
	return str;
    }
}

