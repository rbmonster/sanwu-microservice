package com.sanwu.gateway.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: Rate
 * @Author: sanwu
 * @Date: 2020/12/13 17:08
 */

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @Column(name = "rate_key", nullable = false)
    private String rateKey;

    @Column(name = "remaining")
    private long remaining;

    @Column(name = "remaining_quota")
    private long remainingQuota;

    @Column(name = "reset")
    private long reset;

    public String getRateKey() {
        return rateKey;
    }

    public void setRateKey(String rateKey) {
        this.rateKey = rateKey;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }

    public long getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(long remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public long getReset() {
        return reset;
    }

    public void setReset(long reset) {
        this.reset = reset;
    }
}
