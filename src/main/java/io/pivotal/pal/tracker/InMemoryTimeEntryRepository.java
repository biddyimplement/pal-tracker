package io.pivotal.pal.tracker;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;


public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private int timeEntryCounter;
    Map<Long,TimeEntry> timeEntryMap;

    public InMemoryTimeEntryRepository() {
        this.timeEntryMap = new HashMap<>();
        this.timeEntryCounter = 1;
    }

    public TimeEntry find(Long id) {
        return this.timeEntryMap.get(id);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry actualTimeEntry = new TimeEntry(this.timeEntryCounter++,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
        this.timeEntryMap.put(actualTimeEntry.getId(),actualTimeEntry);
        return actualTimeEntry;
    }

    public List list() {
      List timeEntryList = new ArrayList(this.timeEntryMap.values());
      Collections.sort(timeEntryList);
      return timeEntryList;
    }

    public void delete(Long id) {
        if(this.timeEntryMap.containsKey(id)){
            this.timeEntryMap.remove(id);
        }
    }

    public TimeEntry update(Long id, TimeEntry timeEntry) {
        if(this.timeEntryMap.containsKey(id)) {
            TimeEntry timeEntryToUpdate = this.timeEntryMap.get(id);
            timeEntryToUpdate.setUserId(timeEntry.getUserId());
            timeEntryToUpdate.setProjectId(timeEntry.getProjectId());
            timeEntryToUpdate.setDate(timeEntry.getDate());
            timeEntryToUpdate.setHours(timeEntry.getHours());
            this.timeEntryMap.put(id, timeEntryToUpdate);
            return timeEntryToUpdate;
        }else{
            return null;
        }
    }
}
