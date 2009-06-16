package org.grails.util

class DateServiceTests extends GroovyTestCase {
    
    def dateService = new DateService()
    
    void testCalendarMonths() {
        def date = new GregorianCalendar(2003, 0, 1).time
        assertEquals "Jan", dateService.getMonthString(date)
        date = new GregorianCalendar(2003, 6, 1).time
        assertEquals "July", dateService.getMonthString(date)
    }
    
    void testGetDayOfMonth() {
        def date = new GregorianCalendar(2003, 0, 21).time
        assertEquals 21, dateService.getDayOfMonth(date)
        date = new GregorianCalendar(2003, 6, 4).time
        assertEquals 4, dateService.getDayOfMonth(date)
    }
    
}