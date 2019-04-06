/**
 * 
 * @author Thomas
 *
 */
public class Boss extends Game {
	private int bossLife;
	private int bossRow = 3;
	private boolean bossMovedDown;
/**
 * 
 * @param gameboard
 */
	public Boss(String[][] gameboard) {
		super(gameboard);
		bossLife = 10;
	}

	@Override
	boolean levelComplete() {
		return bossLife > 0;
	}

	@Override
	void alienMoveDown() {
		if (bossMovedDown == false) {
			for (int r1 = gameboard.length - 2; r1 > 0; r1--) {
				for (int c = gameboard[r1].length - 1; c > 0; c--) {
					if (gameboard[r1][c].equals("x")) {
						gameboard[r1][c] = " ";
						gameboard[r1 + 1][c] = "x";
					}
				}
			}
			bossMovedDown = true;
		} else {
			for (int r1 = 1; r1 < 6; r1++) {
				for (int c = gameboard[r1].length - 1; c > 0; c--) {
					if (gameboard[r1][c].equals("x")) {
						gameboard[r1][c] = " ";
						gameboard[r1 - 1][c] = "x";
					}
				}
			}
			bossMovedDown = false;
		}
	}

	@Override
	void alienMove() {
		if (getAlienDirection() == true)
			for (int r1 = 1; r1 < gameboard.length - 1; r1++) {
				for (int c = gameboard[r1].length - 1; c > 0; c--) {
					if (gameboard[r1][c].equals("x")) {
						gameboard[r1][c] = " ";
						gameboard[r1][c + 1] = "x";
					}
				}
			}
		else if (getAlienDirection() == false) {
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

	@Override
	void playerBulletMovement() {

		for (int idx = 0; idx < pBullets.size(); idx++) {
			Node temp = pBullets.getNode(idx);
			if (gameboard[temp.getRow()][temp.getColumn()] == "^") {
				gameboard[temp.getRow()][temp.getColumn()] = " ";
			}
			if (gameboard[temp.getRow() - 1][temp.getColumn()].equals("_")) {
				gameboard[temp.getRow() - 1][temp.getColumn()] = "_";
				pBullets.remove(idx);
			} else if (gameboard[temp.getRow() - 1][temp.getColumn()].equals("v")
					|| gameboard[temp.getRow() - 1][temp.getColumn()].equals("=")) {
				gameboard[temp.getRow() - 1][temp.getColumn()] = " ";
				pBullets.remove(idx);
			} else if (gameboard[temp.getRow() - 1][temp.getColumn()].equals("x")) {
				pBullets.remove(idx);
				setBossLife(bossLife - 1);
			} else {
				gameboard[temp.getRow() - 1][temp.getColumn()] = "^";
				temp.setRow(temp.getRow() - 1);
			}

		}
	}

	void alienShooting() {

		for (int r = gameboard.length - 3; r >= bossRow; r--) {
			for (int c = 1; c < gameboard[r].length - 1; c++) {
				if (gameboard[r][c].equals("x")
						&& !(gameboard[r][c + 1].equals("x") && gameboard[r][c - 1].equals("x"))) {
					if (gameboard[r + 1][c].equals(" ")) {
						if (randomGenerator.nextInt() < (1 / bossLife)) {
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
	 * @return the bossLife
	 */
	public int getBossLife() {
		return bossLife;
	}

	/**
	 * @param bossLife
	 *            the bossLife to set
	 */
	public void setBossLife(int bossLife) {
		this.bossLife = bossLife;
	}

	/**
	 * @return the bossRow
	 */
	public int getBossRow() {
		return bossRow;
	}

	/**
	 * @param bossRow
	 *            the bossRow to set
	 */
	public void setBossRow(int bossRow) {
		this.bossRow = bossRow;
	}

}
