package com.firebirdcss.tools.password_generator;

import java.util.Random;

public class SandBox {

	public static void main(String[] args) {
		Random rnd = new Random();
		
		int attempts = 1;
		while (rnd.nextInt(6) != 5) {
			attempts ++;
		}
		
		System.out.println("It took '" + attempts + "' attempts to get the desired number.");
	}

}
