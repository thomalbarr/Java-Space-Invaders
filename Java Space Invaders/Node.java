/**
 * 
 * @author Thomas
 *
 */
public class Node {
	private Node next;
	private int pbullet[];

	/**
	 * 
	 * @param row
	 * @param column
	 */
	public Node(int row, int column) {
		pbullet = new int [2];
		pbullet[0] = row;
		pbullet[1] = column;
	}

	/**
	 * 
	 * @return
	 */
	public Node getNext() {
		return next;
	}

	/**
	 * 
	 * @param next
	 */
	public void setNext(Node next) {
		this.next = next;
	}

	/**
	 * 
	 * @return
	 */
	public int getRow() {
		return pbullet[0];
	}

	/**
	 * 
	 * @param row
	 */
	public void setRow(int row) {
		pbullet[0] = row;
	}

	/**
	 * 
	 * @return
	 */
	public int getColumn() {
		return pbullet[1];
	}

	/**
	 * 
	 * @param column
	 */
	public void setColumn(int column) {
		pbullet[1] = column;
	}
}
