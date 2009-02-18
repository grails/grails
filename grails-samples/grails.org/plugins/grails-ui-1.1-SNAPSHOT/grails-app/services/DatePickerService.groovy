/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class DatePickerService {
    static transactional = false
    
    def calendarSplit = { cal ->
        def ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? 'AM' : 'PM'
        // make 0 hours 12 for logical reading
        def hour = cal.get(Calendar.HOUR)
        if (hour == 0) hour = 12 
        return [
            year:cal.get(Calendar.YEAR),
            month:cal.get(Calendar.MONTH)+1,
            day:cal.get(Calendar.DAY_OF_MONTH),
            hour:hour,  
            minute:cal.get(Calendar.MINUTE),
            second:cal.get(Calendar.SECOND),
            ampm:ampm
        ]
    }
}