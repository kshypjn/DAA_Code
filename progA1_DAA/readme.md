# Maximal Layers Algorithm

## Overview
This program computes the **maximal layers** of a given set of 2D points using a **line-sweep algorithm**. The maximal layers are constructed such that:
- A point dominates another if both its `x` and `y` coordinates are greater.
- No point in a layer dominates any other point in that layer.
- Points in later layers are dominated by at least one point from earlier layers.

## I/O
### **Input Format** (from `input.txt`):
- The first line contains an integer `n`, representing the number of points.
- The next `n` lines each contain two integers `x y`, representing a point's coordinates.

### **Output Format** (to `output.txt`):
- Each layer is printed in **ascending order of y coordinates**.
- A blank line separates consecutive layers.

## Algorithm Explanation
The algorithm:
1. **Sorting**: The points are sorted in descending order of x coordinates.
2. **Layer assignment**:
   - The rightmost/first point is assigned to the first layer.
   - Subsequent points are checked against the highest y coordinate in the current layer to determine if they belong.
   - If a point has a lower y value than an existing point in the layer, it is postponed to the next layer.
   - The process continues until all points are assigned.
3. **Sorting in the layers**: Each layer is sorted in ascending order of y values before writing to the output file.

## Code Walkthrough
### **Point Class**
Represents a point in a 2D space with `x` and `y` coordinates.

```java
class Point implements Comparable<Point> {
    int x, y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int compareTo(Point other) {
        return Integer.compare(this.y, other.y);
    }
    
    @Override
    public String toString() {
        return x + " " + y;
    }
}
```

### **MaximalLayers Class**


#### **Main Method**
- Reads input points from `input.txt`.
- Calls `computeMaximalLayers()` to determine layers.
- Writes the output to `output.txt`.
- Prints the total number of operations `T`.

#### **computeMaximalLayers(List<Point> points)**
- sorts points by x-coordinate (descending).
- iteratively extracts maximal layers:
  - Adds the first point to the current layer.
  - Compares each subsequent point against the highest y-value in the layer.
  - Unfit points are deferred to the next layer.
- the process repeats until all points are assigned.

## Time Complexity
- Sorting by x coordinate: `O(n log n)`
- Processing each point: `O(n log n)` (since each point is compared using a binary search like search)
- Total Complexity: `O(n log n)`

## Example
### **Input (input.txt):**
```
5
5 2
1 3
4 1
3 5
2 4
```
### **Output (output.txt):**
```
3 5
2 4

5 2
1 3

4 1
```


