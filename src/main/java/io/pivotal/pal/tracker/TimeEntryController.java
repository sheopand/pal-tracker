package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class TimeEntryController {

    //@Autowired
    private TimeEntryRepository repo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.repo = timeEntryRepository;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry entry = repo.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(repo.list().size());
        ResponseEntity<TimeEntry> responseEntity = new ResponseEntity<TimeEntry>(entry,HttpStatus.CREATED);
        return responseEntity;
    }


    @GetMapping("time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {

        TimeEntry entryList = repo.find(timeEntryId);
        ResponseEntity<TimeEntry> responseEntity;
        if(entryList == null){
            actionCounter.increment();
            responseEntity = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        else{
            responseEntity = new ResponseEntity<TimeEntry>(entryList,HttpStatus.OK);
        }
        //responseEntity = new ResponseEntity<TimeEntry>(entryList,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> entryList = repo.list();
        actionCounter.increment();
        ResponseEntity<List<TimeEntry>> responseEntity = new ResponseEntity<List<TimeEntry>>(entryList,HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable  long timeEntryId, @RequestBody TimeEntry expected) {
        //TimeEntry entryList = repo.find(timeEntryId);
        ResponseEntity<TimeEntry> responseEntity;

            TimeEntry entry = repo.update(timeEntryId,expected);
            if(entry!=null) {
                actionCounter.increment();
                responseEntity = new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
            }else{
                responseEntity=new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       // ResponseEntity<TimeEntry> responseEntity = new ResponseEntity<TimeEntry>(entry,HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable  long timeEntryId) {
        repo.delete(timeEntryId);
        actionCounter.increment();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }
}
