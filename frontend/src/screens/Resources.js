import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { useSelector, useDispatch } from "react-redux";
// import { acceptUser, rejectUser, deleteUser } from '../redux/userSlice';
import { validateUser, upgradeUser } from "../redux/userSlice";
import Navbar from "../components/Navbar";
import userService from "../services/user.service";
import { Navigate } from "react-router-dom";
import employeeService from "../services/employees.service";

const Container = styled.div`
  display: flex;
  flex-direction: column;
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

  @media (max-width: 800px) {
    display: block;
  }
`;

const Resource = styled.div`
  display: flex;
  flex-wrap: wrap;
  font-family: "Roboto";

  .user-data {
    border-radius: 3px;
    padding: 1rem 3.5rem;
    border: 1px solid black;
    background-color: rgb(164, 212, 204, 0.7);
    color: black;
    text-transform: uppercase;
    letter-spacing: 1px;
    text-align: center;
    font-size: 14px;
    margin: 10px;
  }

  h3 {
    font-size: 20px;
    padding: 1rem;
  }
`;

const Button = styled.button`
  border-radius: 3px;
  padding: 1rem;
  margin-top: 1rem;
  border: 1px solid black;
  color: black;
  text-transform: uppercase;
  cursor: pointer;
  letter-spacing: 1px;
  box-shadow: 0 3px #999;
  font-family: "Roboto";
  text-align: center;
  font-size: 14px;

  &.button-accept {
    background-color: #a4d4cc;
    margin-right: 1rem;

    &:hover {
      background-color: green;
    }
  }

  &.button-reject {
    background-color: #a4d4cc;

    &:hover {
      background-color: red;
    }
  }

  &.button-delete {
    background-color: #a4d4cc;

    &:hover {
      background-color: red;
    }
  }

  &.button-validate {
    background-color: #a4d4cc;
    margin-right: 1rem;

    &:hover {
      background-color: green;
    }
  }

  &.button-upgrade {
    background-color: #a4d4cc;
    margin-right: 1rem;

    &:hover {
      background-color: green;
    }
  }

  &:active {
    background-color: #a4d4cc;
    box-shadow: 0 3px #666;
    transform: translateY(4px);
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
  const { user: currentUser } = useSelector((state) => state.auth);
  const users = useSelector((state) => state.users);
  const clicked = useSelector((state) => state.navigation.clicked);

  const [allEmployees, setAllEmployees] = useState([]);

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

  const isAdminOrManager =
    currentUser &&
    (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");

  const dispatch = useDispatch();

  const employees = users.filter((user) => user.role === "EMPLOYEE");
  const validated = users.filter((user) => user.role === "VALIDATEDEMPLOYEE");
  const managers = users.filter((user) => user.role === "MANAGER");
  const admins = users.filter((user) => user.role === "ADMIN");

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

  const renderUser = (user) => (
    <div key={user.id} className="user-data">
      <h3 className="user-username">{user.username}</h3>
      <p className="user-email">{user.email}</p>
      <p className="user-role">{user.role}</p>
      {user.role === "EMPLOYEE" && (
        <div>
          <Button
            className="button-validate"
            onClick={() => handleValidate(user.id)}
          >
            Validate
          </Button>
          {/* <Button className="button-reject" onClick={() => handleReject(user.id)}>Reject</Button> */}
        </div>
      )}
      {user.role === "VALIDATEDEMPLOYEE" && (
        <div>
          <Button
            className="button-upgrade"
            onClick={() => handleUpgrade(user.id)}
          >
            Upgrade
          </Button>
          {/* <Button className="button-delete" onClick={() => handleDelete(user.id)}>Delete</Button> */}
        </div>
      )}
    </div>
  );

  return (
    <Container>
      <Navbar />
      <MainContent visible={contentVisible}>
        <Title>RESOURCES</Title>
        <UserContainer>
          {allEmployees.filter((user) => user.role === "EMPLOYEE").length >
            0 && (
            <UserItem>
              <Subheader>Employees</Subheader>
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
              <Subheader>Validated Employees</Subheader>
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
                  .map(renderUser)}
              </Resource>
            </UserItem>
          )}
        </UserContainer>
      </MainContent>
    </Container>
  );
}

export default Resources;
