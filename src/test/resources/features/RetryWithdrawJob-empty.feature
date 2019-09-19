@RetryWithdrawJob
Feature: RetryWithdrawJob
    Checks if the job for retrying withdraws is correct

    Scenario: Withdraw job with no pending withdrawals
        Given there are no pending withdraws
        When RetryWithdrawJob runs
        Then system will report that no withdraws were processed
