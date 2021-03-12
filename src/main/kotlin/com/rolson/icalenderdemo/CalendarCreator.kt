package com.rolson.icalenderdemo

import biweekly.ICalVersion
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.io.text.ICalWriter
import biweekly.parameter.CalendarUserType
import biweekly.parameter.ParticipationLevel
import biweekly.parameter.ParticipationStatus
import biweekly.parameter.Role
import biweekly.property.Attendee
import biweekly.property.Method
import biweekly.property.Status
import biweekly.property.Transparency
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

const val DEFAULT_METHOD: String = Method.REQUEST
const val INVITE_FILE: String = "invite.ics"
const val UPDATED_EVENT_FILE: String = "update.ics"
const val CANCEL_EVENT_FILE: String = "cancel.ics"
val EMAILS: List<String> = arrayListOf("rodneyolson1@gmail.com", "rodolson96@outlook.com")

fun main(args: Array<String>) {
    val uid = UUID.randomUUID().toString()
    createInitialInvite(uid)
    createUpdatedInvite(uid)
    createCancelledInvite(uid)
}

private fun createInitialInvite(
        uid: String
) {
    val start = Date.from(Instant.now().plus(1, ChronoUnit.DAYS))
    val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))
    val sequence = 0
    val attendees = createAttendees(EMAILS)

    val event = createEvent(uid, start, end, sequence, false, attendees)
    val ical = createCalendar(DEFAULT_METHOD, event)

    val file = File(INVITE_FILE)
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createUpdatedInvite(
        uid: String
) {
    val start = Date.from(Instant.now().plus(2, ChronoUnit.DAYS))
    val end = Date.from(start.toInstant().plus(1, ChronoUnit.HOURS))
    val sequence = 1
    val attendees = createAttendees(EMAILS)

    val event = createEvent(uid, start, end, sequence, false, attendees)
    val ical = createCalendar(DEFAULT_METHOD, event)

    val file = File(UPDATED_EVENT_FILE)
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createCancelledInvite(
        uid: String
) {
    val sequence = 2
    val attendees = createAttendees(EMAILS)

    val event = createCancelledEvent(uid, sequence, attendees)
    val ical = createCalendar(Method.CANCEL, event)

    val file = File(CANCEL_EVENT_FILE)
    val writer = ICalWriter(file, ICalVersion.V2_0)
    writer.write(ical)
    writer.close()
}

private fun createCalendar(
        calMethod: String,
        event: VEvent
): ICalendar {
    val ical = ICalendar()
    ical.setProductId("-//rolson//ICalendar Demo 1.0.0//EN")
    ical.setMethod(calMethod)
    ical.addEvent(event)

    assert(ical.validate(ICalVersion.V2_0).isEmpty)

    return ical
}

private fun createCancelledEvent(
        uid: String,
        sequence: Int,
        attendees: List<Attendee>
): VEvent {
    return createEvent(
            uid, null, null, sequence, true, attendees
    )
}

private fun createEvent(
        uid: String,
        start: Date?,
        end: Date?,
        sequence: Int,
        cancelled: Boolean,
        attendees: List<Attendee>
): VEvent {
    val event = VEvent()
    event.setUid(uid)

    // event details
    event.setSummary("Simple Event")
    event.setDescription("Simple invite to my event")
    event.setOrganizer("no-reply@gmail.com")

    // date & time
    if (start != null) event.setDateStart(start)
    if (end != null) event.setDateEnd(end)

    // attendees
    // Required for REQUEST
    // Must be empty for PUBLISH
    for (attendee in attendees) event.addAttendee(attendee)

    // status must be CANCELLED to cancel an event
    event.status = if (cancelled) Status.cancelled() else Status.confirmed()

    // sequence must be > 0 for an updated event
    event.setSequence(sequence)

    // setting to consume time on a recipient's calendar
    event.transparency = Transparency.opaque()

    return event
}

private fun createAttendees(
        emails: List<String>
): List<Attendee> {
    val attendeeList: MutableList<Attendee> = ArrayList()

    for (email in emails) {
        val name = email.split("@")[0]
        val attendee = Attendee(name, email)
        attendee.calendarUserType = CalendarUserType.INDIVIDUAL
        attendee.role = Role.ATTENDEE
        attendee.participationStatus = ParticipationStatus.ACCEPTED // force accept invite
        attendee.participationLevel = ParticipationLevel.REQUIRED
        attendee.rsvp = true

        attendeeList.add(attendee)
    }

    return attendeeList
}