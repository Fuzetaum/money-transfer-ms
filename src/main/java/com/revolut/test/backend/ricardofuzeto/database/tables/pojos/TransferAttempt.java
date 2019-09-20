/*
 * This file is generated by jOOQ.
 */
package com.revolut.test.backend.ricardofuzeto.database.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransferAttempt implements Serializable {

    private static final long serialVersionUID = 837301040;

    private Integer   id;
    private String    transferId;
    private Timestamp dateOfTransfer;
    private UInteger  result;

    public TransferAttempt() {}

    public TransferAttempt(TransferAttempt value) {
        this.id = value.id;
        this.transferId = value.transferId;
        this.dateOfTransfer = value.dateOfTransfer;
        this.result = value.result;
    }

    public TransferAttempt(
        Integer   id,
        String    transferId,
        Timestamp dateOfTransfer,
        UInteger  result
    ) {
        this.id = id;
        this.transferId = transferId;
        this.dateOfTransfer = dateOfTransfer;
        this.result = result;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransferId() {
        return this.transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public Timestamp getDateOfTransfer() {
        return this.dateOfTransfer;
    }

    public void setDateOfTransfer(Timestamp dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }

    public UInteger getResult() {
        return this.result;
    }

    public void setResult(UInteger result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TransferAttempt (");

        sb.append(id);
        sb.append(", ").append(transferId);
        sb.append(", ").append(dateOfTransfer);
        sb.append(", ").append(result);

        sb.append(")");
        return sb.toString();
    }
}
