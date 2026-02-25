package mygame.pathFinder;

import mygame.mainLogic.GamePanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PathFinder {
    GamePanel gp;
    Node[][] node;

    PriorityQueue<Node> openList;
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, currentNode, goalNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
        openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
    }

    public void instantiateNodes() {
        node = new Node[gp.maxScreenCol][gp.maxScreenRow];
        for (int r = 0; r < gp.maxScreenRow; r++) {
            for (int c = 0; c < gp.maxScreenCol; c++) {
                node[c][r] = new Node(c, r);
            }
        }
    }

    public void resetNodes() {
        for (int r = 0; r < gp.maxScreenRow; r++) {
            for (int c = 0; c < gp.maxScreenCol; c++) {
                node[c][r].open = false;
                node[c][r].checked = false;
                node[c][r].solid = false;
                node[c][r].parent = null;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = node[startCol][startRow];
        goalNode = node[goalCol][goalRow];

        for (int r = 0; r < gp.maxScreenRow; r++) {
            for (int c = 0; c < gp.maxScreenCol; c++) {
                int tileNum = gp.tileM.mapTileNr[c][r];
                if(gp.tileM.tile[tileNum].collision) {
                    node[c][r].solid = true;
                }
            }
        }

        startNode.gCost = 0;
        startNode.hCost = calculateHCost(startNode);
        startNode.fCost = startNode.gCost + startNode.hCost;

        openList.add(startNode);
    }

    private int calculateHCost(Node n) {
        // calculate the Manhattan distance to goal
        return Math.abs(n.col - goalNode.col) + Math.abs(n.row - goalNode.row);
    }

    public boolean search() {
        while (!goalReached && step < 500 && !openList.isEmpty()) {

            currentNode = openList.poll();
            currentNode.checked = true;

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                return true;
            }

            openAdjacentNodes();
            step++;
        }
        return goalReached;
    }

    private void openAdjacentNodes() {
        int col = currentNode.col;
        int row = currentNode.row;

        if (row - 1 >= 0) updateNode(node[col][row - 1]);
        if (row + 1 < gp.maxScreenRow) updateNode(node[col][row + 1]);
        if (col - 1 >= 0) updateNode(node[col - 1][row]);
        if (col + 1 < gp.maxScreenCol) updateNode(node[col + 1][row]);
    }

    private void updateNode(Node target) {
        if (!target.solid && !target.checked) {
            // A* cost: cost from start + 1 (movement to next tile)
            int newGCost = currentNode.gCost + 1;

            if (!target.open || newGCost < target.gCost) {
                target.parent = currentNode;
                target.gCost = newGCost;
                target.hCost = calculateHCost(target);
                target.fCost = target.gCost + target.hCost;

                if (!target.open) {
                    target.open = true;
                    openList.add(target);
                }
            }
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode && current != null) {
            pathList.add(current);
            current = current.parent;
        }
        Collections.reverse(pathList);
    }
}