@RetryDepositJob
Feature: RetryDepositJob
    Checks if the job for retrying deposits is correct for no pending deposits

    Scenario: Deposit job with no pending deposits
        Given there are no pending deposits
        When RetryDepositJob runs
        Then system will report that no deposits were processed
