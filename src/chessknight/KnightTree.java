package chessknight;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * Class represents all possible moves of chess knights on the chess table and
 * methods for find the shortest path between two knights
 * 
 * @author sergiy.bezzub
 *
 */
public class KnightTree {
	static class KnightNode {
		private Set<KnightNode> availableMoves = new HashSet<KnightNode>();;
		private String x;
		private String y;
		private boolean isUsed = false;
		private KnightNode root;

		public KnightNode(String x, String y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(KnightNode node) {

			return (this.x.equals(node.x) && this.y.equals(node.y));
		}

		public int hashCode() {

			return (this.x + "-" + this.y).hashCode();
		}

		public String toString() {
			return "[" + x + "-" + y + "]";
		}
	}

	private String[] Y = { "8", "7", "6", "5", "4", "3", "2", "1" };
	private String[] X = { "a", "b", "c", "d", "e", "f", "g", "h" };
	private int countX = 8;
	private int countY = 8;

	private Set<KnightNode> nodes = new HashSet<KnightNode>();

	public KnightTree() {
		resetTree();
	}

	public void printInfo() {
		int size = nodes.size();
		String info = "Size=" + size;
		int i = 0;
		for (KnightNode node : nodes) {
			info += node;
			info += "\n--" + i + "--\n";
		}
		System.out.println(info);
	}

	public void printPath(KnightNode node, String prefix) {

		KnightNode tmp = node;
		System.out.println(prefix);
		do {
			System.out.println("[" + tmp.x + "-" + tmp.y + "],");
			tmp = findNode(tmp.root.x, tmp.root.y);
		} while (tmp.root != null);
		System.out.println("[" + tmp.x + "-" + tmp.y + "],");
	}

	/**
	 * Search path by Depth-first search algorithm
	 * 
	 * @param startNode
	 * @param finishNode
	 * @return
	 */
	public KnightNode getPathByDFS(KnightNode startNode,
			KnightNode finishNode) {
		if (startNode == null) {
			throw new IllegalArgumentException("start node shouldn't be null!");
		}

		if (finishNode == null) {
			throw new IllegalArgumentException("finish node shouldn't be null!");
		}

		Deque<KnightNode> stack = new LinkedList<KnightNode>();

		// step 0
		startNode.isUsed = true;
		stack.add(startNode);

		nodes.remove(startNode);
		nodes.add(startNode);

		while (!stack.isEmpty()) {
			// get next node from head of queue
			KnightNode tmpNode = stack.getLast();
			Set<KnightNode> childNodes = tmpNode.availableMoves;
			for (KnightNode tmpChild : childNodes) {
				tmpChild = findNode(tmpChild.x, tmpChild.y);
				// check if we have already been here
				if (!tmpChild.isUsed) {
					tmpChild.root = tmpNode;
					tmpChild.isUsed = true;
					stack.add(tmpChild);

					if (tmpChild.equals(finishNode)) {
						// we have found our short path
						return tmpChild;
					}
				}
			}

		}

		// path is not exist
		return null;
	}

	/**
	 * Search path by Breadth-first search algorithm
	 * 
	 * @param startNode
	 * @param finishNode
	 * @return
	 */
	public KnightNode getPathByBFS(KnightNode startNode,
			KnightNode finishNode) {
		if (startNode == null) {
			throw new IllegalArgumentException("start node shouldn't be null!");
		}

		if (finishNode == null) {
			throw new IllegalArgumentException("finish node shouldn't be null!");
		}

		Queue<KnightNode> queue = new LinkedList<KnightNode>();

		// step 0
		startNode.isUsed = true;
		queue.add(startNode);
	
		while (!queue.isEmpty()) {
			// get next node from head of queue
			KnightNode tmpNode = queue.remove();
			Set<KnightNode> childNodes = tmpNode.availableMoves;
			for (KnightNode tmpChild : childNodes) {
				tmpChild = findNode(tmpChild.x, tmpChild.y);
				// check if we have already been here
				if (!tmpChild.isUsed) {
					tmpChild.root = tmpNode;
					tmpChild.isUsed = true;
					queue.add(tmpChild);

					if (tmpChild.equals(finishNode)) {
						// we have found our short path
						return tmpChild;
					}
				}
			}

		}

		// path is not exist
		return null;
	}

	public KnightNode findNode(String x, String y) {
		for (KnightNode tmpNode : nodes) {
			if (tmpNode.x.equals(x) && tmpNode.y.equals(y)) {
				return tmpNode;
			}
		}
		return null;
	}

	private void resetTree() {

		if (nodes.size() > 0) {
			nodes.clear();
		}

		for (int j = 0; j < countY; j++) {
			for (int i = 0; i < countX; i++) {
				nodes.add(populateChildNodes(i, j));
			}
		}
	}

	/**
	 * Populate chess tree
	 * 
	 * @param parentX
	 * @param parentY
	 * @return
	 */
	private KnightNode populateChildNodes(int parentX, int parentY) {

		KnightNode nodeTmp = new KnightNode(X[parentX], Y[parentY]);
		// 1
		int x = parentX + 2;
		int y = parentY + 1;
		if (bottomRightOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 2
		y = parentY - 1;
		if (topRightOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 3 top-left check
		x = parentX - 2;
		y = parentY - 1;
		if (topLeftOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 4
		y = parentY + 1;
		if (bottomLeftOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 5
		x = parentX + 1;
		y = parentY + 2;
		if (bottomRightOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 6
		x = parentX - 1;
		if (bottomLeftOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 7
		x = parentX + 1;
		y = parentY - 2;
		if (topRightOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}
		// 8
		x = parentX - 1;
		if (topLeftOk(x, y)) {
			nodeTmp.availableMoves.add(new KnightNode(X[x], Y[y]));
		}

		return nodeTmp;
	}

	private boolean topLeftOk(int i, int j) {
		return (i >= 0 && j >= 0);
	}

	private boolean topRightOk(int i, int j) {
		return (i < countX && j >= 0);
	}

	private boolean bottomRightOk(int i, int j) {
		return (i < countX && j < countY);
	}

	private boolean bottomLeftOk(int i, int j) {
		return (i >= 0 && j < countY);
	}

	@Test
	public void testPositive() {
		KnightTree tree = new KnightTree();

		// application will find path between start and finish cells
		KnightTree.KnightNode start = tree.findNode("h", "8");
		KnightTree.KnightNode finish = tree.findNode("a", "1");

		// find path by BFS
		KnightTree.KnightNode resultNode = tree.getPathByBFS(start, finish);
		Assert.assertNotNull(resultNode);

		// print result path
		tree.printPath(resultNode, "BFS between:" + finish + " and " + start);
		tree.resetTree();

		// find path by DBFS
		resultNode = tree.getPathByDFS(start, finish);
		Assert.assertNotNull(resultNode);

		// print result path
		tree.printPath(resultNode, "\nDSF between:" + finish + " and " + start);
	}
}
