import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final int trialsCount;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be > 0");
        }
        
        trialsCount = trials;
        thresholds = new double[trials];
        
        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            int opened = 0;
            
            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    opened++;
                }
            }
            
            thresholds[t] = (double) opened / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trialsCount));
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trialsCount));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java PercolationStats n trials");
            return;
        }
        
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(n, trials);
        
        System.out.printf("mean                    = %f%n", stats.mean());
        System.out.printf("stddev                  = %f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]%n", 
                         stats.confidenceLo(), stats.confidenceHi());
    }
}