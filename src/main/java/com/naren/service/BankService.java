package com.naren.service;

import com.naren.models.Balance;
import com.naren.models.BalanceCheckRequest;
import com.naren.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();

        Balance balance = Balance.newBuilder().setAmount(AccountDatabase.getBalance(accountNumber)).build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}
