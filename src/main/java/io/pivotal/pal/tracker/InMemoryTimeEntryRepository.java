package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntries = new ConcurrentHashMap<>();
    private AtomicLong lastId = new AtomicLong();

    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() != 0) {
            throw new IllegalStateException("ID cannot be set");
        }

        timeEntry.setId(lastId.incrementAndGet());

        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntries.get(id);
    }

    public TimeEntry read(long id) {
        return timeEntries.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry oldTimeEntry = read(id);

        if (oldTimeEntry != null) {
            oldTimeEntry.setDate(timeEntry.getDate());
            oldTimeEntry.setHours(timeEntry.getHours());
            oldTimeEntry.setProjectId(timeEntry.getProjectId());
            oldTimeEntry.setUserId(timeEntry.getUserId());
        }

        return oldTimeEntry;
    }

    public TimeEntry delete(long id) {
        return timeEntries.remove(id);
    }
}
