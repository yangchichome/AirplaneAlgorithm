package base;

import java.util.*;

public class Dijkstra {

  public static List<String> findShortestPath(List<Flight> flights, String source, String destination) {

    // Initialize variables
    Map<String, Integer> distances = new HashMap<>();  // Stores distances from source to each airport
    Map<String, String> previous = new HashMap<>();    // Stores previous node in shortest path for each airport
    Set<String> settled = new HashSet<>();              // Set of processed airports
    PriorityQueue<AirportNode> pq = new PriorityQueue<>();  // Priority queue for unprocessed airports

    // Initialize distances and previous for all airports
    for (Flight flight : flights) {
      distances.put(flight.from, Integer.MAX_VALUE);
      distances.put(flight.to, Integer.MAX_VALUE);
      previous.put(flight.from, null);
      previous.put(flight.to, null);
    }

    // Set source distance to 0 and add it to the queue
    distances.put(source, 0);
    pq.add(new AirportNode(source, 0));

    // Dijkstra's algorithm loop
    while (!pq.isEmpty()) {
      AirportNode currentNode = pq.poll();
      String currentAirport = currentNode.airport;
      settled.add(currentAirport);

      // Check if reached destination
      if (currentAirport.equals(destination)) {
        return reconstructPath(previous, source, destination);
      }

      // Relax edges for neighboring airports
      for (Flight flight : flights) {
        if (flight.from.equals(currentAirport) && !settled.contains(flight.to)) {
          int newDistance = distances.get(currentAirport) + flight.distance;
          if (newDistance < distances.get(flight.to)) {
            distances.put(flight.to, newDistance);
            previous.put(flight.to, currentAirport);
            pq.add(new AirportNode(flight.to, newDistance));
          }
        }
      }
    }

    // No path found
    return Collections.emptyList();
  }

  // Reconstructs the shortest path from previous nodes
  private static List<String> reconstructPath(Map<String, String> previous, String source, String destination) {
    List<String> path = new ArrayList<>();
    path.add(destination);
    String current = destination;
    while (previous.get(current) != null) {
      current = previous.get(current);
      path.add(0, current);
    }

    if (!path.get(0).equals(source)) { // Check if path exists
      return Collections.emptyList();
    }

    return path;
  }

  // Helper class for storing airport and its distance in priority queue
  private static class AirportNode implements Comparable<AirportNode> {
    String airport;
    int distance;

    public AirportNode(String airport, int distance) {
      this.airport = airport;
      this.distance = distance;
    }

    @Override
    public int compareTo(AirportNode other) {
      return Integer.compare(distance, other.distance);
    }
  }
}
