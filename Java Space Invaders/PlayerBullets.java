/**
 * 
 * @author Thomas
 *
 */
public class PlayerBullets {
	private Node head;
	private Node current;

	/**
	 * 
	 */
	public PlayerBullets() {
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @return
	 */
	public int size() {

		int idx = 0;
		Node current = head;
		while (current != null) {
			idx++;
			current = current.getNext();
		}
		return idx;
	}

	/**
	 * 
	 * @param row
	 * @param column
	 */
	public void add(int row, int column) {
		if (isEmpty()) {
			head = new Node(row, column);
			return;
		}
		current = head;
		Node pBullet = new Node(row, column);
		while (current.getNext() != null) {
			current = current.getNext();
		}
		current.setNext(pBullet);
	}

	/**
	 * 
	 * @param position
	 * @return
	 */
	public Node getNode(int position) {// out of bounds check
		current = head;
		for (int idx = 0; idx < position; idx++) {
			current = current.getNext();
		}
		return current;
	}

	/**
	 * 
	 * @param position
	 */
	public void remove(int position) {// ensure no out of bounds with size()
		current = head;
		if (position == 0){
			head = current.getNext();
			current = null;
		} else {
		for (int idx = 0; idx < position - 1 ; idx++) {
			current = current.getNext();
		}
		if (current.getNext().getNext() != null);
		current.setNext(current.getNext().getNext());
		}
	}

	/**
	 * 
	 */
	public void removeAll() {
		head = null;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public int find(int input) {
		current = head;
		int idx = 1;
		while (current.getRow() != input) {
			idx++;
			current = current.getNext();
		}
		return idx;
	}
}
