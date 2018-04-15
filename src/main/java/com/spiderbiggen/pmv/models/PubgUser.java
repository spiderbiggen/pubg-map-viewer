package com.spiderbiggen.pmv.models;

import java.util.Objects;

public class PubgUser implements Comparable<PubgUser> {

    private final String username;
    private final String accountId;

    public PubgUser(String username, String accountId) {
        this.username = username;
        this.accountId = accountId;
    }

    /**
     * Gets username
     *
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets accountId
     *
     * @return value of accountId
     */
    public String getAccountId() {
        return accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAccountId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PubgUser pubgUser = (PubgUser) o;
        return Objects.equals(getUsername(), pubgUser.getUsername()) &&
                Objects.equals(getAccountId(), pubgUser.getAccountId());
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public int compareTo(PubgUser o) {
        return getUsername().compareTo(o.getUsername());
    }
}
