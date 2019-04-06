import java.io.IOException;

/**
 * 
 * @author Thomas
 *
 */
public class Driver {
	/**
	 * 
	 * @param args
	 */

	static boolean hasLife = true;
	static int levelNumber = 1;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		while (hasLife) {
			String[][] currentLevel = Game.loadLevel("level" + levelNumber + ".txt");
			if (levelNumber < 3) {
				hasLife = playNormalLevel(currentLevel);
			} else
				hasLife = playBossLevel(currentLevel);
			levelNumber++;
			if (levelNumber == 5) {
				System.out.println("Congratulations!  You've beaten the game!");
				break;
			}

		}
	}
	
/**
 * 
 * @param currentLevel
 * @return
 * @throws IOException
 */
	private static boolean playNormalLevel(String[][] currentLevel) throws IOException {

		Game level = new Game(currentLevel);
		level.printGameboard();
		level.playGame();
		if (level.getPlayerLives() > 0 && level.getGameOver() == false) {
			System.out.println("Congratulations!  You've beaten level " + levelNumber);
			return true;
		} else {
			System.out.println("You've lost on level " + levelNumber);
			return false;
		}
	}

	/**
	 * 
	 * @param currentLevel
	 * @return
	 * @throws IOException
	 */
	private static boolean playBossLevel(String[][] currentLevel) throws IOException {

		Game bossLevel = new Boss(currentLevel);
		bossLevel.printGameboard();
		bossLevel.playGame();
		if (bossLevel.getPlayerLives() > 0 && bossLevel.getGameOver() == false) {
			System.out.println("Congratulations!  You've beaten level " + levelNumber);
			return true;
		} else {
			System.out.println("You've lost on level " + levelNumber);
			return false;
		}
	}
}
