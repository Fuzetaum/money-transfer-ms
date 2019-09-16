package com.revolut.test.backend.ricardofuzeto.model;

public class JobResponsePojo {
    private Integer totalCount;
    private Integer changedCount;
    private Integer notChangedCount;

    public JobResponsePojo(Integer totalCount, Integer changedCount) {
        this.totalCount = totalCount;
        this.changedCount = changedCount;
        this.notChangedCount = totalCount - changedCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(Integer changedCount) {
        this.changedCount = changedCount;
        this.notChangedCount = this.totalCount - changedCount;
    }

    public Integer getNotChangedCount() {
        return notChangedCount;
    }

    public void setNotChangedCount(Integer notChangedCount) {
        this.notChangedCount = notChangedCount;
        this.changedCount = this.totalCount - notChangedCount;
    }

    @Override
    public String toString() {
        return "JobResponsePojo{" +
                "totalCount=" + totalCount +
                ", changedCount=" + changedCount +
                ", notChangedCount=" + (totalCount - changedCount) +
                '}';
    }
}
