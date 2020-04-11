package com.walmart.labs.ads.keyword.cache.simple;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlacklistedItemService {
    static final ConcurrentHashMap<Long, Long> placeholder = new ConcurrentHashMap<>();

    /**
     * A thread thread blacklisted item set.
     */
    private Set<Long> blackedListItems = placeholder.newKeySet();

    public void removeAll(Set<Integer> remove_set) {
        blackedListItems.removeAll(remove_set);
    }

    public void addAll(Set<Long> remove_set) {
        blackedListItems.addAll(remove_set);
    }

    public void remove(Long remove) {
        blackedListItems.remove(remove);
    }

    public void add(Long remove) {
        blackedListItems.add(remove);
    }

    public boolean notBlacklisted(long checked) {
        return !blackedListItems.contains(checked);
    }
}
