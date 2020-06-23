// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.ArrayList;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) { 
        
        // Declare varibles for information from request
        Collection<String> requestAttendees = request.getAttendees();
        long durationOfRequest = request.getDuration();
        Collection<String> optionalAttendees = request.getOptionalAttendees();

        if(optionalAttendees.size() > 0){
            
            // Create a list of all attendees
            Collection<String> allAttendees = new ArrayList<String>();

            for(String attendee : requestAttendees){
                allAttendees.add(attendee);
            }

            for(String attendee : optionalAttendees){
                allAttendees.add(attendee);
            }

            // Find all TimeRanges that will not work and then sort them
            ArrayList<TimeRange> AllUnavailableTimes = unavailableMeetingTimes(events, allAttendees);
            Collections.sort(AllUnavailableTimes, TimeRange.ORDER_BY_START);

            // Create availabe times for all attendees
            Collection<TimeRange> AlllAvailableTimes = availableTimes(AllUnavailableTimes, durationOfRequest);
        
            if(AlllAvailableTimes.size() > 0 || requestAttendees.size() == 0){
                return AlllAvailableTimes;
            }
        }

        // Find all TimeRanges that will not work and then sort them
        ArrayList<TimeRange> unavailableTimes = unavailableMeetingTimes(events, requestAttendees);
        Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

        // Create availabe times for required attendees
        Collection<TimeRange> RequiredAttendeeAvailableTimes = availableTimes(unavailableTimes, durationOfRequest);
        
        return RequiredAttendeeAvailableTimes;
    }

    /* Checks if attendee for meeting requset is in a certain event */ 
    private Boolean areAttendeesInEvent(Collection<String> meetingAttendees, Collection<String> eventAttendees){
        
        for (String attendee : meetingAttendees) {
        
            if (eventAttendees.contains(attendee)){

                return true;
        
            }

        }

        return false;
    }

    /* Returns a Collection of all the TimeRanges that will not work for attendees. */
    private ArrayList<TimeRange> unavailableMeetingTimes(Collection<Event> events, Collection<String> meetingAttendees){

        // Create the Collection for TimeRanges unavailable 
        ArrayList<TimeRange> UnavailableTimes = new ArrayList<TimeRange>();

        // Loop through all the events and check if attendees are in an event
        for(Event event: events){

            Collection<String> eventAttendees = event.getAttendees();
            TimeRange eventTimeRange = event.getWhen();
                
            if(areAttendeesInEvent(meetingAttendees, eventAttendees)){
                    
                UnavailableTimes.add(eventTimeRange);

            }

        }
            
        return UnavailableTimes;
    }

    public Collection<TimeRange> availableTimes(ArrayList<TimeRange> unavailableMeetingTimes, long durationOfRequest) { 
        Collection<TimeRange> availableTimes = new ArrayList<TimeRange>();
        int totalMinutes = 24 * 60;

        // Variable that will hold the end time of meetings. Starts at 0 since it marks the beginning of the day
        int possibleMeetingStart  = 0; 

        // Check for possible times between meetings
        for (TimeRange eventTime : unavailableMeetingTimes){

            int unavailableTimeStart = eventTime.start();
            int unavailableTimeEnd = eventTime.end();

            if (possibleMeetingStart + durationOfRequest <= unavailableTimeStart){ 

                TimeRange availableTime = TimeRange.fromStartEnd(possibleMeetingStart, unavailableTimeStart, false);
                availableTimes.add(availableTime);
            
            }
        
            // Changes variable to next possible starting time
            possibleMeetingStart = Math.max(unavailableTimeEnd,possibleMeetingStart);       
       
        }

        // Check if there is available time between the end of the last meeting and the end of the day
        if (possibleMeetingStart + durationOfRequest <= totalMinutes){

            TimeRange restOfDay = TimeRange.fromStartEnd(possibleMeetingStart, totalMinutes, false);
            availableTimes.add(restOfDay);

        }

        return availableTimes;
    }
}