package markov;

import java.util.Arrays;
import java.util.Random;

public class MensaQueue {

    private double[][] table = new double[10][8];

    private double[] generateExponentialTimes(int length, double rate) {
        double[] exponentialTimesArr = new double[length];
        Random gen = new Random(1);
        for (int i = 0; i < exponentialTimesArr.length; i++) {
            exponentialTimesArr[i] = -(rate) * Math.log(gen.nextDouble());
        }
        return exponentialTimesArr;
    }

    private double[] calculateArrivalTime(double[] intermArrivalTimes) {
        double[] arrivalTimes = new double[intermArrivalTimes.length];
        arrivalTimes[0] = intermArrivalTimes[0];
        for (int i = 1; i < intermArrivalTimes.length; i++) {
            arrivalTimes[i] = arrivalTimes[i-1] + intermArrivalTimes[i];
        }

        return arrivalTimes;
    }

    private  double[][] calculateServingStartAndEnd(double[] arrivalTimes, double[] servingTimes) {
        double[][] servingStartAndEndTimes = new double[2][arrivalTimes.length];
        servingStartAndEndTimes[0][0] = arrivalTimes[0];
        servingStartAndEndTimes[1][0] = arrivalTimes[0] + servingTimes[0];
        for (int i = 1; i < servingStartAndEndTimes[0].length; i++) {
            servingStartAndEndTimes[0][i] = Math.max(arrivalTimes[i], servingStartAndEndTimes[1][i - 1]);
            servingStartAndEndTimes[1][i] = servingStartAndEndTimes[0][i] + servingTimes[i];
        }
        return servingStartAndEndTimes;
    }

    private double[] calculateWaitingTimes(double[] arrivalTimes, double[] servingBegin) {
        double[] waitingTimes = new double[arrivalTimes.length];
        for (int i = 0; i < waitingTimes.length; i++) {
            waitingTimes[i] = servingBegin[i] - arrivalTimes[i];
        }
        return waitingTimes;
    }

    private double[] calculateStayingDuration(double[] arrivalTimes, double[] servingEnd) {
        double[] stayingDur = new double[arrivalTimes.length];
        for (int i = 0; i < stayingDur.length; i++) {
            stayingDur[i] = servingEnd[i] - arrivalTimes[i];
        }
        return stayingDur;
    }

    private double calculateAverage(double[] valueArr) {
        double sum = 0.0;
        for (int i = 0; i < valueArr.length; i++){
            sum += valueArr[i];
        }
        return sum / valueArr.length;
    }


    public static void main(String[] args) {

        MensaQueue test = new MensaQueue();
        double[] intermArrivalTimes = test.generateExponentialTimes(10, 0.8);
        double[] arrivalTimes = test.calculateArrivalTime(intermArrivalTimes);
        double[] servingTimes = test.generateExponentialTimes(10, 0.7);
        double[][] servingStartAndEnd = test.calculateServingStartAndEnd(arrivalTimes, servingTimes);
        double[] servingBegin = servingStartAndEnd[0];
        double[] servingEnd = servingStartAndEnd[1];
        double[] waitingTimes = test.calculateWaitingTimes(arrivalTimes, servingBegin);
        double[] stayingDurations = test.calculateStayingDuration(arrivalTimes, servingEnd);

        System.out.println();
        System.out.println();
        System.out.println("*****************************");
        System.out.println("A      B      C     D    E      F    G     H");
        for (int i = 0; i < test.table.length; i++) {
            test.table[i][0] = i+1;
            test.table[i][1] = Math.round (intermArrivalTimes[i] * 100.0) / 100.0;
            test.table[i][2] = Math.round (arrivalTimes[i] * 100.0) / 100.0;
            test.table[i][3] = Math.round (servingTimes[i] * 100.0) / 100.0;
            test.table[i][4] = Math.round (servingBegin[i] * 100.0) / 100.0;
            test.table[i][5] = Math.round (servingEnd[i] * 100.0) / 100.0;
            test.table[i][6] = Math.round (waitingTimes[i] * 100.0) / 100.0;
            test.table[i][7] = Math.round (stayingDurations[i] * 100.0) / 100.0;
            System.out.println(Arrays.toString(test.table[i]));
        }
        System.out.println("*****************************");
        System.out.println();

        System.out.println("Durchschnitt summeZwischenankunftszeit: " + test.calculateAverage(intermArrivalTimes));
        System.out.println("Durchschnitt summeBediendauer: " + test.calculateAverage(servingTimes));
        System.out.println("Durchschnitt summeWartezeit: " + test.calculateAverage(waitingTimes));
        System.out.println("Durchschnitt summeVerweilzeit: " + test.calculateAverage(stayingDurations));
    }

}
