
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * 
 * @author Thomas
 *
 */
public class Game {
	// Instantiate new Scanner, new Random
	Scanner console = new Scanner(System.in);
	Random randomGenerator = new Random();

	/*
	 * Declare the following: game board, which will have the level. the
	 * moveInput, where the user input will be stored playerLoc[], contains the
	 * players starting location alienNumber, # of aliens on the screen
	 * alienDirection, true for right, false for left gameOver, boolean to allow
	 * game play to continue if false
	 */
	String gameboard[][];
	private String moveInput = null;
	private int playerLoc[] = { 14, 7 };
	private int alienNumber = 0;
	private boolean alienDirection = true;
	private boolean gameOver = false;
	/**
	 * @param gameOver the gameOver to set
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	private static int playerLives = 3;
	protected PlayerBullets pBullets;

	/**
	 * 
	 * @param gameboard
	 */
	public Game(String[][] gameboard) {
		this.gameboard = gameboard;
		pBullets = new PlayerBullets();
		setAlienNumber();
	}

	/**
	 * 
	 * @param someLevel
	 * @return the 2D array which is the game board.
	 * @throws IOException
	 */
	public static String[][] loadLevel(String someLevel) throws IOException {
		File level = new File(someLevel); // takes in the pathway and creates a
											// variable, level, (type File) from
											// it
		FileReader fileReader = new FileReader(level); // takes the level and
														// makes a fileReader
														// from it.
		BufferedReader input = new BufferedReader(fileReader); // takes the
																// filereader
																// and creates a
																// buffered
																// reader
		String theLine = input.readLine(); // creates a string from the first
											// line read from the input
		String[][] gameboard = new String[16][15]; // declares the 2d array into
													// which we will read our
													// file's contents
		int LineIndex = 0; // Set an index for the rows/lines
		while (theLine != null) { // While loop to read all the lines of the
									// file, assign the chars to
			char[] characters = theLine.toCharArray(); // a char[] and place
														// them into the
														// gameboard one
														// element/char at a
														// time
			for (int i = 0; i < characters.length; i++) {
				gameboard[LineIndex][i] = Character.toString(characters[i]);
			}
			LineIndex++; // increment row/line counter
			theLine = input.readLine(); // get next line from file
		}
		input.close();
		return gameboard;

	}

