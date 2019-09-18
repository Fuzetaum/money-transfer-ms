package com.revolut.test.backend.ricardofuzeto.model;

public class JobRequestPojo {
    private Integer replicas;
    private Integer instance;

    public JobRequestPojo(Integer replicas, Integer instance) {
        this.replicas = replicas;
        this.instance = instance;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "JobRequestPojo{" +
                "replicas=" + replicas +
                ", instance=" + instance +
                '}';
    }
}
