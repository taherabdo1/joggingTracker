package com.toptal.demo.controllers.filtter;

public enum RsqlSearchOperation {
    eq, ne, gt, lt, and, or, AND, OR;

    // private ComparisonOperator operator;
    //
    // private RsqlSearchOperation(final ComparisonOperator operator) {
    // this.operator = operator;
    // }

    // public static RsqlSearchOperation getSimpleOperator(final ComparisonOperator operator) {
    //
    // for (final RsqlSearchOperation operation : values()) {
    // if (operation.getOperator() == operator) {
    // return operation;
    // }
    // }
    // return null;
    // }

    // public ComparisonOperator getOperator() {
    // return operator;
    // }

}
