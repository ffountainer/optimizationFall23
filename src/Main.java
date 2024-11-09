import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a vector of coefficients of supply S");
        String line = scanner.nextLine();
        String[] wholeLine = line.split(" ");
        int[] S = new int[wholeLine.length];
        int k = 0;
        for (String s : wholeLine) {
            S[k] = Integer.parseInt(s);
            k += 1;
        }
        Integer wholeSupply = 0;
        for (int i = 0; i < S.length; i++) {
            wholeSupply += S[i];
        }

        System.out.println("Enter the number of row and columns for the costs matrix divided by space");
        String dimensions = scanner.nextLine();
        String[] dim = dimensions.split(" ");
        int rows = Integer.parseInt(dim[0]);
        int columns = Integer.parseInt(dim[1]);
        System.out.println("Enter the matrix of coefficients of costs C row by row");
        int[][] costs = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            String row = scanner.nextLine();
            String[] elements = row.split(" ");
            for (int j = 0; j < columns; j++) {
                costs[i][j] = Integer.parseInt(elements[j]);
            }
        }

        System.out.println("Enter a vector of coefficients of demand D");

        String d = scanner.nextLine();
        String[] dStr = d.split(" ");

        int[] demand = new int[dStr.length];
        k = 0;
        for (String s : dStr) {
            demand[k] = Integer.parseInt(s);
            k += 1;
        }

        Integer wholeDemand = 0;
        for (int i = 0; i < demand.length; i++) {
            wholeDemand += demand[i];
        }

        System.out.println();
        System.out.println("Here is an input parameter table:");
        System.out.println();

        Integer length = Integer.toString(wholeDemand).length();

        System.out.print("Source ");
        for (int i = 0; i < columns; i++) {
            System.out.print("B" + i + " ");
            int len = 2;
            while (len < length) {
                System.out.print(" ");
                len += 1;
            }
        }
        System.out.println("Supply");


        for (int i = 0; i < rows; i++) {
            System.out.print("A" + i + "     ");
            for (int j = 0; j < columns; j++) {
                System.out.print(costs[i][j] + " ");
                int len = Integer.toString(costs[i][j]).length();
                while (len < length) {
                    System.out.print(" ");
                    len += 1;
                }
            }
            System.out.print(S[i]);
            System.out.print("\n");
        }
        System.out.print("Demand ");
        for (int i = 0; i < columns; i++) {
            System.out.print(demand[i] + " ");
            int len = Integer.toString(demand[i]).length();
            while (len < length) {
                System.out.print(" ");
                len += 1;
            }
        }
        System.out.println(wholeDemand);
        System.out.println();
        System.out.println("---------------------");
        System.out.println();
        boolean flag = true;

        if (!wholeDemand.equals(wholeSupply)) {
            System.out.println("The problem is not balanced!");
            flag = false;
        }

        int[][] costsVog = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                costsVog[i][j] = costs[i][j];
            }
        }
        int[] supplyVog = new int[rows];
        for (int i = 0; i < rows; i++) {
            supplyVog[i] = S[i];
        }

        int[] demandVog = new int[columns];
        for (int i = 0; i < columns; i++) {
            demandVog[i] = demand[i];
        }

        int[][] costsRus = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                costsRus[i][j] = costs[i][j];
            }
        }
        int[] supplyRus = new int[rows];
        for (int i = 0; i < rows; i++) {
            supplyRus[i] = S[i];
        }

        int[] demandRus = new int[columns];
        for (int i = 0; i < columns; i++) {
            demandRus[i] = demand[i];
        }

        if (flag) {
            northwest(S, rows, columns, costs, demand, wholeSupply);
            System.out.println("---------------------");
            System.out.println();
            vogel(supplyVog, rows, columns, costsVog, demandVog, wholeSupply);
            System.out.println("---------------------");
            System.out.println();
            russel(supplyRus, rows, columns, costsRus, demandRus, wholeSupply);
        }


    }

    public static void northwest(int[] supply, int rows, int columns, int[][] costs, int[] demand, Integer allSupply) {
        int[][] times = new int[rows][columns];
        int[][] oldCosts = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                oldCosts[i][j] = costs[i][j];
            }
        }

        Integer wholeDemand = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                times[i][j] = 0;
            }
        }

        for (int j = 0; j < columns; j++) {
            int i = 0;
            while (demand[j] != 0) {
                if (supply[i] >= demand[j]) {
                    times[i][j] = demand[j];
                    supply[i] = supply[i] - demand[j];
                    demand[j] = 0;
                    for (int k = i + 1; k < rows; k++) {
                        costs[k][j] = 0;
                    }
                } else {
                    demand[j] = demand[j] - supply[i];
                    times[i][j] = supply[i];
                    supply[i] = 0;
                    for (int k = j + 1; k < columns; k++) {
                        costs[i][k] = 0;
                    }
                    if (demand[j] != 0) {
                        i++;
                    }
                }
            }

            wholeDemand = 0;
            for (int m = 0; m < columns; m++) {
                wholeDemand += demand[m];
            }

        }
        Integer totalCost = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                totalCost += times[i][j] * oldCosts[i][j];
            }
        }

        int length = Integer.toString(allSupply).length();

        System.out.println("Vectors of initial basic feasible solution for North-West:");
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                System.out.print(times[m][n] + " ");
                int len = Integer.toString(times[m][n]).length();
                while (len < length) {
                    System.out.print(" ");
                    len += 1;
                }
            }
            System.out.print("\n");
        }
        System.out.println();

        if (wholeDemand != 0) {
            System.out.println("The North-West corner method is not applicable!");
            System.out.println();
        } else {
            System.out.println("Total cost with the North-West corner method: " + totalCost);
            System.out.println();
        }

    }

    public static void vogel(int[] supply, int rows, int columns, int[][] costs, int[] demand, Integer allSupply) {
        int wholeDemand = allSupply;
        int totalCost = 0;
        boolean flag = true;
        int iteration = 0;

        int[][] visited = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited[i][j] = 0;
            }
        }

        int[][] times = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                times[i][j] = 0;
            }
        }
        while (flag) {
            iteration += 1;
            Integer maxDiff = -1000000;
            String maxDiffLoc = "";
            // for rows
            for (int i = 0; i < rows; i++) {
                int min1 = 10000000;
                int min2 = 10000000;
                for (int j = 0; j < columns; j++) {
                    if (visited[i][j] != 1) {
                        if (costs[i][j] < min2) {
                            min2 = costs[i][j];
                        }
                        if (costs[i][j] < min1) {
                            min2 = min1;
                            min1 = costs[i][j];
                        }
                    }
                }
                int difference = min2 - min1;
                if (difference > maxDiff) {
                    maxDiff = difference;
                    maxDiffLoc = "r " + i;
                }
            }
            // for columns
            for (int j = 0; j < columns; j++) {
                int min1 = 10000000;
                int min2 = 10000000;
                for (int i = 0; i < rows; i++) {
                    if (visited[i][j] != 1) {
                        if (costs[i][j] < min2) {
                            min2 = costs[i][j];
                        }
                        if (costs[i][j] < min1) {
                            min2 = min1;
                            min1 = costs[i][j];
                        }
                    }
                }
                int difference = min2 - min1;
                if (difference > maxDiff) {
                    maxDiff = difference;
                    maxDiffLoc = "c " + j;
                }
            }

            String[] loc = maxDiffLoc.split(" ");
            int min = 100000000;
            int locX = -1;
            int locY = -1;
            if (loc[0].equals("r")) {
                int i = Integer.parseInt(loc[1]);
                locX = i;
                for (int j = 0; j < columns; j++) {
                    if (visited[i][j] != 1) {
                        if (costs[i][j] < min) {
                            min = costs[i][j];
                            locY = j;
                        }
                    }
                }
            }
            if (loc[0].equals("c")) {
                int j = Integer.parseInt(loc[1]);
                locY = j;
                for (int i = 0; i < rows; i++) {
                    if (visited[i][j] != 1) {
                        if (costs[i][j] < min) {
                            min = costs[i][j];
                            locX = i;
                        }
                    }
                }
            }
            int i = locX;
            int j = locY;
            if (supply[i] >= demand[j]) {
                times[i][j] = demand[j];
                supply[i] = supply[i] - demand[j];
                demand[j] = 0;
                for (int k = i + 1; k < rows; k++) {
                    visited[k][j] = 1;
                }
            } else {
                demand[j] = demand[j] - supply[i];
                times[i][j] = supply[i];
                supply[i] = 0;
                for (int k = j + 1; k < columns; k++) {
                    visited[i][k] = 1;
                }
            }
            visited[i][j] = 1;

            wholeDemand = 0;

            for (int m = 0; m < columns; m++) {
                wholeDemand += demand[m];
            }

            if (wholeDemand == 0 || iteration > rows * columns) {
                flag = false;
            }
        }

        int length = Integer.toString(allSupply).length();


        System.out.println("Vectors of initial basic feasible solution for Vogel:");
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                System.out.print(times[m][n] + " ");
                int len = Integer.toString(times[m][n]).length();
                while (len < length) {
                    System.out.print(" ");
                    len += 1;
                }
            }
            System.out.print("\n");
        }
        System.out.println();

        totalCost = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                totalCost += times[m][n] * costs[m][n];
            }
        }

        if (wholeDemand != 0 || iteration > rows * columns) {
            System.out.println("The Vogel's approximation method is not applicable!");
            System.out.println();
        } else {
            System.out.println("Total cost with the Vogel's approximation method: " + totalCost);
            System.out.println();
        }

    }

    public static void russel(int[] supply, int rows, int columns, int[][] costs, int[] demand, Integer allSupply) {
        int wholeDemand = allSupply;
        int totalCost = 0;
        boolean flag = true;

        int[][] visited = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited[i][j] = 0;
            }
        }

        int[][] times = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                times[i][j] = 0;
            }
        }


        // choose max value in each row and column
        int[] maxRows = new int[rows];
        int[] maxColumns = new int[columns];

        // for rows
        for (int i = 0; i < rows; i++) {
            int max = -1000000000;
            for (int j = 0; j < columns; j++) {
                if (costs[i][j] > max) {
                    max = costs[i][j];
                }
            }
            maxRows[i] = max;
        }

        // for columns
        for (int j = 0; j < columns; j++) {
            int max = -1000000000;
            for (int i = 0; i < rows; i++) {
                if (costs[i][j] > max) {
                    max = costs[i][j];
                }
            }
            maxColumns[j] = max;
        }

        int[][] diffs = new int[rows][columns];

        // calculate differences for each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                diffs[i][j] = costs[i][j] - (maxRows[i] + maxColumns[j]);
            }
        }


        while (flag) {
            int min = 1000000000;
            int x = -1;
            int y = -1;
            // select most negative cell
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (diffs[i][j] < min && visited[i][j] != 1) {
                        min = diffs[i][j];
                        x = i;
                        y = j;
                    }
                }
            }

            int i = x;
            int j = y;
            if (supply[i] >= demand[j]) {
                times[i][j] = demand[j];
                supply[i] = supply[i] - demand[j];
                demand[j] = 0;
                for (int k = 0; k < rows; k++) {
                    visited[k][j] = 1;
                }
            } else {
                demand[j] = demand[j] - supply[i];
                times[i][j] = supply[i];
                supply[i] = 0;
                for (int k = 0; k < columns; k++) {
                    visited[i][k] = 1;
                }
            }

            wholeDemand = 0;

            for (int m = 0; m < columns; m++) {
                wholeDemand += demand[m];
            }

            if (wholeDemand == 0) {
                flag = false;
            }

        }

        int length = Integer.toString(allSupply).length();
        System.out.println("Vectors of initial basic feasible solution for Russel:");
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                System.out.print(times[m][n] + " ");
                int len = Integer.toString(times[m][n]).length();
                while (len < length) {
                    System.out.print(" ");
                    len += 1;
                }
            }
            System.out.print("\n");
        }
        System.out.println();


        totalCost = 0;
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                totalCost += times[m][n] * costs[m][n];
            }
        }

        if (wholeDemand != 0) {
            System.out.println("The Russel's approximation method is not applicable!");
            System.out.println();
        } else {
            System.out.println("Total cost with the Russel's approximation method: " + totalCost);
            System.out.println();
        }


    }
}
