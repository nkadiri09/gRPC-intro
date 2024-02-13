package com.naren.service;

import com.naren.models.TransferRequest;
import com.naren.models.TransferResponse;
import com.naren.models.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
