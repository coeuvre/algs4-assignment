import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int topVirtualSizeIndex;
    private final int bottomVirtualSizeIndex;
    private final int n;
    private final WeightedQuickUnionUF ufForPercolation;
    private final WeightedQuickUnionUF ufForFull;
    private boolean[] open;
    private int openSitesCount;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        this.n = n;

        // 2 extra virtual sites
        int count = n * n + 2;
        ufForPercolation = new WeightedQuickUnionUF(count);
        ufForFull = new WeightedQuickUnionUF(count);
        open = new boolean[count];

        for (int i = 0; i < count; ++i) {
            open[i] = false;
        }
        openSitesCount = 0;

        topVirtualSizeIndex = 0;
        bottomVirtualSizeIndex = count - 1;
    }

    private void validate(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 1 && row <= n && col >= 1 && col <= n;
    }

    private int index(int row, int col) {
        return (row - 1) * n + col;
    }

    private void connectAdjacentOpenSites(int row, int col) {
        connectOpenSites(row, col, row - 1, col);
        connectOpenSites(row, col, row + 1, col);
        connectOpenSites(row, col, row, col - 1);
        connectOpenSites(row, col, row, col + 1);
    }

    private void connectOpenSites(int row, int col, int otherRow, int otherCol) {
        if (isValidCoordinate(otherRow, otherCol) && isOpen(otherRow, otherCol)) {
            ufForPercolation.union(index(row, col), index(otherRow, otherCol));
            ufForFull.union(index(row, col), index(otherRow, otherCol));
        } else if (otherRow == 0) {
            ufForPercolation.union(index(row, col), topVirtualSizeIndex);
            ufForFull.union(index(row, col), topVirtualSizeIndex);
        } else if (otherRow == n + 1) {
            ufForPercolation.union(index(row, col), bottomVirtualSizeIndex);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (!isOpen(row, col)) {
            open[index(row, col)] = true;
            ++openSitesCount;
            connectAdjacentOpenSites(row, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return open[index(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        return isOpen(row, col) && ufForFull.connected(topVirtualSizeIndex, index(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufForPercolation.connected(topVirtualSizeIndex, bottomVirtualSizeIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        percolation.open(1, 1);
        System.out.println(percolation.percolates());
    }
}
