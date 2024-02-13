package com.naren.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {

    private static final Map<Integer, Integer> accountMap =
    IntStream.rangeClosed(1,25).boxed().collect(Collectors.toMap(x->x, x->100));

    public static int getBalance(Integer accountId){
        return accountMap.get(accountId);
    }

    public static Integer depositAmount(Integer accountId, int amount) {
        return accountMap.computeIfPresent(accountId, (k, v) -> v + amount);
    }

    public static Integer deductBalance(Integer accountId, int amount) {
        return accountMap.computeIfPresent(accountId, (k, v) -> v - amount);
    }

    public static void printAccountDetails() {
        System.out.println(accountMap);
    }

}
