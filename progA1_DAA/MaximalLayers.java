import java.io.*;
import java.util.*;

/*
 * point class represents a 2d point with x and y coordinates.
 * points are compared based on their y coordinate (used for sorting output).
 */
class Point implements Comparable<Point> {
    int x, y;
    
    // creates a new point with the given x and y values
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
     * compares points based on their y coordinate.
     * returns negative if this point's y is less than other's,
     * positive if greater, and zero if they are equal.
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


//   this class implements the line-sweep algorithm to compute the maximal layers.
//  it uses a multi staircase structure. for each new point (swept in descending x order),it uses binary search (which takes o(log n) time per point) over the current layers (tracked by the highest y value in each layer) to decide which layer the point belongs to.
// if no current layer is suitable, a new layer is created.

public class MaximalLayers {
    // global counter t for operations (comparisons and arithmetic)
    static int T = 0;
    
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
             PrintWriter pw = new PrintWriter(new FileWriter("output.txt"))) {
            
            // read number of points from the first line
            int n = Integer.parseInt(br.readLine());
            List<Point> points = new ArrayList<>();
            
            // read all points from the input file
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split(" ");
                points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
            
            // reset operation counter before starting the algorithm
            T = 0;
            
            // sort points by x coordinate in descending order
            Collections.sort(points, Comparator.comparingInt((Point p) -> p.x).reversed());
            T += points.size() * Math.log(points.size());
            
            // layers - store points in each max layer
            List<List<Point>> layers = new ArrayList<>();
            // layerTops to track the highest y-coordinate in each layer
            List<Integer> layerTops = new ArrayList<>();
            
            // process each point in one sweep
            for (Point p : points) {
                // binary search for the first layer where p.y > current layer top.
                // search for the smallest index such that p.y is greater than layertops.get(index)
                int lo = 0, hi = layerTops.size();
                // create a new layer if no layer qualifies
                int pos = hi;
                while (lo < hi) {
                    int mid = (lo + hi) / 2;
                    // count the binary search comparison - increment operation count
                    T++; 
                    if (p.y > layerTops.get(mid)) {
                        pos = mid;
                        hi = mid;
                    } else {
                        lo = mid + 1;
                    }
                }
                
                if (pos < layerTops.size()) {
                    // add point p into the found layer and update that layer's top
                    layers.get(pos).add(p);
                    layerTops.set(pos, p.y);
                } else {
                    // if no layer qualifies, create a new layer with point p
                    List<Point> newLayer = new ArrayList<>();
                    newLayer.add(p);
                    layers.add(newLayer);
                    layerTops.add(p.y);
                }
            }
            
            // before output, sort the points in each layer by ascending y coordinate
            for (int i = 0; i < layers.size(); i++) {
                if (i > 0) {
                    pw.println();
                }
                Collections.sort(layers.get(i));
                for (Point pt : layers.get(i)) {
                    pw.println(pt);
                }
            }
            pw.println();
            System.out.println("total operations (t) for n=" + n + ": " + T);
        }

        analyzePerformance();
    }
    
    // analyzes algorithm performance for different input sizes
    private static void analyzePerformance() {
        try (PrintWriter dataWriter = new PrintWriter(new FileWriter("performance_data.csv"))) {
            dataWriter.println("n,operations");
            
            // test with input sizes from 10 to 1000 points
            for (int n = 10; n <= 1000; n += 50) {
                List<Point> randomPoints = generateRandomPoints(n);
                
                // reset operation counter for this test
                T = 0;
                
                // sort points by descending x coordinate
                Collections.sort(randomPoints, Comparator.comparingInt((Point p) -> p.x).reversed());
                T += randomPoints.size() * Math.log(randomPoints.size());
                
                // compute maximal layers using the new method
                List<List<Point>> layers = new ArrayList<>();
                List<Integer> layerTops = new ArrayList<>();
                for (Point p : randomPoints) {
                    int lo = 0, hi = layerTops.size();
                    int pos = hi;
                    while (lo < hi) {
                        int mid = (lo + hi) / 2;
                        T++;
                        if (p.y > layerTops.get(mid)) {
                            pos = mid;
                            hi = mid;
                        } else {
                            lo = mid + 1;
                        }
                    }
                    
                    if (pos < layerTops.size()) {
                        layers.get(pos).add(p);
                        layerTops.set(pos, p.y);
                    } else {
                        List<Point> newLayer = new ArrayList<>();
                        newLayer.add(p);
                        layers.add(newLayer);
                        layerTops.add(p.y);
                    }
                }
                
                dataWriter.println(n + "," + T);
                System.out.println("input size n = " + n + ", operations t = " + T);
            }
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
    }
    
    // generates n random points with coordinates in the range [0, 1000]
    private static List<Point> generateRandomPoints(int n) {
        List<Point> points = new ArrayList<>();
        Random random = new Random(42); 
        
        for (int i = 0; i < n; i++) {
            int x = random.nextInt(1001);
            int y = random.nextInt(1001);
            points.add(new Point(x, y));
        }
        
        return points;
    }
}
