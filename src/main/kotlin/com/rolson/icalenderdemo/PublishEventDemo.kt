package com.rolson.icalenderdemo

import biweekly.ICalVersion
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.io.text.ICalWriter
import biweekly.property.Method
import biweekly.property.Status
import biweekly.property.Transparency
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


fun main(args: Array<String>) {
    val uid = UUID.randomUUID().toString()
    createInitialInvite(uid)
    createUpdatedInvite(uid)
    createCancelledInvite(uid)
}

private fun createInitialInvite(uid: String) {
    val start = Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
    val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))
    val sequence = 0

    val event = createEvent(uid, start, end, sequence, false)
    val ical = createCalendar(Method.PUBLISH, event)

    val file = File("publish_invite.ics")
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createUpdatedInvite(uid: String) {
    val start = Date.from(Instant.now().plus(2, ChronoUnit.DAYS))
    val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))
    val sequence = 1

    val event = createEvent(uid, start, end, sequence, false)
    val ical = createCalendar(Method.PUBLISH, event)

    val file = File("publish_update.ics")
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createCancelledInvite(uid: String) {
    val sequence = 2

    val event = createCancelledEvent(uid, sequence)
    val ical = createCalendar(Method.CANCEL, event)

    val file = File("publish_cancel.ics")
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createCalendar(
        calMethod: String,
        event: VEvent,
): ICalendar {
    val ical = ICalendar()
    ical.setProductId("-//rolson//ICalendar Demo 1.0.0//EN")
    ical.setMethod(calMethod)
    ical.addEvent(event)

    assert(ical.validate(ICalVersion.V2_0).isEmpty)

    return ical
}

private fun createEvent(
        uid: String,
        start: Date?,
        end: Date?,
        sequence: Int,
        cancelled: Boolean
): VEvent {
    val event = VEvent()
    event.setUid(uid)

    // event details
    event.setSummary("Simple Publish Event")
    event.setDescription("Simple publish invite to my event")
    event.setOrganizer("no-reply@gmail.com")

    // date & time
    if (start != null) event.setDateStart(start)
    if (end != null) event.setDateEnd(end)

    // status must be CANCELLED to cancel an event
    event.status = if (cancelled) Status.cancelled() else Status.confirmed()

    // sequence must be > 0 for an updated event
    event.setSequence(sequence)

    // setting to consume time on a recipient's calendar
    event.transparency = Transparency.opaque()

    return event
}

private fun createCancelledEvent(
        uid: String,
        sequence: Int
): VEvent {
    return createEvent(uid, null, null, sequence, true)
}