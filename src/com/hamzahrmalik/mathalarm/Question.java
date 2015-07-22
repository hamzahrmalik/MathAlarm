package com.hamzahrmalik.mathalarm;

import java.util.HashSet;
import java.util.Set;

public class Question {

	String question;
	int answer;

	public Question(Set<String> typesSet, int difficulty) {
		// Highest number user will get in calculation
		difficulty *= 5;
		if (typesSet == null || typesSet.isEmpty()) {
			// Make a set, because it's not there...
			typesSet = new HashSet<String>();
			typesSet.add("1");
			typesSet.add("2");
			typesSet.add("3");
			typesSet.add("4");
		}
		String[] types = typesSet.toArray(new String[0]);
		int random = getRandomNumber(1, types.length) - 1;// Because java counts
															// from 0, -1
		// int operator should hold 1,2,3 or 4, representing + - * /
		int operator = Integer.parseInt(types[random]);
		String operatorStr = ""; // Holds the actual symbol for the operator
		if (operator == 1)
			operatorStr = "+";
		else if (operator == 2)
			operatorStr = "-";
		else if (operator == 3)
			operatorStr = "x";
		else if (operator == 4)
			operatorStr = "/";

		// The two numbers in the sum, and the answer
		int num1 = getRandomNumber(difficulty - 4, difficulty);
		int num2 = getRandomNumber(difficulty - 4, difficulty);
		int answer = 0;// will be calculated later

		// Use the operator generated earlier to work out answer
		if (operator == 1)
			answer = num1 + num2;
		else if (operator == 2) {
			answer = num1 - num2;
			if (answer < 0) {
				// too crazy, switch em around
				int temp = num1;
				num1 = num2;
				num2 = temp;
				answer = num1 - num2;
			}
		} else if (operator == 3)
			answer = num1 * num2;
		else if (operator == 4) {
			// To prevent crazy decimals, multiply the two random numbers and
			// make the answer the question
			num1 = num1 * num2;
			answer = num1 / num2;
		}

		this.question = "What is " + num1 + operatorStr + num2 + "?";
		this.answer = answer;
	}

	/**
	 * @param min
	 *            The lowest number this can return
	 * @param max
	 *            The highest number this can return
	 * @return Returns a random number between min and max, inclusive
	 */
	public int getRandomNumber(int min, int max) {
		min--;
		return (int) (Math.floor(Math.random() * (max - min)) + 1 + min);
	}
}
