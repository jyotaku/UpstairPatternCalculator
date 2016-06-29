package upstair.src;

import java.math.BigInteger;
import java.util.Queue;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import com.google.common.collect.Lists;

/**
 * 問題4
 * 
 * @author jyotaku
 * @since 2016.6.2
 */
public class UpstairPatternCalculator {

	private final static String TIME_FORMAT = "HH:mm:ss.SSS";
	private final static int OUT_PUT_DIGIT = 9;

	private final Long maxStep;
	private final Long stairs;
	private final Queue<BigInteger> que = Lists.newLinkedList();

	public UpstairPatternCalculator(Long stairs, Long maxStep) {
		this.stairs = stairs;
		this.maxStep = maxStep;
	}

	/**
	 * メイン処理
	 */
	public void run() {
		long start = System.currentTimeMillis();

		BigInteger answer = calcPatterns();

		printoutAnswer(answer);

		System.out.println("Time: " + DurationFormatUtils.formatPeriod(start, System.currentTimeMillis(), TIME_FORMAT));
	}

	/*
	 * パターン数計算
	 */
	private BigInteger calcPatterns() {
		long start = System.currentTimeMillis();

		if (this.maxStep == 1 || this.stairs == 1) {
			return BigInteger.ONE;
		}
		if (this.stairs == 2 && this.maxStep >= 1) {
			return BigInteger.valueOf(2);
		}

		int j = 1;
		que.offer(BigInteger.valueOf(1));
		que.offer(BigInteger.valueOf(1));
		long mileStone = this.stairs / 100;
		BigInteger lastSum = BigInteger.ONE;
		for (long i = 2; i < this.stairs; i++) {
			BigInteger sum = getQueSum(this.que, lastSum);
			que.offer(sum);
			if (que.size() > this.maxStep + 1) {
				que.poll();
			}
			if (mileStone != 0 && i % mileStone == 0) {
				System.out.println(j++ + "% " + "Time: "
						+ DurationFormatUtils.formatPeriod(start, System.currentTimeMillis(), TIME_FORMAT));
			}
			lastSum = sum;
		}

		return getQueSum(this.que, lastSum);
	}

	/*
	 * 解の出力
	 */
	private void printoutAnswer(BigInteger answer) {
		String answerStr = String.valueOf(answer);
		if (answerStr.length() > 9) {
			answerStr = answerStr.substring(answerStr.length() - OUT_PUT_DIGIT);
		}
		System.out.println("Rresult(under " + OUT_PUT_DIGIT + " digit)  = " + answerStr);
	}

	/*
	 * Queの中身の合計を計算
	 */
	private BigInteger getQueSum(Queue<BigInteger> que, BigInteger lastSum) {
		if (que.size() > this.maxStep) {
			BigInteger TWO = BigInteger.valueOf(2);
			BigInteger sum = lastSum.multiply(TWO).subtract(que.peek());
			return sum;
		}

		BigInteger sum = BigInteger.ZERO;
		for (BigInteger value : que) {
			sum = sum.add(value);
		}
		return sum;
	}

	/**
	 * main function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Please set amount of stairs and  max value of steps as parameter.");
			return;
		} else if (!NumberUtils.isNumber(args[0]) || !NumberUtils.isNumber(args[1])) {
			System.out.println("Parameters have to be numbers.");
			return;
		}

		Long stairs = Long.parseLong(args[0]);
		Long maxStep = Long.parseLong(args[1]);

		if (stairs.longValue() > Math.pow(10, 18)) {
			System.out.println("Amount of stairs have to be lower than 10^18.");
			return;
		} else if (maxStep.intValue() > 10 || maxStep.intValue() < 0) {
			System.out.println("Steps have to between 0 and 10.");
			return;
		}
		UpstairPatternCalculator codeBreaker = new UpstairPatternCalculator(stairs, maxStep);
		codeBreaker.run();
	}
}
