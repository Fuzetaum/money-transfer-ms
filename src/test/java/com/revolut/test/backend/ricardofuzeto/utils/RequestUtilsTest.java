package com.revolut.test.backend.ricardofuzeto.utils;

import com.google.gson.JsonSyntaxException;
import com.revolut.test.backend.ricardofuzeto.configuration.gson.GsonConfiguration;
import com.revolut.test.backend.ricardofuzeto.model.AccountPojo;
import org.jooq.types.UInteger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.runners.JUnit44RunnerImpl;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class RequestUtilsTest {
    @BeforeClass
    public static void setUpClass() {
        GsonConfiguration.configure();
    }

    @Test
    public void fromJson_WithNothing_ShouldReturnNull() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("", AccountPojo.class);
        assertThat(pojo, is(nullValue()));
    }

    @Test
    public void fromJson_WithJsonEmpty_ShouldReturnEmptyObject() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("{}", AccountPojo.class);
        assertThat(pojo.getId(), is(nullValue()));
        assertThat(pojo.getOwner(), is(nullValue()));
        assertThat(pojo.getBalance(), is(nullValue()));
        assertThat(pojo.getCurrency(), is(nullValue()));
    }

    @Test
    public void fromJson_WithJsonHavingOnlyId_ShouldReturnObjectWithOnlyId() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("{\"id\":\"test-id\"}", AccountPojo.class);
        assertThat(pojo.getId(), is(equalTo("test-id")));
        assertThat(pojo.getOwner(), is(nullValue()));
        assertThat(pojo.getBalance(), is(nullValue()));
        assertThat(pojo.getCurrency(), is(nullValue()));
    }

    @Test
    public void fromJson_WithJsonHavingOnlyOwner_ShouldReturnObjectWithOnlyOwner() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("{\"owner\":\"account owner\"}", AccountPojo.class);
        assertThat(pojo.getId(), is(nullValue()));
        assertThat(pojo.getOwner(), is(equalTo("account owner")));
        assertThat(pojo.getBalance(), is(nullValue()));
        assertThat(pojo.getCurrency(), is(nullValue()));
    }

    @Test
    public void fromJson_WithJsonHavingOnlyBalance_ShouldReturnObjectWithOnlyBalance() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("{\"balance\":12345}", AccountPojo.class);
        assertThat(pojo.getId(), is(nullValue()));
        assertThat(pojo.getOwner(), is(nullValue()));
        assertThat(pojo.getBalance(), is(equalTo(UInteger.valueOf(12345))));
        assertThat(pojo.getCurrency(), is(nullValue()));
    }

    @Test
    public void fromJson_WithJsonHavingOnlyCurrency_ShouldReturnObjectWithOnlyCurrency() {
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson("{\"currency\":\"GBP\"}", AccountPojo.class);
        assertThat(pojo.getId(), is(nullValue()));
        assertThat(pojo.getOwner(), is(nullValue()));
        assertThat(pojo.getBalance(), is(nullValue()));
        assertThat(pojo.getCurrency(), is(equalTo("GBP")));
    }

    @Test(expected = JsonSyntaxException.class)
    public void fromJson_WithJsonHavingOnlyNullFields_ShouldReturnEmptyObject() {
        String json = "{\"id\":null,\"owner\":null,\"balance\":null,\"currency\":null}";
        RequestUtils.fromJson(json, AccountPojo.class);
    }

    @Test
    public void fromJson_WithJsonHavingAllFields_ShouldReturnBuiltObject() {
        String json = "{\"id\":\"test-id\",\"owner\":\"Account Owner\",\"balance\":12345,\"currency\":\"GBP\"}";
        AccountPojo pojo = (AccountPojo) RequestUtils.fromJson(json, AccountPojo.class);
        assertThat(pojo.getId(), is(equalTo("test-id")));
        assertThat(pojo.getOwner(), is(equalTo("Account Owner")));
        assertThat(pojo.getBalance(), is(equalTo(UInteger.valueOf(12345))));
        assertThat(pojo.getCurrency(), is(equalTo("GBP")));
    }

    @Test
    public void toJson_WithOjectNull_ShouldReturnNull() {
        String json = RequestUtils.toJson(null, AccountPojo.class);
        assertThat(json, is(equalTo("null")));
    }

    @Test(expected = NullPointerException.class)
    public void toJson_WithBalanceNull_ShouldThrowNullPointerException() {
        AccountPojo pojo = new AccountPojo(null, null, null, null);
        RequestUtils.toJson(pojo, AccountPojo.class);
    }

    @Test
    public void toJson_WithObjectHavingOnlyBalance_ShouldReturnJsonWithOnlyBalance() {
        AccountPojo pojo = new AccountPojo(null, null, UInteger.valueOf(12345), null);
        String json = RequestUtils.toJson(pojo, AccountPojo.class);
        assertThat(json, not(containsString("\"id\":")));
        assertThat(json, not(containsString("\"owner\":")));
        assertThat(json, containsString("\"balance\": 12345"));
        assertThat(json, not(containsString("\"currency\":")));
    }

    @Test
    public void toJson_WithObjectHavingOnlyBalanceAndId_ShouldReturnJsonWithOnlyNonNullFields() {
        AccountPojo pojo = new AccountPojo("test-id", null, UInteger.valueOf(12345), null);
        String json = RequestUtils.toJson(pojo, AccountPojo.class);
        assertThat(json, containsString("\"id\": \"test-id\""));
        assertThat(json, not(containsString("\"owner\":")));
        assertThat(json, containsString("\"balance\": 12345"));
        assertThat(json, not(containsString("\"currency\":")));
    }

    @Test
    public void toJson_WithObjectHavingOnlyBalanceAndOwner_ShouldReturnJsonWithOnlyNonNullFields() {
        AccountPojo pojo = new AccountPojo(null, "Account Owner", UInteger.valueOf(12345), null);
        String json = RequestUtils.toJson(pojo, AccountPojo.class);
        assertThat(json, not(containsString("\"id\":")));
        assertThat(json, containsString("\"owner\": \"Account Owner\""));
        assertThat(json, containsString("\"balance\": 12345"));
        assertThat(json, not(containsString("\"currency\":")));
    }

    @Test
    public void toJson_WithObjectHavingOnlyBalanceAndCurrency_ShouldReturnJsonWithOnlyNonNullFields() {
        AccountPojo pojo = new AccountPojo(null, null, UInteger.valueOf(12345), "GBP");
        String json = RequestUtils.toJson(pojo, AccountPojo.class);
        assertThat(json, not(containsString("\"id\":")));
        assertThat(json, not(containsString("\"owner\":")));
        assertThat(json, containsString("\"balance\": 12345"));
        assertThat(json, containsString("\"currency\": \"GBP\""));
    }

    @Test
    public void toJson_WithObjectHavingAllFields_ShouldReturnJsonWithAllFields() {
        AccountPojo pojo = new AccountPojo("test-id", "Account Owner", UInteger.valueOf(12345), "GBP");
        String json = RequestUtils.toJson(pojo, AccountPojo.class);
        assertThat(json, containsString("\"id\": \"test-id\""));
        assertThat(json, containsString("\"owner\": \"Account Owner\""));
        assertThat(json, containsString("\"balance\": 12345"));
        assertThat(json, containsString("\"currency\": \"GBP\""));
    }
}
