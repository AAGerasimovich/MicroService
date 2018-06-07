package utils;

import services.*;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationHolder {
    List<ServiceOperation> svsOps;

    private ServiceOperation addSvc;
    private ServiceOperation subSvc;
    private ServiceOperation mltSvc;
    private ServiceOperation divSvc;
    private String s;
    List<Double> expression;

    public OperationHolder(ServiceOperation addSvc, ServiceOperation subSvc, ServiceOperation mltSvc, ServiceOperation divSvc) {
        this.addSvc = addSvc;
        this.subSvc = subSvc;
        this.mltSvc = mltSvc;
        this.divSvc = divSvc;
    }

    public Double calc(String str) {

        str = str.replaceAll(" ", "");
        expression = Stream.of(str.split("[*/+-]")).map(c -> Double.parseDouble(c)).collect(Collectors.toList());

        s = str.replaceAll("[^*/+-]", "");


        while (!s.equals("")) {
            int n;
            if (s.contains("*") || s.contains("/")) {

                if ((s.indexOf('*') != -1 && s.indexOf('*') < s.indexOf('/')) || s.indexOf('/') == -1) {
                    replaceList('*');

                } else {
                    replaceList('/');
                }
            } else {
                if ((s.indexOf('+') != -1 && s.indexOf('+') < s.indexOf('-')) || s.indexOf('-') == -1) {
                    replaceList('+');

                } else {
                    replaceList('-');
                }
            }
        }

        return    (double)((int)(expression.get(0)*100))/100;

    }

    private void replaceList(char c) {
        int n = s.indexOf(c);
        if(c=='+') {
            expression.set(n + 1, addSvc.calculate(expression.get(n), expression.get(n + 1)));
        }
        if(c=='-') {
            expression.set(n + 1, subSvc.calculate(expression.get(n), expression.get(n + 1)));
        }
        if(c=='*') {
            expression.set(n + 1, mltSvc.calculate(expression.get(n), expression.get(n + 1)));
        }
        if(c=='/') {
            expression.set(n + 1, divSvc.calculate(expression.get(n), expression.get(n + 1)));
        }
        expression.remove(n);
        s = s.replaceFirst("[" + c + "]", "");
    }



}

