package com.firebirdcss.tools.password_generator.exceptions;

/**
 * This exception indicates that a Statistical Improbability has occurred which
 * has compromised the code's ability to function properly.
 * 
 * @author Scott Griffis
 *
 */
public class StatisticalImprobabilityException extends Exception {
	private static final long serialVersionUID = 7193393040886676630L;

	public StatisticalImprobabilityException() {
		super();
	}

	public StatisticalImprobabilityException(String message) {
		super(message);
	}

	public StatisticalImprobabilityException(Throwable cause) {
		super(cause);
	}

	public StatisticalImprobabilityException(String message, Throwable cause) {
		super(message, cause);
	}

	public StatisticalImprobabilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
