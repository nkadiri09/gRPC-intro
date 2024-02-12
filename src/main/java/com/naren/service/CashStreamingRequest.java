package com.naren.service;

import com.naren.models.Balance;
import com.naren.models.DepositRequest;
import io.grpc.stub.StreamObserver;

public class CashStremingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<Balance> balanceStreamObserver;
    private int accountBal;

    @Override
    public void onNext(DepositRequest depositRequest) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
