package code;

import java.util.Stack;

public class Bottle {
    public int capacity;
    public Stack<Colors> layers;

    public Bottle(int capacity, Stack<Colors> layers) {
        this.capacity = capacity;
        this.layers = layers;
    }

    @Override
    public String toString() {
        String result = "\n         Bottle {\n";
        result += "             >> Capacity: " + capacity + ",\n";
        result += "             >> Layers: [";

        for (int i = 0; i < layers.size(); i++) {
            if (i < layers.size() - 1)
                result += layers.get(i) + ",";
            else
                result += layers.get(i);
        }

        result += "]\n         }\n";

        return result;
    }
}
