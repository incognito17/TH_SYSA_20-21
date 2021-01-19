package markov;

import java.util.Arrays;
import java.util.Random;

public class MensaQueue {

    private double[][] table = new double[1000000][8];

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

    private double calculateAverage(double[] valueArr, int count) {
        double sum = 0.0;
        for (int i = 0; i < count; i++){
            sum += valueArr[i];
        }
        return sum / count;
    }


    public static void main(String[] args) {

        MensaQueue mensa = new MensaQueue();
        double[] intermArrivalTimes = mensa.generateExponentialTimes(1000000, 0.8);
        double[] arrivalTimes = mensa.calculateArrivalTime(intermArrivalTimes);
        double[] servingTimes = mensa.generateExponentialTimes(1000000, 0.7);
        double[][] servingStartAndEnd = mensa.calculateServingStartAndEnd(arrivalTimes, servingTimes);
        double[] servingBegin = servingStartAndEnd[0];
        double[] servingEnd = servingStartAndEnd[1];
        double[] waitingTimes = mensa.calculateWaitingTimes(arrivalTimes, servingBegin);
        double[] stayingDurations = mensa.calculateStayingDuration(arrivalTimes, servingEnd);

        System.out.println();
        System.out.println();
        System.out.println("*****************************");
        System.out.println("A      B      C     D    E      F    G     H");
        for (int i = 0; i < 10; i++) {
            mensa.table[i][0] = i+1;
            mensa.table[i][1] = Math.round (intermArrivalTimes[i] * 100.0) / 100.0;
            mensa.table[i][2] = Math.round (arrivalTimes[i] * 100.0) / 100.0;
            mensa.table[i][3] = Math.round (servingTimes[i] * 100.0) / 100.0;
            mensa.table[i][4] = Math.round (servingBegin[i] * 100.0) / 100.0;
            mensa.table[i][5] = Math.round (servingEnd[i] * 100.0) / 100.0;
            mensa.table[i][6] = Math.round (waitingTimes[i] * 100.0) / 100.0;
            mensa.table[i][7] = Math.round (stayingDurations[i] * 100.0) / 100.0;
            System.out.println(Arrays.toString(mensa.table[i]));
        }
        System.out.println("*****************************");
        System.out.println();

        System.out.println("10 Kunden:");
        System.out.println("Durchschnitt summeZwischenankunftszeit: " + Math.round (mensa.calculateAverage(intermArrivalTimes, 10) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeBediendauer: " + Math.round (mensa.calculateAverage(servingTimes, 10) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeWartezeit: " + Math.round (mensa.calculateAverage(waitingTimes, 10) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeVerweilzeit: " + Math.round (mensa.calculateAverage(stayingDurations, 10) * 100.0) / 100.0);


        System.out.println("*****************************");
        System.out.println();
        System.out.println("1000 Kunden:");
        System.out.println("Durchschnitt summeZwischenankunftszeit: " + Math.round (mensa.calculateAverage(intermArrivalTimes, 1000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeBediendauer: " + Math.round (mensa.calculateAverage(servingTimes, 1000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeWartezeit: " + Math.round (mensa.calculateAverage(waitingTimes, 1000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeVerweilzeit: " + Math.round (mensa.calculateAverage(stayingDurations, 1000) * 100.0) / 100.0);

        System.out.println("*****************************");
        System.out.println();
        System.out.println("1 000 000 Kunden:");
        System.out.println("Durchschnitt summeZwischenankunftszeit: " + Math.round (mensa.calculateAverage(intermArrivalTimes, 1000000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeBediendauer: " + Math.round (mensa.calculateAverage(servingTimes, 1000000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeWartezeit: " + Math.round (mensa.calculateAverage(waitingTimes, 1000000) * 100.0) / 100.0);
        System.out.println("Durchschnitt summeVerweilzeit: " + Math.round (mensa.calculateAverage(stayingDurations, 1000000) * 100.0) / 100.0);
    }

}
