package services.operation;

import services.AbctractServiceOperation;



public class OperationAdd  extends AbctractServiceOperation {


    public Double calculate(Double a, Double b){
        getStatistic();


        return a + b;
    }
}