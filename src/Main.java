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

        Matrix A = new Matrix("");

        System.out.println("Enter b in one line:");
        String lineB = scanner.nextLine();
        String[] wholeLineB = lineB.split(" ");
        ArrayList<Double> b = new ArrayList<>();
        b.add(0.0);
        for (String s : wholeLineB) {
            b.add(Double.parseDouble(s));
        }

        ArrayList<Double> b2 = b;

        System.out.println("Enter x0 in one line:");
        String x0str = scanner.nextLine();
        String[] wholeLineX_0 = x0str.split(" ");
        ArrayList<Double> x0 = new ArrayList<>();
        for (String s : wholeLineX_0) {
            x0.add(Double.parseDouble(s));
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
        Integer k = 1;
        for (int i = 0; i < C.size(); i++) {
            System.out.print("x" + k);
            if (i != C.size() - 1) {
                System.out.print(", ");
            }
            if (i == C.size() - 1) {
                System.out.println(" >= 0");
            }
            k += 1;
        }
        System.out.println();

        ArrayList<Double> answer2 = InteriorPointAlgorithm(C, A, b2, x0, 0.5, 0.9);

        boolean flag = false;
        for (int i = 0; i < answer2.size(); i++) {
            if (!Double.isNaN(answer2.get(i))) {
                flag = true;
            }
        }


        System.out.println("Interior Point Algorithm result:");

        if (answer2.isEmpty() || !flag) {
            System.out.println("The method is not applicable!");
        }
        else {
            if (type.equals("Max")) {
                Integer c = 1;
                for (int i = 0; i < answer2.size() - 1; i++) {
                    double val = answer2.get(i);
                    val = Math.round(val * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                    System.out.println(String.format("x" + c + " = " + "%." + accuracy + "f", val));
                    c += 1;
                }
                double func = answer2.get(answer2.size() - 1);
                func = Math.round(func * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                System.out.println(String.format("Maximum value is " + "%." + accuracy + "f", func));
            }
            if (type.equals("Min")) {
                Integer c = 1;
                for (int i = 0; i < answer2.size() - 1; i++) {
                    double val = answer2.get(i);
                    val = Math.round(val * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                    System.out.println(String.format("x" + c + " = " + "%." + accuracy + "f", val));
                    c += 1;
                }
                double func = -answer2.get(answer2.size() - 1);
                func = Math.round(func * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                System.out.println(String.format("Minimum value is " + "%." + accuracy + "f", func));
            }
        }
        System.out.println();

        System.out.println("Simplex result:");
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
                    System.out.println(String.format("x" + c + " = " + "%." + accuracy + "f", val));
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
                    System.out.println(String.format("x" + c + " = " + "%." + accuracy + "f", val));
                    c += 1;
                }
                double func = -ans.get(ans.size() - 1);
                func = Math.round(func * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
                System.out.println(String.format("Minimum value is " + "%." + accuracy + "f", func));
            }
        }

    }

    public static ArrayList<Double> InteriorPointAlgorithm(ArrayList<Double> initC, Matrix initA, ArrayList<Double> b, ArrayList<Double> x0, Double alpha, Double epsilon) {
        boolean solved = false;
        ArrayList<Double> x = x0;
        while (!solved) {
            Matrix A = initA;
            ArrayList<Double> C = initC;

            ArrayList<Double> oldX = x;

            Matrix D = Matrix.diag(x);

            A = A.multiply(D);

            C = D.multiplyMatVec(C);


            Matrix identity = Matrix.identity(A.getNumberOfColumns());


            Matrix inverseD = Matrix.findInverse(D, identity, A.getNumberOfColumns());

            x = inverseD.multiplyMatVec(x);


            Matrix transposeA = A.transpose();

            Matrix AmultTranspose = A.multiply(transposeA);

            Matrix I = Matrix.identity(AmultTranspose.getNumberOfColumns());
            Matrix AmultTrInverse = Matrix.findInverse(AmultTranspose, I, AmultTranspose.numberOfColumns);
            Matrix Aintermediate = A.transpose().multiply(AmultTrInverse);
            A = Aintermediate.multiply(A);

            Matrix iden = Matrix.identity(A.getNumberOfColumns());

            ArrayList<ArrayList<Double>> array = new ArrayList<>();
            for (int i = 0; i < A.getNumberOfRows(); i++) {
                ArrayList<Double> ar = new ArrayList<>();
                for (int j = 0; j < A.getNumberOfColumns(); j++) {
                    Double value = iden.matrix.get(i).get(j) - A.matrix.get(i).get(j);
                    ar.add(value);
                }
                array.add(ar);
            }
            Matrix P = new Matrix(array);


            ArrayList<Double> projGradient = new ArrayList<>();
            projGradient = P.multiplyMatVec(C);



            Double V = 100000000.0;
            for (int i = 0; i < C.size(); i++) {
                if (projGradient.get(i) < V) {
                    V = projGradient.get(i);
                }
            }
            V = abs(V);

            if (V < Math.pow(10, -10)) {
                ArrayList<Double> ans = new ArrayList<>();
                return ans;
            }


            Double coefficient = alpha/V;

            for (int i = 0; i < projGradient.size(); i++) {
                projGradient.set(i, projGradient.get(i) * coefficient);
            }

            for (int i = 0; i < x.size(); i++) {
                x.set(i, x.get(i) + projGradient.get(i));
            }

            x = D.multiplyMatVec(x);

            boolean flag = true;
            for (int i = 0; i < x.size(); i++) {
                if (abs(x.get(i) - oldX.get(i)) > epsilon) {
                    flag = false;
                }
            }

            if (flag) {
                solved = true;
                ArrayList<Double> answer = x;
                Double function = 0.0;
                for (int i = 0; i < initC.size(); i++) {
                    function += initC.get(i)*x.get(i);
                }
                answer.add(function);
            }

        }
        return x;
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
        System.out.println("Enter the number of rows:");
        Integer n = Integer.parseInt(sc.nextLine());
        numberOfRows = n;

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] wholeLine = line.split(" ");
            numberOfColumns = wholeLine.length;
            ArrayList<Double> mLine = new ArrayList<>();
            for (String s : wholeLine) {
                mLine.add(Double.parseDouble(s));
            }
            matrix.add(mLine);
        }
    }

    Matrix(ArrayList<ArrayList<Double>> array) {
        matrix = array;
        numberOfRows = array.size();
        numberOfColumns = array.get(0).size();
    }

    Matrix(String console) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter n (the number of constraints):");
        Integer n = Integer.parseInt(sc.nextLine());
        numberOfRows = n;
        System.out.println("Enter matrix A line by line:");
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] wholeLine = line.split(" ");
            numberOfColumns = wholeLine.length;
            ArrayList<Double> mLine = new ArrayList<>();
            for (String s : wholeLine) {
                mLine.add(Double.parseDouble(s));
            }
            matrix.add(mLine);
        }
    }

    Integer numberOfRows;
    Integer numberOfColumns;

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public Integer getNumberOfColumns() {
        return numberOfColumns;
    }


    public Matrix multiply(Matrix b) {
        if (this.numberOfColumns != b.getNumberOfRows()) {
            System.out.println();
            System.out.println("The method is not applicable!");
            throw new IllegalArgumentException("Number of rows does not match the number of columns!");
        }
        ArrayList<ArrayList<Double>> newMatrix = new ArrayList<>();
        for (int i = 0; i < this.numberOfRows; i++) {
            ArrayList<Double> ar = new ArrayList<>();
            for (int j = 0; j < b.getNumberOfColumns(); j++) {
                Double value = 0.0;
                for (int k = 0; k < this.numberOfColumns; k++) {
                    value += this.matrix.get(i).get(k) * b.matrix.get(k).get(j);
                }
                ar.add(value);
            }
            newMatrix.add(ar);
        }
        return new Matrix(newMatrix);
    }

    public ArrayList<Double> multiplyMatVec(ArrayList<Double> vector) {
        if (this.getNumberOfColumns() != vector.size()) {
            System.out.println();
            System.out.println("The method is not applicable!");
            throw new IllegalArgumentException("Number of rows does not match the number of columns!");
        }
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            double value = 0.0;
            for (int j = 0; j < this.getNumberOfColumns(); j++) {


                if (Double.isNaN(matrix.get(i).get(j)) || Double.isNaN(vector.get(j))) {
                    value += 0.0;
                }
                else {
                    value += matrix.get(i).get(j) * vector.get(j);
                }
            }
            result.add(value);
        }
        return result;
    }

    public Matrix transpose() {
        ArrayList<ArrayList<Double>> newMatrix = new ArrayList<>();
        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            ArrayList<Double> array = new ArrayList<>();
            for (int j = 0; j < this.getNumberOfRows(); j++) {
                Double element = matrix.get(j).get(i);
                array.add(element);
            }
            newMatrix.add(array);
        }
        return new Matrix(newMatrix);
    }

    public static Matrix diag(ArrayList<Double> array) {
        ArrayList<ArrayList<Double>> newMatrix = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            ArrayList<Double> ar = new ArrayList<>();
            for (int j = 0; j < array.size(); j++) {
                if (i == j) {
                    ar.add(array.get(i));
                } else {
                    ar.add(0.0);
                }
            }
            newMatrix.add(ar);
        }
        return new Matrix(newMatrix);
    }

    public static Matrix identity(int size) {
        ArrayList<ArrayList<Double>> newMatrix = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Double> ar = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    ar.add(1.0);
                } else {
                    ar.add(0.0);
                }
            }
            newMatrix.add(ar);
        }
        return new Matrix(newMatrix);
    }
    public static Matrix eliminationMatrix(Matrix matrix, int i, int j, int pivRow)
    {
        Matrix identity = identity(matrix.getNumberOfRows());
        Double difference  = matrix.matrix.get(i).get(j) / matrix.matrix.get(pivRow).get(j);
        identity.matrix.get(i).set(j, -difference);
        return identity;
    }
    // function that will find inverse from identity matrix B by making A identity
    public static Matrix findInverse(Matrix A, Matrix B, int a) {
        if (a == 1) {
            ArrayList<ArrayList<Double>> array = new ArrayList<>();
            ArrayList<Double> ar = new ArrayList<>();
            ar.add(1 / A.matrix.get(0).get(0));
            array.add(ar);
            return new Matrix(array);
        } else {
            Integer trans = 0;
            Integer permutationCounter = 0;

            for (int i = 0; i < a - 1; i++) {
                Double max = abs(A.matrix.get(i).get(i));
                Boolean swap = false;
                int position = i;
                for (int j = i + 1; j < a; j++) {
                    if (abs(A.matrix.get(j).get(i)) > max) {
                        max = abs(A.matrix.get(j).get(i));
                        position = j;
                        swap = true;
                    }
                }
                if (swap) {
                    Matrix P = identity(a);
                    ArrayList<Double> array = P.matrix.get(i);
                    for (int l = 0; l < P.getNumberOfColumns(); l++) {
                        P.matrix.get(i).set(l, P.matrix.get(position).get(l));
                    }
                    for (int l = 0; l < P.getNumberOfColumns(); l++) {
                        P.matrix.get(position).set(l, array.get(l));
                    }
                    permutationCounter += 1;
                    A = P.multiply(A);
                    B = P.multiply(B);
                    trans += 1;
                }

                for (int k = i + 1; k < a; k++) {
                    if (A.matrix.get(k).get(i) != 0) {
                        Matrix E = eliminationMatrix(A, k, i, i);
                        A = E.multiply(A);
                        B = E.multiply(B);
                        trans += 1;
                    }

                }
            }

            for (int i = a - 1; i >= 0; i--) {
                double max = abs(A.matrix.get(i).get(i));
                Boolean swap = false;
                int position = i;
                for (int j = i + 1; j < a; j++) {
                    if (abs(A.matrix.get(j).get(i)) > max) {
                        max = abs(A.matrix.get(j).get(i));
                        position = j;
                        swap = true;
                    }
                }
                if (swap) {
                    Matrix P = identity(a);
                    ArrayList<Double> array = P.matrix.get(i);
                    for (int l = 0; l < P.getNumberOfColumns(); l++) {
                        P.matrix.get(i).set(l, P.matrix.get(position).get(l));
                    }
                    for (int l = 0; l < P.getNumberOfColumns(); l++) {
                        P.matrix.get(position).set(l, array.get(l));
                    }
                    permutationCounter += 1;
                    A = P.multiply(A);
                    B = P.multiply(B);
                    trans += 1;
                }

                for (int k = i - 1; k >= 0; k--) {
                    if (A.matrix.get(k).get(i) != 0) {
                        Matrix E = eliminationMatrix(A, k, i, i);
                        A = E.multiply(A);
                        B = E.multiply(B);
                        trans += 1;
                    }

                }

            }
            Matrix M = identity(a);
            for (int i = 0; i < a; i++) {
                M.matrix.get(i).set(i, 1 / A.matrix.get(i).get(i));
            }
            A = M.multiply(A);
            B = M.multiply(B);
            return B;
        }
    }

}
