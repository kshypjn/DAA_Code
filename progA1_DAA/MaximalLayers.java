import java.io.*;
import java.util.*;

/**
 * point class represents a 2d point with x and y coordinates.
 * points can be compared based on their y-coordinate, which is used 
 * when we need to sort points within each maximal layer.
 */
class Point implements Comparable<Point> {
    int x, y;
    // creates a new point with the given coordinates
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * compares points based on their y coordinate.
     * used when sorting points within a layer for the output.
     * return negative if this point's y is less than other's y, positive if greater, zero if equal
     */
    @Override
    public int compareTo(Point other) {
        return Integer.compare(this.y, other.y);
    }
    // returns the string representation of this point as "x y"
    @Override
    public String toString() {
        return x + " " + y;
    }
}

/**
 * maximallayers class implements an algorithm to find maximal layers in a set s of 2d points.
 * maximal layer definition:
 * - a point p dominates point q if both x and y coordinates of p are greater than q
 * - points in the first layer are not dominated by any other point
 * - points in the second layer are only dominated by points in the first layer
 * - this pattern continues for all subsequent layers
 */
public class MaximalLayers {
    // counter t for operations
    static int T = 0;
    
    public static void main(String[] args) throws IOException {
        // use try-with-resources to automatically close input and output files
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
             PrintWriter pw = new PrintWriter(new FileWriter("output.txt"))) {
            
            // read number of points from first line
            int n = Integer.parseInt(br.readLine());
            List<Point> points = new ArrayList<>();

            // read all points from input file
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split(" ");
                points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
            
            // reset operation counter before starting the algorithm
            T = 0;
            
            // compute maximal layers using our algorithm
            List<List<Point>> layers = computeMaximalLayers(points);
            
            // write each layer to the output file
            for (int i = 0; i < layers.size(); i++) {
                if (i > 0) {
                    pw.println();
                }
                // sort points in ascending order (each layer by y coordinate)
                Collections.sort(layers.get(i));
                
                // print all points in current layer
                for (Point p : layers.get(i)) {
                    pw.println(p);
                }
            }
            pw.println();
            System.out.println("total operations (t) for n=" + n + ": " + T);
        }
        
        // analyze performance using try-with-resources in the method
        analyzePerformance();
    }
    
    /**
     * computes maximal layers from a set of points using an efficient algorithm.
     * 
     * how the algorithm works:
     * 1. sort all points by x-coordinate in descending order
     * 2. for each layer, take the first point (highest x)
     * 3. add other points to the current layer only if they have higher y-values
     *    than any point already in the layer (this ensures no dominance)
     * 4. repeat until all points are assigned to layers
     * 
     * @return list of maximal layers, where each layer is a list of points
     */
    private static List<List<Point>> computeMaximalLayers(List<Point> points) {
        List<List<Point>> layers = new ArrayList<>();
        // empty input case handled
        if (points.isEmpty()) {
            return layers;
        }
        
        // sort points by x coordinate (highest to lowest)
        Collections.sort(points, Comparator.comparingInt((Point p) -> p.x).reversed());
        T += points.size() * Math.log(points.size()); 
        
        // creating a working copy of points
        List<Point> remainingPoints = new ArrayList<>(points);
        
        // process layers one by one until all points are assigned
        while (!remainingPoints.isEmpty()) {
            List<Point> currentLayer = new ArrayList<>();
            List<Point> pointsForNextLayer = new ArrayList<>();
            
            // the point with highest x value always belongs to current layer  (it cannot be dominated by any remaining point)
            Point firstPoint = remainingPoints.get(0);
            currentLayer.add(firstPoint);
            int highestY = firstPoint.y;
            
            // process the remaining points
            for (int i = 1; i < remainingPoints.size(); i++) {
                Point currentPoint = remainingPoints.get(i);
                // add this to count of operations
                T++; 
                
                // if this point has a higher y value than any in the current layer, it belongs in this layer because it cannot be dominated
                if (currentPoint.y > highestY) {
                    currentLayer.add(currentPoint);
                    highestY = currentPoint.y; // update highest y value in this layer
                } else {
                    // else this point is dominated and goes to next layer
                    pointsForNextLayer.add(currentPoint);
                }
            }
            
            // add the current layer to the result and continue with remaining points
            layers.add(currentLayer);
            remainingPoints = pointsForNextLayer;
        }
        
        return layers;
    }
    
    // analyzes algorithm performance for different input sizes.

    private static void analyzePerformance() {
        
        try (PrintWriter dataWriter = new PrintWriter(new FileWriter("performance_data.csv"))) {
            dataWriter.println("n,operations");
            
            // test with different input sizes from 10 to 1000 points
            for (int n = 10; n <= 1000; n += 50) {
                // generate a test set with n random points
                List<Point> randomPoints = generateRandomPoints(n);
                
                // reset operation counter for this test
                T = 0;
                
                // run the algorithm and measure operations
                computeMaximalLayers(randomPoints);
    
                dataWriter.println(n + "," + T);
                System.out.println("input size n = " + n + ", operations t = " + T);
            }
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
    }
    
    // random points generator
    private static List<Point> generateRandomPoints(int n) {
        List<Point> points = new ArrayList<>();
        Random random = new Random(42); 
        
        for (int i = 0; i < n; i++) {
            // random coordinates in range [0, 1000]
            int x = random.nextInt(1001);
            int y = random.nextInt(1001);
            points.add(new Point(x, y));
        }
        
        return points;
    }
}
