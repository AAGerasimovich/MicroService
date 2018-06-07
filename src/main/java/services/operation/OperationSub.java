package services.operation;

import services.AbctractServiceOperation;

public class OperationSub   extends AbctractServiceOperation {

    public Double calculate(Double a, Double b){

        getStatistic();
        return a - b;
    }
}