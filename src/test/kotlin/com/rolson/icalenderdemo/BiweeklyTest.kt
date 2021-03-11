package com.rolson.icalenderdemo

import biweekly.ICalVersion
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.io.text.ICalWriter
import biweekly.property.Attendee
import biweekly.property.Method
import biweekly.property.Status
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class BiweeklyTest {

    @Test
    fun createStandardRequestEvent() {

        // invite ics
        val ical = ICalendar()
        ical.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        ical.setMethod(Method.REQUEST)

        val event = VEvent()
        event.setSummary("Simple Event")

        val start = Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
        val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))

        event.setDateStart(start)
        event.setDateEnd(end)
        event.addAttendee(Attendee("rod", "rodneyolson1@gmail.com"))
        event.addAttendee(Attendee("rod2", "rodolson96@outlook.com"))

        event.setOrganizer("rolson@indeed.com")

        val uid = UUID.randomUUID().toString()

        event.setUid(uid)
        event.setDescription("Simple invite to my event")
        event.status = Status.confirmed()
        event.setSequence(0)

        ical.addEvent(event)
        assert(ical.validate(ICalVersion.V2_0).isEmpty)

        val file = File("invite.ics")
        val writer = ICalWriter(file, ICalVersion.V2_0)
        writer.write(ical)
        writer.close()

        // update ics
        val updateIcs = ICalendar()
        ical.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        ical.setMethod(Method.REQUEST)

        val updateEvent = VEvent()
        updateEvent.setSummary("Simple Event")

        val newStart = Date.from(Instant.now().plus(2, ChronoUnit.DAYS))
        val newEnd = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))

        updateEvent.setDateStart(newStart)
        updateEvent.setDateEnd(newEnd)
        updateEvent.addAttendee(Attendee("rod", "rodneyolson1@gmail.com"))
        updateEvent.addAttendee(Attendee("rod2", "rodolson96@outlook.com"))

        event.setOrganizer("rolson@indeed.com")

        updateEvent.setUid(uid)
        updateEvent.setDescription("Simple invite to my event")
        updateEvent.setSequence(1)
        updateEvent.status = Status.confirmed()

        updateIcs.addEvent(event)
        assert(updateIcs.validate(ICalVersion.V2_0).isEmpty)

        val updatefile = File("update.ics")
        val updatewriter = ICalWriter(updatefile, ICalVersion.V2_0)
        updatewriter.write(updateIcs)
        updatewriter.close()

        // cancel ics
        val cancelIcs = ICalendar()
        cancelIcs.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        cancelIcs.setMethod(Method.CANCEL)

        val cevent = VEvent()
        cevent.setSummary("Simple Event")
        cevent.status = Status.cancelled()
        cevent.addComment("Reason to cancel")

        event.setOrganizer("rolson@indeed.com")

        cevent.setUid(uid)
        cevent.setDescription("Simple invite to my event")
        cevent.setSequence(2)

        cancelIcs.addEvent(event)
        assert(cancelIcs.validate(ICalVersion.V2_0).isEmpty)

        val cfile = File("cancel.ics")
        val cwriter = ICalWriter(cfile, ICalVersion.V2_0)
        cwriter.write(cancelIcs)
        cwriter.close()
    }

    @Test
    fun createStandardPublishEvent() {

        // invite ics
        val ical = ICalendar()
        ical.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        ical.setMethod(Method.PUBLISH)

        val event = VEvent()
        event.setSummary("Simple Event")

        val start = Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
        val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))

        event.setDateStart(start)
        event.setDateEnd(end)

        event.setOrganizer("rolson@indeed.com")

        val uid = UUID.randomUUID().toString()

        event.setUid(uid)
        event.setDescription("Simple invite to my event")
        event.status = Status.confirmed()
        event.setSequence(0)

        ical.addEvent(event)
        assert(ical.validate(ICalVersion.V2_0).isEmpty)

        val file = File("invite.ics")
        val writer = ICalWriter(file, ICalVersion.V2_0)
        writer.write(ical)
        writer.close()

        // update ics
        val updateIcs = ICalendar()
        ical.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        ical.setMethod(Method.PUBLISH)

        val updateEvent = VEvent()
        updateEvent.setSummary("Simple Event")

        val newStart = Date.from(Instant.now().plus(2, ChronoUnit.DAYS))
        val newEnd = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))

        updateEvent.setDateStart(newStart)
        updateEvent.setDateEnd(newEnd)

        event.setOrganizer("rolson@indeed.com")

        updateEvent.setUid(uid)
        updateEvent.setDescription("Simple invite to my event")
        updateEvent.setSequence(1)
        updateEvent.status = Status.confirmed()

        updateIcs.addEvent(event)
        assert(updateIcs.validate(ICalVersion.V2_0).isEmpty)

        val updatefile = File("update.ics")
        val updatewriter = ICalWriter(updatefile, ICalVersion.V2_0)
        updatewriter.write(updateIcs)
        updatewriter.close()

        // cancel ics
        val cancelIcs = ICalendar()
        cancelIcs.setProductId("-//Indeed//Indeed Resume 1.0.0//EN")
        cancelIcs.setMethod(Method.CANCEL)

        val cevent = VEvent()
        cevent.setSummary("Simple Event")
        cevent.status = Status.cancelled()
        cevent.addComment("Reason to cancel")

        event.setOrganizer("rolson@indeed.com")

        cevent.setUid(uid)
        cevent.setDescription("Simple invite to my event")
        cevent.setSequence(2)

        cancelIcs.addEvent(event)
        assert(cancelIcs.validate(ICalVersion.V2_0).isEmpty)

        val cfile = File("cancel.ics")
        val cwriter = ICalWriter(cfile, ICalVersion.V2_0)
        cwriter.write(cancelIcs)
        cwriter.close()
    }
}