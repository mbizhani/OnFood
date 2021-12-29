package org.devocative.onfood.search.expression.logical;

public abstract class LogicalOperator {
	public enum EMultiOperand {
		And,
		Or
	}

	public enum ESingleOperand {
		Not
	}
}
