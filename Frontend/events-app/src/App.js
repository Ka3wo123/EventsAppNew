
import { Typography } from '@mui/material';
import './App.css';
import { Outlet } from 'react-router-dom';

import MainPage from './components/MainPage';

function App() {

  return (
    <div>      
      <Outlet/>
    </div>

  );
}

export default App;
