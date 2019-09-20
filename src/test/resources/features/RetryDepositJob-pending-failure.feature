@RetryDepositJob
Feature: RetryDepositJob
    Checks if the job for retrying deposits is correct for at least one pending deposits

    Scenario Outline: Deposit job with pending deposits
        Given the following deposits are pending
        |     id      |  sender  |  receiver  | amount | senderCurrency | receiverCurrency | retriesLeft |
        | transfer-1  | sender-1 | receiver-1 | 20000  |       USD      |       USD        |     10      |
        | transfer-2  | sender-2 | receiver-2 | 5000   |       USD      |       USD        |      9      |
        | transfer-3  | sender-3 | receiver-3 | 12798  |       USD      |       USD        |      8      |
        | transfer-4  | sender-4 | receiver-4 | 1235   |       USD      |       USD        |      7      |
        | transfer-5  | sender-5 | receiver-3 | 124785 |       USD      |       USD        |      6      |
        | transfer-6  | sender-6 | receiver-4 | 14285  |       USD      |       USD        |      5      |
        | transfer-7  | sender-2 | receiver-2 | 8799   |       USD      |       USD        |      4      |
        | transfer-8  | sender-4 | receiver-1 | 6105   |       USD      |       USD        |      3      |
        | transfer-9  | sender-7 | receiver-5 | 11479  |       USD      |       USD        |      2      |
        | transfer-10 | sender-4 | receiver-6 | 86500  |       USD      |       USD        |      1      |
        | transfer-11 | sender-1 | receiver-7 | 20000  |       USD      |       USD        |      0      |
        When RetryDepositJob runs
        Then system will report that <amount> deposits were processed
        Examples:
        | amount |
        |   10   |
