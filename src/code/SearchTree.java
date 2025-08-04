package code;

import java.util.*;

public class SearchTree {
    Node root;
    int numBottles;
    int capacity;

    public SearchTree(String init, String strategy) {
        ArrayList<Bottle> bottles = new ArrayList<Bottle>();

        String[] bottleStrings = init.split(";");

        this.numBottles = Integer.parseInt(bottleStrings[0]);
        this.capacity = Integer.parseInt(bottleStrings[1]);

        for (int i = 2; i < bottleStrings.length; i++) {
            Bottle bottle = new Bottle(capacity, new Stack<Colors>());
            String[] colors = bottleStrings[i].split(",");

            for (int j = 0; j < colors.length; j++) {
                if (!colors[j].equals("e"))
                    bottle.layers.push(Colors.valueOf(colors[j]));
            }
            Bottle temp = new Bottle(capacity, new Stack<Colors>());
            while (!bottle.layers.isEmpty())
                temp.layers.push(bottle.layers.pop());
            bottles.add(temp);
        }

        root = new Node(null, null, 0, 0, bottles);

        if (strategy.equals("GR1") || strategy.equals("AS1")) {
            root.heuristicValue = heuristic1(root);
        }

        if (strategy.equals("GR2") || strategy.equals("AS2")) {
            root.heuristicValue = heuristic2(root);
        }
    }

    public void createChildren(Node node, Set<String> visitedStates) {
        // int depthLimit = 1000;

        // if (node.depth > depthLimit) {
        //     return;
        // }

        String stateHash = getStateHash(node.bottles);

        if (visitedStates.contains(stateHash)) {
            return;
        }

        visitedStates.add(stateHash);

        if (node.checkGoal()) {
            node.isGoal = true;
            return;
        }

        for (int i = 0; i < node.bottles.size(); i++) {
            Bottle bottle1 = node.bottles.get(i);

            for (int j = 0; j < node.bottles.size(); j++) {
                Bottle bottle2 = node.bottles.get(j);

                if (!bottle1.equals(bottle2)) {
                    if (node.checkPour(bottle1, bottle2)) {
                        if (node.operator != null && node.operator.equals("pour_" + j + "_" + i)) {
                            continue;
                        }

                        ArrayList<Bottle> newBottles = new ArrayList<>();
                        for (Bottle bottle : node.bottles) {
                            Stack<Colors> newLayers = new Stack<>();
                            newLayers.addAll(bottle.layers);
                            newBottles.add(new Bottle(bottle.capacity, newLayers));
                        }

                        Node childNode = new Node(node, "pour_" + i + "_" + j, node.depth + 1,
                                node.pathCost, newBottles);

                        int x = childNode.pour(i, j);

                        childNode.pathCost += x;

                        node.children.add(childNode);

                        createChildren(childNode, visitedStates);
                    }

                }
            }
        }
    }

    public void createChildrenHeuristic(Node node, Set<String> visitedStates, String strategy) {
        int depthLimit = 1000;

        boolean isHeuristic1 = strategy.equals("GR1") || strategy.equals("AS1");

        if (node.depth > depthLimit) {
            return;
        }

        String stateHash = getStateHash(node.bottles);

        if (visitedStates.contains(stateHash)) {
            return;
        }

        visitedStates.add(stateHash);

        if (node.checkGoal()) {
            node.isGoal = true;
            return;
        }

        for (int i = 0; i < node.bottles.size(); i++) {
            Bottle bottle1 = node.bottles.get(i);

            for (int j = 0; j < node.bottles.size(); j++) {
                Bottle bottle2 = node.bottles.get(j);

                if (!bottle1.equals(bottle2)) {
                    if (node.checkPour(bottle1, bottle2)) {
                        if (node.operator != null && node.operator.equals("pour_" + j + "_" + i)) {
                            continue;
                        }

                        ArrayList<Bottle> newBottles = new ArrayList<>();
                        for (Bottle bottle : node.bottles) {
                            Stack<Colors> newLayers = new Stack<>();
                            newLayers.addAll(bottle.layers);
                            newBottles.add(new Bottle(bottle.capacity, newLayers));
                        }

                        Node childNode = new Node(node, "pour_" + i + "_" + j, node.depth + 1,
                                node.pathCost, newBottles);

                        int x = childNode.pour(i, j);

                        if (isHeuristic1) {
                            childNode.heuristicValue = heuristic1(childNode);
                        } else {
                            childNode.heuristicValue = heuristic2(childNode);
                        }

                        childNode.pathCost += x;

                        node.children.add(childNode);

                        createChildrenHeuristic(childNode, visitedStates, strategy);
                    }
                }
            }
        }
    }

    public String getStateHash(ArrayList<Bottle> bottles) {
        StringBuilder stateHash = new StringBuilder();
        for (Bottle bottle : bottles) {
            stateHash.append("|");
            for (Colors color : bottle.layers) {
                stateHash.append(color.toString());
            }
            stateHash.append("|");
        }
        return stateHash.toString();
    }

    public boolean hasGoalNode(Node node) {
        if (node.isGoal) {
            return true;
        } else {
            for (Node child : node.children) {
                if (hasGoalNode(child)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void printTree(Node node) {
        System.out.println("------------------ NODE ------------------");
        System.out.println(node);

        for (Node child : node.children) {
            printTree(child);
        }
    }

    public int heuristic1(Node node) {
        int heuristicValue = 0;

        for (Bottle bottle : node.bottles) {
            if (bottle.layers.size() > 1) {
                Colors firstColor = bottle.layers.get(0);
                for (Colors color : bottle.layers) {
                    if (!firstColor.equals(color)) {
                        heuristicValue++;
                        break;

                    }
                }
            }
        }
        return heuristicValue;
    }

    public int heuristic2(Node node) {
        int heuristicValue = 0;

        for (Bottle bottle : node.bottles) {
            if (!(bottle.layers.size() >= 1))
                continue;

            Map<Colors, Integer> colorFrequency = new HashMap<>();

            for (Colors color : bottle.layers) {
                colorFrequency.put(color, colorFrequency.getOrDefault(color, 0) + 1);
            }

            Colors dominantColor = bottle.layers.peek();
            int maxFrequency = 0;

            for (Map.Entry<Colors, Integer> entry : colorFrequency.entrySet()) {
                if (entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    dominantColor = entry.getKey();
                }
            }

            for (Colors color : bottle.layers) {
                if (!color.equals(dominantColor)) {
                    heuristicValue++;
                }
            }
        }

        return heuristicValue;
    }
}
