package com.revolut.test.backend.ricardofuzeto.system.datatables;

import com.revolut.test.backend.ricardofuzeto.database.tables.pojos.Transfer;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import org.jooq.types.UInteger;

import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public class TransferTypeRegistry implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(Transfer.class,
                (Map<String, String> row) -> {
                    String id = row.get("id");
                    String sender = row.get("sender");
                    String receiver = row.get("receiver");
                    UInteger amount = UInteger.valueOf(row.get("amount"));
                    String senderCurrency = row.get("senderCurrency");
                    String receiverCurrency = row.get("receiverCurrency");
                    Integer retriesLeft = Integer.parseInt(row.get("retriesLeft"));
                    return new Transfer(id, sender, receiver, amount, senderCurrency, receiverCurrency, retriesLeft);
                }));
    }
}
