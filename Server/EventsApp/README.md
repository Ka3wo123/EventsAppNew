### Endpoints for user
- ```localhost:8080/event-app/user``` for create new user
- ```localhost:8080/event-app/user/validate?email=&password=``` for authenticate user
- ```localhost:8080/event-app/user/add-event?email=&name=``` for assign user to event
- ```localhost:8080/event-app/user/delete-event?email=&name=``` for dicharge user from event

### Endpoints for event
- ```localhost:8080/event-app/events?user-email=``` for retrieve events for user who is assigned to
- ```localhost:8080/event-app/events``` for retreive all events
- ```localhost:8080/event-app/event/delete?name=``` for delete event
- ```localhost:8080/event-app/event/add``` for add new event

# IMPORTANT!!!
When deleting specific event from this endpoint ```localhost:8080/event-app/event/delete?name=``` assigned users are automatically discharged from it. (delete on cascade)

