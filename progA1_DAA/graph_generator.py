import matplotlib.pyplot as plt
import csv

def generate_graph():
        with open('performance_data.csv', 'r') as file:
            csv_reader = csv.reader(file)
            next(csv_reader)  
            
            data = [(int(row[0]), int(float(row[1]))) for row in csv_reader if len(row) == 2]
        
        n_values, operation_counts = zip(*data)
        plt.plot(n_values, operation_counts, 'o-', color='green', linewidth=2, markersize=6)
        plt.xlabel('n (Number of Points)')
        plt.ylabel('T (Number of Operations)')
        plt.title('Algorithm Performance')
        plt.grid(True, linestyle='--', alpha=0.7)
        plt.savefig('graph.png', dpi=300)
        plt.show()


generate_graph()