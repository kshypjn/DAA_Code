# Maximal Layers Algorithm

## Overview

This program finds groups/layers of points where no point in a group is dominated by another point in the same group. A point dominates another when it has both larger x AND y coordinates.

Think of it like this:

- Layer 1: Contains the points that aren't dominated by any other point
- Layer 2: Contains points that are only dominated by Layer 1 points
- Layer 3: Contains points dominated by points in Layers 1 or 2
- And so on...

## I/O

### Input Format (from input.txt)

- First line: an integer n, representing the number of points.
- Next n lines: each line contains two integers x y, representing a point's coordinates.

### Output Format (to output.txt)

- Points in each layer are printed in ascending order of y coordinates.
- Consecutive layers are separated by a blank line.
- The total number of operations (T) is printed to the console.

## Algorithm Explanation

1. Sorting:

   - The points are initially sorted in descending order of x coordinates.
   - Time Complexity: O(n log n).

2. Layer assignment:

   - Data Structures:
     - `layers`: a list of lists, where each sublist represents a layer of points.
     - `layerTops`: a list that tracks the current highest y coordinate of each layer.
   - Process:
     - Process each point (in descending x order) and use binary search on `layerTops` to find the first layer where the point's y coordinate is greater than the current top.
     - If a suitable layer is found:
       - Add the point to that layer.
       - Update the corresponding top value in `layerTops` with the point's y coordinate.
     - If no suitable layer is found:
       - Create a new layer for that point and add the point's y coordinate to `layerTops`.
   - Time Complexity: O(n log n) overall.

3. Sorting within each layer :
   - Each layer is sorted in ascending order of y coordinates before writing the output.
   - Time Complexity: O(n log n) overall.

## Time Complexity 
- Initial Sorting by x-coordinate: O(n log n)
- Layer Assignment (binary search for each point): O(n log n)
- Sorting within Layers: O(n log n)
- Total Complexity: O(n log n)

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

## How to Run : 
Run these lines in the terminal
```
javac MaximalLayers.java 
java MaximalLayers  
```
