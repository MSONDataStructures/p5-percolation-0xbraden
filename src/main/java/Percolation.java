import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufNoBottom;
    private int openSites;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be > 0");
        
        size = n;
        grid = new boolean[n][n];
        openSites = 0;
        
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNoBottom = new WeightedQuickUnionUF(n * n + 1);
        virtualTop = n * n;
        virtualBottom = n * n + 1;
    }

    private void validateIndices(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    public void open(int row, int col) {
        validateIndices(row, col);
        int r = row - 1;
        int c = col - 1;
        
        if (!grid[r][c]) {
            grid[r][c] = true;
            openSites++;
            
            int site = xyTo1D(row, col);
            
            if (row == 1) {
                uf.union(site, virtualTop);
                ufNoBottom.union(site, virtualTop);
            }
            
            if (row == size) {
                uf.union(site, virtualBottom);
            }
            
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(site, xyTo1D(row - 1, col));
                ufNoBottom.union(site, xyTo1D(row - 1, col));
            }
            if (row < size && isOpen(row + 1, col)) {
                uf.union(site, xyTo1D(row + 1, col));
                ufNoBottom.union(site, xyTo1D(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(site, xyTo1D(row, col - 1));
                ufNoBottom.union(site, xyTo1D(row, col - 1));
            }
            if (col < size && isOpen(row, col + 1)) {
                uf.union(site, xyTo1D(row, col + 1));
                ufNoBottom.union(site, xyTo1D(row, col + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) return false;
        return ufNoBottom.find(xyTo1D(row, col)) == ufNoBottom.find(virtualTop);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (size == 1) return isOpen(1, 1);
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }
}