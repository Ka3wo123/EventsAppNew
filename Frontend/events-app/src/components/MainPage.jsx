import { Typography } from "@mui/material";
import axios from 'axios';
import { useState, useEffect } from 'react';


 const MainPage = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        fetchEvents();
    }, []);

    const fetchEvents = () => {
        axios.get('http://localhost:8080/event-app/events')
            .then(response => {
                console.log('Events', response.data);
                setData(response.data);
            }).catch(err => {
                console.log(err);
            });
    }
    return (
        <div className="events-container">
            <Typography variant="h3">List of Events</Typography>            
            <ul>
                {data.map(event => (
                    <li key={event.id}>
                        {event.name} - {event.dateOfEvent}
                    </li>
                ))}
            </ul>
        </div>
    )
};


export default MainPage;