	/**
	 * Prints the 2d array
	 */
	void printGameboard() {
		for (int i = 0; i < gameboard.length; i++) {
			System.out.print(gameboard[i][0]);
			for (int j = 1; j < gameboard[i].length; j++) {
				System.out.print(gameboard[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Game loop. This essentially is the game. It checks to see if the player
	 * has lives left, if there are any aliens left, and to make sure the user
	 * did not quit. Then, it prompts for a command, and calls the methods to
	 * move the aliens, the player, the bullets, and to create any bullets, then
	 * prints the updated board before looping.
	 */
	void playGame() {
		while (getPlayerLives() > 0 && levelComplete() && getGameOver() == false) {
			promptUser();
			alienBulletMovement();
			alienMovement();
			alienShooting();
			playerBulletMovement();
			playerLogic();
			setAlienNumber();
			printGameboard();
		}
	}

	boolean levelComplete() {
		return alienNumber > 0;
	}

	/**
	 * 
	 * @return if the game should be over from the boolean variable gameOver
	 */
	boolean getGameOver() {
		return gameOver;
	}

	/**
	 * Prompts user for a movement/fire command and assigns it to moveInput
	 */
	void promptUser() {
		System.out.println(
				"Please input your move:\nA - Move left\nD - Move right\nAF - Move left then shoot\nFA - Shoot then move left\nDF - Move right then shoot\nFD - Shoot then move right\nF - stand still and shoot\nQ- Quit game");
		moveInput = console.next();
	}

	/**
	 * This moves the aliens left and right across the screen. Since they move
	 * as a group this works for a set of individual aliens and for bosses.
	 */
	void alienBulletMovement() {
		for (int r = gameboard.length - 2; r > 0; r--) {
			for (int c = 1; c < gameboard[r].length - 1; c++) {
				if (gameboard[r][c].equals("v")) {
					gameboard[r][c] = " ";
					if (gameboard[r + 1][c].equals("x") || gameboard[r + 1][c].equals("^")
							|| gameboard[r + 1][c].equals("=")) {
						gameboard[r + 1][c] = " ";
					} else if (gameboard[r + 1][c].equals("_")) {
						gameboard[r + 1][c] = "_";
					} else if (gameboard[r + 1][c].equals("W")) {
						losePlayerLives();
					} else {
						gameboard[r + 1][c] = "v";
					}
				}
			}
		}
	}

	/**
	 * Changes the direction of the aliens to move right by setting
	 * alienDirection to true
	 */
	void setAlienDirectionRight() {
		alienDirection = true;
	}

	/**
	 * Changes the direction of the aliens to move right by setting
	 * alienDirection to true
	 */
	void setAlienDirectionLeft() {
		alienDirection = false;
	}

	boolean getAlienDirection(){
		return alienDirection;
	}
	
	/**
	 * Logic that determines which of the above setters to use. If the aliens
	 * are along the side of the board, it will move them down and change the
	 * movement direction. If not along the side of the board, it will call the
	 * simpler alienMove method.
	 */
	void alienMovement() {
		for (int r = 1; r < gameboard.length - 1; r++) {
			if (gameboard[r][1].equals("x")) {
				alienMoveDown();
				setAlienDirectionRight();
				break;
			} else if (gameboard[r][gameboard[r].length - 2].equals("x")) {
				alienMoveDown();
				setAlienDirectionLeft();
				break;
			}
		}
		alienMove();
	}

	/**
	 * Scans the 2D array looking for the aliens and moving them down one row.
	 * If one reached the bottom, it sets gameOver to be true. If it touches a
	 * player, it subtracts a player life. If it touches a bullet or barrier,
	 * they both are removed.
	 */
	void alienMoveDown() {
		for (int r1 = gameboard.length - 2; r1 > 0; r1--) {
			for (int c = gameboard[r1].length - 1; c > 0; c--) {
				if (gameboard[r1][c].equals("x")) {
					if (gameboard[r1 + 1][c].equals("_")) {
						gameOver = true;
						gameboard[r1][c] = " ";
						gameboard[r1 + 1][c] = "x";
					} else if (gameboard[r1 + 1][c].equals("W")) {
						losePlayerLives();
					} else if (!gameboard[r1][c].equals("W")) {
						gameboard[r1][c] = " ";
						gameboard[r1 + 1][c] = "x";
					} else if (gameboard[r1 + 1][c].equals("=")) {
						gameboard[r1][c] = " ";
						gameboard[r1 + 1][c] = " ";
					}
				}
			}
		}
	}

	/**
	 * Moves the aliens either left or right depending on the alienDirection
	 * boolean.
	 */
	void alienMove() {
		if (alienDirection == true)
			for (int r1 = 1; r1 < gameboard.length - 1; r1++) {
				for (int c = gameboard[r1].length - 1; c > 0; c--) {
					if (gameboard[r1][c].equals("x")) {
						gameboard[r1][c] = " ";
						gameboard[r1][c + 1] = "x";
					}
				}

			}
		else if (alienDirection == false) {
			for (int r1 = 1; r1 < gameboard.length - 1; r1++) {
				for (int c = 1; c < gameboard[r1].length - 1; c++) {
					if (gameboard[r1][c].equals("x")) {
						gameboard[r1][c] = " ";
						gameboard[r1][c - 1] = "x";
					}
				}
			}
		}
	}

	/**
	 * Determines which aliens will shoot on a normal level and places the
	 * bullets.
	 */
	void alienShooting() {

		for (int r = 1; r < gameboard.length - 2; r++) {
			for (int c = 1; c < gameboard[r].length - 1; c++) {
				if (gameboard[r][c].equals("x")) {
					if (!gameboard[r + 2][c].equals("x") && gameboard[r + 1][c].equals(" ")) {
						if (randomGenerator.nextInt() < (1 / alienNumber)) {
							gameboard[r + 1][c] = "v";
						}
					} else if (gameboard[r + 1][c].equals("W")) {
						losePlayerLives();
					}
				}
			}
		}

	}

	/**
	 * Moves the playerbullets which are already on the board.
	 */
	void playerBulletMovement() {

		for (int idx = 0; idx < pBullets.size(); idx++) {
			Node temp = pBullets.getNode(idx);
			if (gameboard[temp.getRow()][temp.getColumn()] == "^") {
				gameboard[temp.getRow()][temp.getColumn()] = " ";
			}
			if (gameboard[temp.getRow() - 1][temp.getColumn()].equals("_")) {
				gameboard[temp.getRow() - 1][temp.getColumn()] = "_";
				pBullets.remove(idx);
			} else if (gameboard[temp.getRow() - 1][temp.getColumn()].equals("x")
					|| gameboard[temp.getRow() - 1][temp.getColumn()].equals("v")
					|| gameboard[temp.getRow() - 1][temp.getColumn()].equals("=")) {
				gameboard[temp.getRow() - 1][temp.getColumn()] = " ";
				pBullets.remove(idx);
			} else {
				gameboard[temp.getRow() - 1][temp.getColumn()] = "^";
				temp.setRow(temp.getRow() - 1);
			}

		}
	}

	/**
	 * Moves the player right one.
	 */
	void playerMoveRight() {
		if (playerLoc[1] != 13) {
			gameboard[playerLoc[0]][playerLoc[1]] = " ";
			playerLoc[1]++;
			gameboard[playerLoc[0]][playerLoc[1]] = "W";
		}
	}

	/**
	 * Moves the player left one.
	 */
	void playerMoveLeft() {
		if (playerLoc[1] != 1) {
			gameboard[playerLoc[0]][playerLoc[1]] = " ";
			playerLoc[1]--;
			gameboard[playerLoc[0]][playerLoc[1]] = "W";
		}
	}

	void playerShoot() {

		if (gameboard[playerLoc[0] - 1][playerLoc[1]].equals(" ")) {
			gameboard[playerLoc[0] - 1][playerLoc[1]] = "^";

		} else {
			gameboard[playerLoc[0] - 1][playerLoc[1]] = " ";
		}

		pBullets.add(playerLoc[0] - 1, playerLoc[1]);

	}

	/**
	 * Tests the moveInput from the prompt and calls the respective methods.
	 */
	void playerLogic() {
		{
			if (moveInput.equals("D")) {
				playerMoveRight();
			} else if (moveInput.equals("A")) {
				playerMoveLeft();
			} else if (moveInput.equals("AF")) {
				playerMoveLeft();
				playerShoot();
			} else if (moveInput.equals("FA")) {
				playerShoot();
				playerMoveLeft();
			} else if (moveInput.equals("FD")) {
				playerShoot();
				playerMoveLeft();
			} else if (moveInput.equals("DF")) {
				playerMoveRight();
				playerShoot();
			} else if (moveInput.equals("F")) {
				playerShoot();
			} else if (moveInput.equals("Q")) {
				while (getPlayerLives() > 0) {
					losePlayerLives();
				}
			}
		}
	}

	/**
	 * Cycles through 2D array game board and counts aliens and sets the number.
	 */
	void setAlienNumber() {
		alienNumber = 0;
		for (int r = gameboard.length - 1; r > 0; r--) {
			for (int c = gameboard[r].length - 1; c > 0; c--) {
				if (gameboard[r][c].equals("x")) {
					alienNumber++;
				}
			}
		}
	}

	/**
	 * 
	 * @return alienNumber
	 */
	int getAlienNumber() {
		return alienNumber;
	}

	/**
	 * Lowers playerLives by 1
	 */
	void losePlayerLives() {
		playerLives--;
	}

	/**
	 * 
	 * @return playerLives
	 */
	int getPlayerLives() {
		return playerLives;
	}
}
