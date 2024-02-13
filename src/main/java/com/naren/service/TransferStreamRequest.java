package com.naren.service;

import com.naren.models.Account;
import com.naren.models.TransferRequest;
import com.naren.models.TransferResponse;
import com.naren.models.TransferStatus;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class TransferStreamRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;
    private int accountBal;

    public TransferStreamRequest(StreamObserver<TransferResponse> balanceStreamObserver) {
        this.transferResponseStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {

        int amount = transferRequest.getAmount();
        int toAccountNumber = transferRequest.getToAccountNumber();
        int fromAccountNumber = transferRequest.getFromAccountNumber();
        int balance = AccountDatabase.getBalance(fromAccountNumber);
        TransferStatus transferStatus = TransferStatus.FAILED;

        if(balance<amount && fromAccountNumber !=toAccountNumber){
            Status status = Status.INTERNAL;

            //this.transferResponseStreamObserver.onError();
        }
        AccountDatabase.deductBalance(fromAccountNumber, amount);
        Integer accountBal = AccountDatabase.depositAmount(toAccountNumber, amount);
        transferStatus = TransferStatus.SUCCESS;
        Account fromAccount = Account.newBuilder().setAccountNumber(fromAccountNumber)
                .setAmount(AccountDatabase.getBalance(fromAccountNumber)).build();
        Account toAccount = Account.newBuilder().setAccountNumber(fromAccountNumber)
                .setAmount(AccountDatabase.getBalance(fromAccountNumber)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(transferStatus)
                .addAccounts(fromAccount)
                .addAccounts(toAccount)
                .build();
        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.transferResponseStreamObserver.onCompleted();

    }
}
