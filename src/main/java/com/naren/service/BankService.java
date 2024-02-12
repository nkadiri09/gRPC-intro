package com.naren.service;

import com.naren.models.*;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();

        Balance balance = Balance.newBuilder().setAmount(AccountDatabase.getBalance(accountNumber)).build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithDrawRequest request, StreamObserver<Money> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();

        for (int i = 0; i < amount / 10; i++) {
            Money build = Money.newBuilder().setAmount(10).build();
            Integer integer = AccountDatabase.deductBalance(accountNumber, 10);
            System.out.println("withDraw " + integer + "from: " + accountNumber);
            responseObserver.onNext(build);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }

}
