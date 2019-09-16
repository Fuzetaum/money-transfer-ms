/*
 * This file is generated by jOOQ.
 */
package com.revolut.test.backend.ricardofuzeto.database.tables.records;


import com.revolut.test.backend.ricardofuzeto.database.tables.PendingWithdraw;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


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
public class PendingWithdrawRecord extends UpdatableRecordImpl<PendingWithdrawRecord> implements Record1<String> {

    private static final long serialVersionUID = 1506159892;

    /**
     * Setter for <code>pending_withdraw.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>pending_withdraw.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record1 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }

    @Override
    public Row1<String> valuesRow() {
        return (Row1) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return PendingWithdraw.PENDING_WITHDRAW.ID;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public PendingWithdrawRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public PendingWithdrawRecord values(String value1) {
        value1(value1);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PendingWithdrawRecord
     */
    public PendingWithdrawRecord() {
        super(PendingWithdraw.PENDING_WITHDRAW);
    }

    /**
     * Create a detached, initialised PendingWithdrawRecord
     */
    public PendingWithdrawRecord(String id) {
        super(PendingWithdraw.PENDING_WITHDRAW);

        set(0, id);
    }
}
