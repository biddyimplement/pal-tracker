package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TimeEntryController {
    private final CounterService counter;
    private final GaugeService gauge;
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(
            TimeEntryRepository timeEntryRepository,
            CounterService counter,
            GaugeService gauge
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }


    @RequestMapping(value = "/time-entries", method = RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        System.out.println(timeEntry.toString());
        TimeEntry te = timeEntryRepository.create(timeEntry);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity(te, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/time-entries/{id}",  method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity read(@PathVariable(name = "id") Long id) {
        TimeEntry te = timeEntryRepository.find(id);

        if (te == null) {
            counter.increment("TimeEntry.read");
            return new ResponseEntity(te,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(te, HttpStatus.OK);
    }

    @RequestMapping(value = "/time-entries", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity list() {
        counter.increment("TimeEntry.listed");
        List<TimeEntry> timeEntryList = this.timeEntryRepository.list();

        return new ResponseEntity(timeEntryList, HttpStatus.OK);
    }

    @RequestMapping(value = "/time-entries/{id}", method = RequestMethod.PUT,consumes= MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(name = "id") Long id, @RequestBody  TimeEntry timeEntry) {
        TimeEntry te = this.timeEntryRepository.update(id, timeEntry);

        if (te == null) {
            counter.increment("TimeEntry.updated");
            return new ResponseEntity(te, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(te, HttpStatus.OK);
    }

    @RequestMapping(value = "/time-entries/{id}", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        this.timeEntryRepository.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
