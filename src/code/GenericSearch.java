package code;

import java.util.*;

public class GenericSearch {

    public static String getPlan(Node node) {
        String plan = "";
        while (node.parent != null) {
            plan = node.operator + "," + plan;
            node = node.parent;
        }
        return plan;
    }

    public static String bfs(SearchTree tree) {

        Node cur = tree.root;
        if (tree.hasGoalNode(cur)) {

            String plan = "";
            int pathCost = 0;
            int nodesExpanded = 0;

            Queue<Node> q = new LinkedList<>();
            q.add(cur);

            while (!q.isEmpty()) {

                Node n = q.poll();
                nodesExpanded++;

                if (n.checkGoal()) {
                    plan = getPlan(n);
                    pathCost = n.pathCost;
                    break;
                }

                else {
                    for (Node m : n.children) {
                        q.add(m);
                    }
                }
            }
            return plan + ";" + pathCost + ";" + nodesExpanded;
        }

        else {
            return "NOSOLUTION";
        }
    }

    public static String dfs(SearchTree tree) {

        if (tree.hasGoalNode(tree.root)) {
            int nodesExpanded = 0;
            Stack<Node> stack = new Stack<>();
            stack.push(tree.root);
            Set<Node> visited = new HashSet<>();
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                nodesExpanded++;
                visited.add(current);
                if (current.checkGoal()) {

                    return getPlan(current) + ";" + current.pathCost + ";" + nodesExpanded;
                }
                for (Node child : current.children) {
                    if (!visited.contains(child)) {
                        stack.push(child);
                    }
                }
            }
            return "";
        } else {
            return "NOSOLUTION";
        }
    }

    public static String ids(SearchTree tree) {
        if (tree.hasGoalNode(tree.root)) {
            int depthLimit = 0;
            int nodesExpanded = 0;
            while (true) {
                Object result = dls(tree, depthLimit);
                if (!result.equals("NOSOLUTION")) {
                    if (result instanceof String) {
                        String result2 = (String) result;
                        String[] splittedResult = result2.split(";");
                        String plan = splittedResult[0];
                        String pathCost = splittedResult[1];
                        int temp = Integer.parseInt(splittedResult[2]);
                        nodesExpanded += temp;
                        return plan + ";" + pathCost + ";" + nodesExpanded;
                    } else {
                        int temp = (int) result;
                        nodesExpanded += temp;
                        depthLimit++;
                    }
                } else {
                    depthLimit++;
                }
            }

        } else {
            return "NOSOLUTION";
        }
    }

    public static String ucs(SearchTree tree) {
        if (tree.hasGoalNode(tree.root)) {
            PriorityQueue<Node> pQ = new PriorityQueue<>(Comparator.comparingInt(node -> node.pathCost));
            pQ.add(tree.root);

            int pathCost = 0;
            int nodesExpanded = 0;
            String plan = "";

            while (!pQ.isEmpty()) {
                Node current = pQ.poll();
                nodesExpanded++;

                if (current.checkGoal()) {
                    plan = getPlan(current);
                    pathCost = current.pathCost;
                    break;
                } else {
                    for (Node child : current.children) {
                        pQ.add(child);
                    }
                }
            }

            return plan + ";" + pathCost + ";" + nodesExpanded;
        } else {
            return "NOSOLUTION";
        }
    }

    public static String greedy(SearchTree tree, int i) {

        if (tree.hasGoalNode(tree.root)) {
            PriorityQueue<Node> pQ = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristicValue));
            pQ.add(tree.root);
            int nodesExpanded = 0;
            int pathCost = 0;
            String plan = "";

            while (!pQ.isEmpty()) {
                Node current = pQ.poll();
                nodesExpanded++;

                if (current.checkGoal()) {
                    pathCost = current.pathCost;
                    plan = getPlan(current);
                    break;
                } else {
                    for (Node child : current.children) {
                        pQ.add(child);
                    }
                }
            }

            return plan + ";" + pathCost + ";" + nodesExpanded;
        } else {
            return "NOSOLUTION";
        }
    }

    public static String aStar(SearchTree tree, int i) {

        if (tree.hasGoalNode(tree.root)) {

            PriorityQueue<Node> pQ = new PriorityQueue<>(
                    Comparator.comparingInt(node -> node.pathCost + node.heuristicValue));
            pQ.add(tree.root);

            int pathCost = 0;
            int nodesExpanded = 0;
            String plan = "";

            while (!pQ.isEmpty()) {
                Node current = pQ.poll();
                nodesExpanded++;

                if (current.checkGoal()) {
                    plan = getPlan(current);
                    pathCost = current.pathCost;
                    break;
                } else {
                    for (Node child : current.children) {
                        pQ.add(child);
                    }
                }
            }

            return plan + ";" + pathCost + ";" + nodesExpanded;
        } else {
            return "NOSOLUTION";
        }
    }

    public static Object dls(SearchTree tree, int limit) {
        if (tree.hasGoalNode(tree.root)) {
            int nodesExpanded = 0;
            Stack<Node> stack = new Stack<>();
            Set<Node> visited = new HashSet<>();
            stack.push(tree.root);
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                nodesExpanded++;
                visited.add(current);
                if (current.checkGoal()) {
                    return getPlan(current) + ";" + current.pathCost + ";" + nodesExpanded;
                }
                if (current.depth < limit) {
                    for (Node child : current.children) {
                        if (!visited.contains(child)) {
                            stack.push(child);
                        }
                    }
                }
            }
            return nodesExpanded;
        } else {
            return "NOSOLUTION";
        }
    }
}
