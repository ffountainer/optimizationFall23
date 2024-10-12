import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write Max for maximization problem OR Min for minimization");
        String type = scanner.nextLine();

        System.out.println("Enter vector C in one line:");
        String line = scanner.nextLine();
        String[] wholeLine = line.split(" ");
        ArrayList<Double> C = new ArrayList<>();
        for (String s : wholeLine) {
            C.add(Double.parseDouble(s));
        }

        if (type.equals("Min")) {
            for (int i = 0; i < C.size(); i++) {
                C.set(i, -C.get(i));
            }
        }

        Matrix A = new Matrix();

        System.out.println("Enter b in one line:");
        String lineB = scanner.nextLine();
        String[] wholeLineB = lineB.split(" ");
        ArrayList<Double> b = new ArrayList<>();
        b.add(0.0);
        for (String s : wholeLineB) {
            b.add(Double.parseDouble(s));
        }

        Integer accuracy = 0;
        System.out.println("Enter the approximation accuracy (the number of digits after the decimal point):");
        String ac = scanner.nextLine();
        accuracy = Integer.parseInt(ac);

        System.out.println("Your problem:");
        if (type.equals("Max")) {
            System.out.print("Maximise z = ");
        }
        if (type.equals("Min")) {
            System.out.print("Minimise z = ");
        }
        Integer counter = 1;
        for (int i = 0; i < C.size(); i++) {
            if (type.equals("Max")) {
                System.out.print(C.get(i) + "*x" + counter);
            }
            if (type.equals("Min")) {
                System.out.print(-C.get(i) + "*x" + counter);
            }
            counter += 1;
            if (type.equals("Max")) {
                if (i != C.size() - 1 && C.get(i + 1) >= 0) {
                    System.out.print(" + ");
                }
                if (i != C.size() - 1 && C.get(i + 1) < 0) {
                    System.out.print(" ");
                }
            }
            if (type.equals("Min")) {
                if (i != C.size() - 1 && -C.get(i + 1) >= 0) {
                    System.out.print(" + ");
                }
                if (i != C.size() - 1 && -C.get(i + 1) < 0) {
                    System.out.print(" ");
                }
            }


        }
        System.out.println();
        System.out.println("Subject to the constraints: ");
        for (int i = 0; i < A.numberOfRows; i++) {
            counter = 1;
            for (int j = 0; j < C.size(); j++) {
                System.out.print(A.matrix.get(i).get(j) + "*x" + counter);
                counter += 1;
                if (j != C.size() - 1 && A.matrix.get(i).get(j + 1) >= 0) {
                    System.out.print(" + ");
                }
                if (j != C.size() - 1 && A.matrix.get(i).get(j + 1) < 0) {
                    System.out.print(" ");
                }
            }
            System.out.print(" <= " + b.get(i + 1));
            System.out.println();

        }
        System.out.println();

        ArrayList<Double> ans = simplex(C, A, b);
        if (ans.isEmpty()) {
            System.out.println("The method is not applicable!");
        }
        else {
            if (type.equals("Max")) {
                Integer c = 1;
                for (int i = 0; i < ans.size() - 1; i++) {
                    double val = ans.get(i);
                    val = Math.round(val * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                    System.out.println(String.format("x" + c + " = "  + "%." + accuracy + "f", val));
                    c += 1;
                }
                double func = ans.get(ans.size() - 1);
                func = Math.round(func * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                System.out.println(String.format("Maximum value is " + "%." + accuracy + "f", func));
            }
            if (type.equals("Min")) {
                Integer c = 1;
                for (int i = 0; i < ans.size() - 1; i++) {
                    double val = ans.get(i);
                    val = Math.round(val * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                    System.out.println(String.format("x" + c + " = "  + "%." + accuracy + "f", val));
                    c += 1;
                }
                double func = -ans.get(ans.size() - 1);
                func = Math.round(func * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                System.out.println(String.format("Minimum value is " + "%." + accuracy + "f", func));
            }
        }
    }

    public static ArrayList<Double> simplex(ArrayList<Double> C, Matrix A, ArrayList<Double> b) {
        ArrayList<Double> answer = new ArrayList<>();
        Double prevResult = 0.0;
        Double epsilon = 0.01;
        ArrayList<Integer> nonBasic = new ArrayList<>();

        Integer numOfVariables = C.size() + A.getNumberOfRows();
        for (int i = 0; i < numOfVariables; i++) {
            nonBasic.add(i + 1);
        }

        ArrayList<Integer> basic = new ArrayList<>();
        for (int i = 0; i < A.getNumberOfRows(); i++) {
            basic.add(C.size() + i + 1);
        }

        ArrayList<ArrayList<Double>> workingTable = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        for (int i = 0; i < C.size(); i++) {
            firstRow.add(-C.get(i));
        }
        for (int i = 0; i < A.getNumberOfRows(); i++) {
            firstRow.add(0.0);
        }
        firstRow.add(0.0);
        workingTable.add(firstRow);
        int counter = 0;
        for (int i = 0; i < A.getNumberOfRows(); i++) {
            ArrayList<Double> array = new ArrayList<>();
            for (int j = 0; j < C.size(); j++) {
                array.add(A.matrix.get(i).get(j));
            }
            for (int j = 0; j < A.numberOfRows; j++) {
                if (counter == j) {
                    array.add(1.0);
                }
                else {
                    array.add(0.0);
                }
            }
            counter += 1;
            array.add(b.get(i + 1));
            workingTable.add(array);
        }

        Integer numberOfColumns = workingTable.get(0).size();
        Integer numberOfRows = A.getNumberOfRows() + 1;

        while (checkCoefficients(workingTable.get(0))) {


            answer.clear();
            double function = prevResult;
            Double minNumber = 1000000.0;
            Integer indexColumn = 0;
            for (int i = 0; i < workingTable.get(0).size(); i++) {
                if (workingTable.get(0).get(i) <= minNumber && workingTable.get(0).get(i) < 0) {
                    minNumber = workingTable.get(0).get(i);
                    indexColumn = i;
                }
            }
            Double minRatio = 10000000.0;
            Integer indexRow = 0;
            Boolean flag = false;
            for (int i = 1; i < b.size(); i++) {
                if (workingTable.get(i).get(indexColumn) > 0) {
                    flag = true;
                    Double ratio = workingTable.get(i).get(numberOfColumns - 1) / workingTable.get(i).get(indexColumn);
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        indexRow = i;
                    }
                }
            }
            if (!flag) {
                System.out.println("Unbounded!");
                return answer;
            }

            basic.set(indexRow - 1, indexColumn + 1);

            Double ratioValue = workingTable.get(indexRow).get(indexColumn);
            for (int i = 0; i < numberOfColumns; i++) {
                workingTable.get(indexRow).set(i, workingTable.get(indexRow).get(i) / ratioValue);
            }

            for (int i = 0; i < numberOfRows; i++) {
                if (i != indexRow) {
                    Double value = workingTable.get(i).get(indexColumn) / workingTable.get(indexRow).get(indexColumn);
                    for (int j = 0; j < numberOfColumns; j++) {
                        workingTable.get(i).set(j, workingTable.get(i).get(j) - workingTable.get(indexRow).get(j)*value);
                    }
                }
            }

            function = workingTable.get(0).get(numberOfColumns - 1);

            if (abs(function - prevResult) < epsilon) {
                return answer;
            }

            for (int i = 0; i < C.size(); i++) {
                answer.add(0.0);
            }
            for (int i = 0; i < basic.size(); i++) {
                if (basic.get(i) <= C.size()) {
                    answer.set(basic.get(i) - 1, workingTable.get(i + 1).get(numberOfColumns - 1));
                }
            }
            answer.add(function);
            prevResult = function;
        }

        return answer;

    }
    public static Boolean checkCoefficients(ArrayList<Double> array) {
        for (Double value: array) {
            if (value < 0) {
                return true;
            }
        }
        return false;
    }
}

class Matrix {
    ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
    Matrix() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter n (the number of constraints):");
        Integer n = Integer.parseInt(sc.nextLine());
        numberOfRows = n;
        System.out.println("Enter matrix A line by line:");
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] wholeLine = line.split(" ");
            ArrayList<Double> mLine = new ArrayList<>();
            for (String s : wholeLine) {
                mLine.add(Double.parseDouble(s));
            }
            matrix.add(mLine);
        }
    }
    Integer numberOfRows;

    public Integer getNumberOfRows() {
        return numberOfRows;
    }
}

