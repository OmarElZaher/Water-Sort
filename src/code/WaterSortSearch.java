package code;

import java.util.*;

public class WaterSortSearch extends GenericSearch {

    public ArrayList<Bottle> bottles;

    public static String solve(String initialState, String strategy, boolean visualize) {
        SearchTree st = new SearchTree(initialState, strategy);

        switch (strategy) {
            case "BF":
                st.createChildren(st.root, new HashSet<>());
                return bfs(st);

            case "DF":
                st.createChildren(st.root, new HashSet<>());
                return dfs(st);

            case "ID":
                st.createChildren(st.root, new HashSet<>());
                return ids(st);

            case "UC":
                st.createChildren(st.root, new HashSet<>());
                return ucs(st);

            case "GR1":
                st.createChildrenHeuristic(st.root, new HashSet<>(), "GR1");
                return greedy(st, 1);

            case "GR2":
                st.createChildrenHeuristic(st.root, new HashSet<>(), "GR2");
                return greedy(st, 2);

            case "AS1":
                st.createChildrenHeuristic(st.root, new HashSet<>(), "AS1");
                return aStar(st, 1);

            case "AS2":
                st.createChildrenHeuristic(st.root, new HashSet<>(), "AS2");
                return aStar(st, 2);
        }

        return "NOSOLUTION";
    }
}