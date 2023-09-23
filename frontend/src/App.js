import "./App.css"
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Welcome from './screens/Welcome';
import SignInUpForm from './screens/SignInUpForm';
import Home from './screens/Home';
import Products from './screens/Products';
import Resources from './screens/Resources';
import ProductsList from "./components/ProductsList";

function App(){
    return(
        <div className="App">

            <BrowserRouter>
                <Routes>
                    <Route path='/' element={ <Welcome /> } />
                    <Route path='/signinupform' element={ <SignInUpForm/> } />
                    <Route path='/homepage' element={ <Home/> } />
                    <Route path='/products' element={ <Products/> } />
                    <Route path='/resources' element={ <Resources/> } />
                </Routes>
            </BrowserRouter>

        </div>
    )
}

export default App;