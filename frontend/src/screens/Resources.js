import React, { useState, useEffect } from "react";
import styled from "styled-components";
import "../Resources.css";
import { useSelector, useDispatch } from "react-redux";
// import { acceptUser, rejectUser, deleteUser } from '../redux/userSlice';
import { validateUser, upgradeUser } from "../redux/userSlice";
import Navbar from "../components/Navbar";
import userService from "../services/user.service";
import { Navigate } from "react-router-dom";
import employeeService from "../services/employees.service";
import user1 from '../images/users/user-1.png';
import user2 from '../images/users/user-2.png';
import user3 from '../images/users/user-3.png';
import user4 from '../images/users/user-4.png';
import user5 from '../images/users/user-5.png';
import user6 from '../images/users/user-6.png';
import user7 from '../images/users/user-7.png';

const userimages = [user1,user2,user3,user4,user5,user6,user7];

const Container = styled.div`
  /* display: flex;
  flex-direction: column; */
`;

const MainContent = styled.div`
  display: ${(props) => (props.visible ? "" : "none")};
  padding: 3rem;
`;

const Title = styled.h1`
  margin-top: 20px;
  color: white;
  font-family: "Roboto";
  font-size: 30px;
  margin-left: 10px;
`;

const UserContainer = styled.div`
  display: flex;
  margin-top: 20px;
  flex-wrap: wrap;
`;

const Resource = styled.div`
  display: flex;
  flex-wrap: wrap;
  font-family: "Roboto";
  gap: 2rem;
  margin: 1rem;
  /* margin: 2rem;
  display: grid;
  gap: 1rem;
  grid-template-columns: 1fr;
  justify-content: center; */
  text-transform: uppercase;

  @media screen and (min-width:600px){
    grid-template-columns: repeat(auto-fit, minmax(24rem, 26rem));
  }

  .card{
    background-color: rgb(164, 212, 204, 0.6);
    border-radius: 0.2rem;
    color: black;
    min-width: 28rem;
    display: flex;
    font-size: 1.4rem;
    text-align: center;
    overflow: hidden;
    align-items: center;

    @media screen and (min-width:600px){
      flex-direction: column;
      text-align: center;
    }

    @media(max-width: 600px){
      width: 100%;
      height: 8em;
    }
  }

  .card-body{
    flex-grow: 1;
    overflow: auto;
    margin: 1em;
  }

  .card-username{
  }

  .card-email{

  }
`;

const Button = styled.button`
  border: none;
  border-top: 1px solid rgb(164, 212, 204, 0.2);
  background-color: transparent;
  font-size: 2rem;
  font-weight: bold;
  color: black;
  width: 100%;
  cursor: pointer;
  text-transform: uppercase;
  padding-top: 1rem;
  margin-top: 0.5em;

  &:hover{
    color: green;
  }
`;

const UserImage = styled.img`
  width: 10rem;
  height: 100%;
  object-fit: cover;

  @media screen and (min-width:600px){
    width: 100%;
    height: 22rem;
    object-fit: cover;
  }
`;

const UserItem = styled.div`
  width: 100%;
`;

const Subheader = styled.h2`
  color: white;
  font-size: 24px;
  margin-left: 10px;
`;

function Resources() {
  const [randomImage, setRandomImage] = useState(null);

  const { user: currentUser } = useSelector((state) => state.auth);
  const clicked = useSelector((state) => state.navigation.clicked);

  const [allEmployees, setAllEmployees] = useState([]);
  const actualUserRole = currentUser.role;


  useEffect(() => {
    employeeService
      .getAllEmployees()
      .then((response) => {
        console.log(response.data);
        const employees = response.data;
        setAllEmployees(employees);
      })
      .catch((error) => {
        console.error("Error al obtener la lista de empleados:", error);
      });
  }, []);

  useEffect(() => {
    const randomIndex = Math.floor(Math.random() * userimages.length);
    setRandomImage(userimages[randomIndex]);
  }, []);

  const handleValidate = (id) => {
    employeeService.validateEmployee(id);
    window.location.reload();
    //dispatch(validateUser(id))
  };

  const handleUpgrade = (id) => {
    //dispatch(upgradeUser(id))
    employeeService.validateAdmin(id);
    window.location.reload();
  };

  const isAdminOrManager = currentUser && (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");

  const contentVisible = !clicked;

  // const handleReject = (id) => {
  //     dispatch(rejectUser(id))
  // };

  // const handleDelete = (id) => {
  //     dispatch(deleteUser(id))
  // };

  if (!currentUser) {
    return <Navigate to="/" />;
  }
  if (!isAdminOrManager) {
    return <Navigate to="/homepage" />;
  }

  const renderUser = (user) => {

    const randomIndex = Math.floor(Math.random() * userimages.length);
    const randomImage = userimages[randomIndex];

    return(
      <div key={user.id} className="card">

        <UserImage src={randomImage} alt={`User ${user.id}`} />

        <div className="card-body">
          <h2 className="card-username">{user.username}</h2>
          <p className="card-email">{user.email}</p>

          {user.role === "EMPLOYEE" && (
            <div>
              <Button
                className="button-validate"
                onClick={() => handleValidate(user.id)}
              >
                Validate
              </Button>
            </div>
          )}

          {user.role === "VALIDATEDEMPLOYEE" && (
            <div>
              { actualUserRole === "ADMIN" && (
              <Button
                className="button-upgrade"
                onClick={() => handleUpgrade(user.id)}
              >
                Upgrade
              </Button>
              )}
            </div>
          )}

        </div>

      </div>
  )};

  const renderAdmin = (user) => {

    const randomIndex = Math.floor(Math.random() * userimages.length);
    const randomImage = userimages[randomIndex];

    return(
      <div key={user.id} className="card">

        <UserImage src={randomImage} alt={`User ${user.id}`} />

        <div className="card-body">
          <h2 className="card-username">{user.username}</h2>
          {actualUserRole === "ADMIN" && <p className="card-email">{user.email}</p>}
        </div>

      </div>
  )};

  return (
    <Container>
      <Navbar />
      <MainContent visible={contentVisible}>
        <UserContainer>
          {allEmployees.filter((user) => user.role === "EMPLOYEE").length >
            0 && (
            <UserItem>
              <Subheader>Pending Approval</Subheader>
              <Resource> 
                {allEmployees
                  .filter((user) => user.role === "EMPLOYEE")
                  .map(renderUser)}
              </Resource>
            </UserItem>
          )}
          {allEmployees.filter((user) => user.role === "VALIDATEDEMPLOYEE")
            .length > 0 && (
            <UserItem>
              <Subheader>Employees</Subheader>
              <Resource>
                {allEmployees
                  .filter((user) => user.role === "VALIDATEDEMPLOYEE")
                  .map(renderUser)}
              </Resource>
            </UserItem>
          )}
          {allEmployees.filter((user) => user.role === "MANAGER").length >
            0 && (
            <UserItem>
              <Subheader>Managers</Subheader>
              <Resource>
                {allEmployees
                  .filter((user) => user.role === "MANAGER")
                  .map(renderUser)}
              </Resource>
            </UserItem>
          )}
          {allEmployees.filter((user) => user.role === "ADMIN").length > 0 && (
            <UserItem>
              <Subheader>Admins</Subheader>
              <Resource>
                {allEmployees
                  .filter((user) => user.role === "ADMIN")
                  .map(renderAdmin)}
              </Resource>
            </UserItem>
          )}
        </UserContainer>
      </MainContent>
    </Container>
  );
}

export default Resources;