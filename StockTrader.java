import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("tiny.txt"));
        String symbol = in.next();
        int n = in.nextInt();

        // TODO: create the three arrays, each with n elements
        int[] year = new int[n];
        int[] month = new int[n];
        double[] price = new double[n];

        // TODO: call loadData, then printData
        loadData(in, year, month, price);
        printData(year, month, price);
        System.out.printf("%s lowest: $%.2f in %s %d%n", symbol, price[indexOfLowest(price)],
            monthName(indexOfLowest(price)), year[indexOfLowest(price)]);
        System.out.printf("%s highest: $%.2f in %s %d%n", symbol, price[indexOfHighest(price)],
            monthName(indexOfHighest(price)), year[indexOfHighest(price)]);
        System.out.printf("%s largest drop: %s %d%n", symbol, monthName(indexOfBiggestDrop(price)),
            year[indexOfBiggestDrop(price)]);
        double result = simulate(year, month, price);
        System.out.printf("Final value: $%,.2f%n", result);
    }
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }
    public static String monthName(int month) {
        String[] name = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return name[month];
    }
    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            System.out.printf("%d %s $%.2f%n", years[i], monthName(i), prices[i]);
        }
    }
    public static int indexOfLowest(double[] prices) {
        // double next=0;
        // double low=in.nextDouble();
        double low = prices[0];
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (low > prices[i]) {
                index = i;
                low = prices[i];
            }
        }
        return index;
    }
    public static int indexOfHighest(double[] prices) {
        double high = prices[0];
        int index = 0;
        for (int i = 0; i < prices.length; i++) {
            if (high < prices[i]) {
                index = i;
                high = prices[i];
            }
        }
        return index;
    }
    public static int indexOfBiggestDrop(double[] prices) {
        double drop = 0.0;
        int index = 0;
        double change = 0;
        for (int i = 1; i < prices.length; i++) {
            change = 100 * (prices[i] - prices[i - 1]) / prices[i - 1];
            if (drop > change) {
                index = i;
                drop = change;
            }
        }
        return index;
    }

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static double simulate(int[] years, int[] months, double[] prices) {
        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {
            double[] history = Arrays.copyOf(prices, i + 1);
            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {
                int canBuy = (int) ((cash - FEE) / prices[i]);
                if (canBuy > 0) {
                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n", monthName(months[i]),
                        years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n", monthName(months[i]),
                    years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }
    //    public static String decide(double[] history, boolean hasShares, double boughtAt) {
    //    if (hasShares == true) {
    //        return "HOLD";
    //    } else {
    //        return "BUY";
    //    }
    //}

    public static String decide(double[] history, boolean hasShares, double boughtAt) {
        String result = "HOLD";
        int boughtIndex=0;
        if (hasShares == true) {
			for(int i = boughtIndex; i<history.length;i++){
				if(history[i]>(boughtAt*1.1)){
					result = "SELL";
				}else{
					result = "HOLD";
				}
			}
        } else {
            for (int i = 2; i < history.length; i++) {
                if (history[i] < history[i - 1] && history[i - 1] < history[i - 2]) {
                    result = "BUY";
                    boughtAt = history[i];
                    boughtIndex=i;
                }
            }
        }
        return result;
	}
}
