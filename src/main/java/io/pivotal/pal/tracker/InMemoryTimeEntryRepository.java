package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long,TimeEntry> timeEntryRepo = new HashMap<Long,TimeEntry>();
    private long count = 0;

    public TimeEntry find(long id) {

        return timeEntryRepo.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> entryList = new ArrayList<TimeEntry>(timeEntryRepo.values());
        return entryList;
    }

    public void delete(long id) {
        timeEntryRepo.remove(id);
    }

    public TimeEntry create(TimeEntry timeEntry){
        timeEntry.setId(++count);
        timeEntryRepo.put(count,timeEntry);
        return timeEntry;
    }

    public TimeEntry update(long timeEntryId,TimeEntry timeEntry){
        TimeEntry updateEntry = find(timeEntryId);
        if(updateEntry!=null) {
            timeEntry.setId(timeEntryId);
            timeEntryRepo.put(timeEntryId, timeEntry);
        }else{
            timeEntry=null;
        }
        return timeEntry;
    }
}
