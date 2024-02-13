package com.naren.service;

import com.naren.models.Account;
import com.naren.models.TransferRequest;
import com.naren.models.TransferResponse;
import com.naren.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private final StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int amount = transferRequest.getAmount();
        int fromAccountNumber = transferRequest.getFromAccountNumber();
        int toAccountNumber = transferRequest.getToAccountNumber();
        int bal = AccountDatabase.getBalance(fromAccountNumber);
        TransferStatus status = TransferStatus.FAILED;
        if(bal>amount && fromAccountNumber != toAccountNumber){
            AccountDatabase.deductBalance(fromAccountNumber, amount);
            AccountDatabase.depositAmount(toAccountNumber, amount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccount = Account.newBuilder().setAccountNumber(fromAccountNumber)
                .setAmount(AccountDatabase.getBalance(fromAccountNumber)).build();
        Account toAccount = Account.newBuilder().setAccountNumber(toAccountNumber)
                .setAmount(AccountDatabase.getBalance(toAccountNumber)).build();

        TransferResponse response = TransferResponse.newBuilder()
                .addAccounts(fromAccount)
                .addAccounts(toAccount)
                .setStatus(status)
                .build();

        transferResponseStreamObserver.onNext(response);

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDatabase.printAccountDetails();
        transferResponseStreamObserver.onCompleted();
    }
}
