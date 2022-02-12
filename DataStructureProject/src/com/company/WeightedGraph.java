package com.company;

import java.util.*;

class Edge {
    int source;
    int destination;
    int weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}

public class WeightedGraph {
    private int verticesNumber;
    private HashMap<Integer, ArrayList<Edge>> adjacencyList;
    private HashMap<Integer, HashMap<Integer, Integer>> dijkstraHashmap;
    private ArrayList<Integer> joinNodes;

    WeightedGraph(int verticesNumber, ArrayList<Integer> vertices) {
        this.verticesNumber = verticesNumber;
        adjacencyList = new HashMap<>();
        for (int i : vertices) {
            adjacencyList.put(i, new ArrayList<Edge>());
        }
        dijkstraHashmap = new HashMap<>();
        joinNodes = new ArrayList<>();
    }

    public HashMap<Integer, ArrayList<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public ArrayList<Integer> getJoinNodes() {
        return joinNodes;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getDijkstraHashmap() {
        return dijkstraHashmap;
    }

    public void addEdge(int source, int destination, int weight) {
        Edge edge1 = new Edge(source, destination, weight);
        Edge edge2 = new Edge(destination, source, weight);
        adjacencyList.get(source).add(edge1);
        adjacencyList.get(destination).add(edge2);
    }

    void DFSUtil(int v, ArrayList<Integer> visited) {
        visited.remove(Integer.valueOf(v));
        System.out.print(v + " ");
        for (Edge n : adjacencyList.get(v)) {
            if (visited.contains(n.destination))
                DFSUtil(n.destination, visited);
        }
    }

    void DFS(int v) {
        ArrayList<Integer> visited = new ArrayList<>(adjacencyList.keySet());
        DFSUtil(v, visited);
        System.out.println();
    }

    public void getFairScore() {
        HashMap<Integer, Integer> sumHashmap = new HashMap<>();
        int sum;
        int count = 0;
        for (int k : adjacencyList.keySet()) {
            for (int i : joinNodes) {
                sum = 0;
                for (int j : joinNodes) {
                    if (i == j) {
                        continue;
                    }
                    sum += Math.abs(dijkstraHashmap.get(k).get(i) - dijkstraHashmap.get(k).get(j));
                    count++;
                }
                if (count != 0) {
                    sumHashmap.put(k, sum / count);
                    count = 0;
                }
            }
        }
        int minimum = Integer.MAX_VALUE;
        for (int i : sumHashmap.values()) {
            if (i < minimum) {
                minimum = i;
            }
        }
        for (int i : sumHashmap.keySet()) {
            if (sumHashmap.get(i) == minimum) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public int getMinimum(HashMap<Integer, Integer> distanceHashmap, HashMap<Integer, Integer> flagHashmap) {
        int min = Integer.MAX_VALUE;
        int minIndex = Integer.MAX_VALUE;
        for (int i : distanceHashmap.keySet()) {
            if (flagHashmap.get(i) == null && distanceHashmap.get(i) != 0 && distanceHashmap.get(i) < min) {
                min = distanceHashmap.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }

    public void dijkstra(int number) {
        HashMap<Integer, Integer> distanceHashmap = new HashMap<>();
        for (int i : adjacencyList.keySet()) {
            if (i == number) {
                distanceHashmap.put(i, 0);
            } else {
                distanceHashmap.put(i, Integer.MAX_VALUE);
            }
        }
        for (Edge e : adjacencyList.get(number)) {
            distanceHashmap.put(e.destination, e.weight);
        }
        HashMap<Integer, Integer> flagHashmap = new HashMap<>();
        while (true) {
            int minIndex = getMinimum(distanceHashmap, flagHashmap);
            if (minIndex == Integer.MAX_VALUE) {
                break;
            }
            flagHashmap.put(minIndex, 1);
            for (Edge e : adjacencyList.get(minIndex)) {
                distanceHashmap.put(e.destination, Math.min(distanceHashmap.get(e.destination),
                        distanceHashmap.get(minIndex) + e.weight));
            }
        }
        dijkstraHashmap.put(number, distanceHashmap);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] inputStringArray = scanner.nextLine().split(" ");
        int verticesNumber = Integer.parseInt(inputStringArray[0]);
        int edgesNumber = Integer.parseInt(inputStringArray[1]);
        ArrayList<Integer> vertices = new ArrayList<>();
        String[] verticesString = scanner.nextLine().split(" ");
        for (String s : verticesString) {
            vertices.add(Integer.parseInt(s));
        }
        WeightedGraph weightedGraph = new WeightedGraph(verticesNumber, vertices);
        for (int i = 0; i < edgesNumber; i++) {
            inputStringArray = scanner.nextLine().split(" ");
            weightedGraph.addEdge(Integer.parseInt(inputStringArray[0]), Integer.parseInt(inputStringArray[1]), Integer.parseInt(inputStringArray[2]));
        }
        while (true) {
            inputStringArray = scanner.nextLine().split(" ");
            if (inputStringArray[0].equals("test")) {
                weightedGraph.DFS(vertices.get(0));
            } else if (inputStringArray[0].equals("exit")) {
                return;
            } else if (inputStringArray[0].equals("join")) {
                for (int i = 1; i < inputStringArray.length; i++) {
                    weightedGraph.getJoinNodes().add(Integer.parseInt(inputStringArray[i]));
                }
                for (int i : weightedGraph.getAdjacencyList().keySet()) {
                    if (weightedGraph.getDijkstraHashmap().get(i) == null) {
                        weightedGraph.dijkstra(i);
                    }
                }
                weightedGraph.getFairScore();
            } else if (inputStringArray[0].equals("left")) {
                for (int i = 1; i < inputStringArray.length; i++) {
                    weightedGraph.getJoinNodes().remove(Integer.valueOf(Integer.parseInt(inputStringArray[i])));
                }
                for (int i : weightedGraph.getAdjacencyList().keySet()) {
                    if (weightedGraph.getDijkstraHashmap().get(i) == null) {
                        weightedGraph.dijkstra(i);
                    }
                }
                weightedGraph.getFairScore();
            }
        }
    }
}



