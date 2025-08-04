package code;

import java.util.*;

public class Node {

    public Node parent;
    public ArrayList<Node> children;
    public Object operator;
    public int depth;
    public int pathCost;
    public ArrayList<Bottle> bottles;
    public boolean isGoal;
    public int heuristicValue;

    public Node(Node parent, Object operator, int depth, int pathCost, ArrayList<Bottle> bottles) {
        this.parent = parent;
        this.children = new ArrayList<Node>();
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
        this.bottles = bottles;
        this.isGoal = false;
        this.heuristicValue = 0;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public boolean checkPour(Bottle bottle1, Bottle bottle2) {
        if (!((bottle1.layers.size() == 1) && (bottle2.layers.size() == 0))) {
            if (bottle1.layers.size() > 0) {
                if (bottle2.layers.size() < bottle2.capacity) {
                    if (bottle2.layers.empty()) {
                        return true;
                    } else {
                        if (bottle2.layers.peek().equals(bottle1.layers.peek())) {
                            return true;
                        } else {
                            return false;
                        }

                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int pour(int i, int j) {
        int x = 0;
        if (this.bottles.get(i) != null && this.bottles.get(j) != null) {
            Bottle bottle1 = bottles.get(i);
            Bottle bottle2 = bottles.get(j);

            while (checkPour(bottle1, bottle2)) {
                bottle2.layers.push(bottle1.layers.pop());
                x++;
            }
        }
        return x;
    }

    public boolean checkGoal() {
        boolean firstCheck = true;
        boolean secondCheck = true;

        for (Bottle bottle : this.bottles) {
            if (!bottle.layers.isEmpty()) {
                Colors firstColor = bottle.layers.peek();
                for (Colors color : bottle.layers) {
                    if (!color.equals(firstColor)) {
                        firstCheck = false;
                    }
                }
            }
        }

        if (firstCheck) {
            for (Bottle bottle : this.bottles) {
                if (!bottle.layers.isEmpty()) {
                    Stack<Colors> layers = bottle.layers;
                    Colors currentColor = layers.peek();
                    for (int i = 1; i < layers.size(); i++) {
                        if (!layers.get(i).equals(currentColor)) {
                            secondCheck = false;
                            break;
                        }
                    }
                }
            }

            if (secondCheck) {
                this.isGoal = true;
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Node {\n" +
                "   • Parent Depth: " + (parent == null ? "This Node is the Root Node,\n" : parent.depth + ",\n") +
                "   • Children: " + children.size() + ",\n" +
                "   • Operator: '" + operator + "',\n" +
                "   • Depth: " + depth + ",\n" +
                "   • Path Cost: " + pathCost + ",\n" +
                "   • Is Goal? " + isGoal + ",\n" +
                "   • Bottles: " + bottles + ",\n" +
                "   • Heuristic Value: " + heuristicValue + "\n" +
                '}';
    }
}
