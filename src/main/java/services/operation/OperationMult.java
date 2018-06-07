package services.operation;

import services.AbctractServiceOperation;

public class OperationMult   extends AbctractServiceOperation {

    public Double calculate(Double a, Double b){
        getStatistic();
        return a * b;
    }
}