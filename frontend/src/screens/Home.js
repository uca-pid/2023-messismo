import React from 'react'
import { styled } from 'styled-components';
// import 'fontsource-roboto';
import { useSelector } from 'react-redux';
import Navbar from "../components/Navbar";

const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
`;

const MainContent = styled.div`
    display: ${props => (props.visible ? 'flex' : 'none')};
    flex-direction: column;
    justify-content: center;
    align-items: center;
    flex-grow: 1;
`;

const WelcomeImage = styled.img`

`;

const Resource = styled.div`

    font-family: 'Roboto',serif;
    color: #a4d4cc;
    text-transform: uppercase;
    letter-spacing: 1px;
    text-align: center;
    font-size: 20px;
    margin-top: 5rem;

    h3{
        font-size: 40px;

    }
    p{
        padding: 1rem;
    }
    
`;


// const signedinuser = [
//     { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'user', pending: 'no' }
//   ];

function HomePage(){

    const clicked = useSelector((state) => state.navigation.clicked);
    const contentVisible = !clicked;

    const signedInUser = useSelector(state => state.login)

    // const renderUser = (user) => (
    //     <div key={user.id}>
    //       <h3>{user.username}</h3>
    //       <p>{user.email}</p>
    //     </div>
    // );

    return(
        <Container>

            <Navbar />

            <MainContent visible={contentVisible}>
                <WelcomeImage src="/images/welcomeback2.png"/>
                <Resource>
                    {signedInUser.email}
                </Resource>
            </MainContent>

        </Container>
    )
}

export default HomePage;