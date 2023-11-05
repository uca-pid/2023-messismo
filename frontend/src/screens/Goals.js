import "../App.css";
import React, { useState, useEffect, useMemo } from "react";
import styled from "styled-components";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import GoalForm from "../components/GoalForm";
import ordersService from "../services/orders.service";
import goalsService from "../services/goals.service";
import { useTheme } from "@mui/material/styles";
import { Box, Typography, gridClasses, useMediaQuery } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { MdFastfood } from "react-icons/md";
import { AiFillDelete } from "react-icons/ai";
import { AiFillCheckCircle } from "react-icons/ai";
import { AiFillCloseCircle } from "react-icons/ai";
import moment from "moment";
import EditIcon from "@mui/icons-material/Edit";
import EditGoalForm from "../components/EditGoalForm";
import ModifyForm from "../components/modifyForm";
import VisibilityIcon from "@mui/icons-material/Visibility";
import Fab from "@mui/material/Fab";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import CircularProgress from "@mui/material/CircularProgress";
import Doughnut from "../components/DoughnutChart";
import GoalGauge from "../components/GoalGauge";

const Container = styled.div``;

const Button = styled.button`
  display: block;
  font-size: 1.2rem;
  border-radius: 3px;
  padding: 1rem 3.5rem;
  margin-top: 3rem;
  border: 1px solid black;
  background-color: #a4d4cc;
  color: black;
  text-transform: uppercase;
  cursor: pointer;
  letter-spacing: 1px;
  box-shadow: 0 3px #999;
  font-family: "Roboto", serif;
  text-align: center;

  &:hover {
    background-color: #a7d0cd;
  }
  &:active {
    background-color: #a4d4cc;
    box-shadow: 0 3px #666;
    transform: translateY(4px);
  }
  &:focus {
    outline: none;
  }

  @media (max-width: 477px) {
    margin-top: 1rem;
    font-size: 1rem;
    padding: 1rem 2.5rem;
  }
`;

const MainContent = styled.div`
  display: ${(props) => (props.visible ? "" : "none")};
  width: 100%;
  margin: auto;

  @media (min-width: 768px) {
    width: 80%;
  }
`;

const Modal = styled.div`
  display: ${(props) => (props.open ? "flex" : "none")};
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  z-index: 9999;
`;

const ModalContent = styled.div`
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  max-height: 60vh;
  overflow-y: auto;
`;

const OrdersTable = styled.div``;

const Details = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  z-index: 9999;
  padding: 1rem;
`;

const DetailsContent = styled.div`
  padding: 2rem;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  max-height: 60vh;
  overflow-y: auto;
  width: 20%;
  margin: auto;
  background-color: black;
  strong {
    color: white;
    font-family: "Roboto";
    font-size: 2rem;
  }

  @media (max-width: 1500px) {
    width: 30%;
  }
  @media (max-width: 1000px) {
    width: 40%;
  }
  @media (max-width: 800px) {
    width: 50%;
  }
`;

const DetailsButton = styled.button`
  display: block;
  width: 100%;
  font-size: 1.5rem;
  border-radius: 3px;
  padding: 1rem 3.5rem;
  border: 1px solid black;
  background-color: #a4d4cc;
  color: black;
  text-transform: uppercase;
  cursor: pointer;
  letter-spacing: 1px;
  box-shadow: 0 3px #999;
  font-family: "Roboto", serif;
  text-align: center;

  &:hover {
    background-color: #a7d0cd;
  }
  &:active {
    background-color: #a4d4cc;
    box-shadow: 0 3px #666;
    transform: translateY(4px);
  }
  &:focus {
    outline: none;
  }
`;

const MoreDetails = styled(MdFastfood)`
  cursor: pointer;
  @media (max-width: 1000px) {
    font-size: 1.5rem;
  }
  @media (max-width: 760px) {
    font-size: 1rem;
  }
  @media (max-width: 500px) {
    font-size: 0.8rem;
  }
`;

const DoughnutDiv = styled.div`
    max-width: 50%;
    display: flex;

    @media (max-width: 1000px) {
        max-width: 90%;
        height: auto;
        flex-direction: column;
    }

`;

const MOBILE_COLUMNS = {
  id: true,
  dateCreated: true,
  totalPrice: true,
  details: false,
  status: true,
};
const ALL_COLUMNS = {
  id: true,
  username: true,
  dateCreated: true,
  totalPrice: true,
  details: true,
  status: true,
};

const initialRows = [
  {
    "id": 3,
    "name": "Product: Fried Calamari goal",
    "startingDate": "2023-08-02T03:00:01.000+00:00",
    "endingDate": "2023-08-03T03:00:01.000+00:00",
    "objectType": "Product",
    "goal": "8600/15000",
    "goalObject": "Fried Calamari",
    "status": "Expired",
    "achieved": "Not Achieved"
  }
];

function Goals() {
  const { user: currentUser } = useSelector((state) => state.auth);
  const clicked = useSelector((state) => state.navigation.clicked);
  const [isGoalFormVisible, setGoalFormVisible] = useState(false);
  const [isDetailsVisible, setDetailsVisible] = useState(false);
  const [open, setOpen] = React.useState(false);
  const [orders, setGoals] = useState([]);
  const [selectedOrderDetails, setSelectedOrderDetails] = useState(null);
  const [pageSize, setPageSize] = useState(5);
  const isAdminOrManager =
    currentUser &&
    (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");
  const [openEditForm, setOpenEditForm] = useState(false);
  const [isEditFormVisible, setEditFormVisible] = useState(false);
  const [orderIdToEdit, setOrderIdToEdit] = useState(null);
  const [selectedTotalPrice, setSelectedTotalPrice] = useState(null);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [alertText, setAlertText] = useState("");
  const [isOperationSuccessful, setIsOperationSuccessful] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedRow, setSelectedRow] = useState(initialRows.find(row => row.id === 3));



  const theme = useTheme();
  const matches = useMediaQuery(theme.breakpoints.up("sm"));

  const [columnVisible, setColumnVisible] = useState(ALL_COLUMNS);
  useEffect(() => {
    const newColumns = matches ? ALL_COLUMNS : MOBILE_COLUMNS;
    setColumnVisible(newColumns);
  }, [matches]);

  useEffect(() => {

    const goalData = {
        status: [], 
        achieved: [],
    };

    goalsService
      .getAllGoals(goalData)
      .then((response) => {
        setGoals(response.data);
        setIsLoading(false);
        console.log("done");
      })
      .catch((error) => {
        console.error("Error al mostrar las metas", error);
        setIsLoading(false);
      });
  }, [isGoalFormVisible, open, isEditFormVisible, openEditForm]);

  if (!currentUser) {
    return <Navigate to="/" />;
  }

  const contentVisible = !clicked;

  const handleAddOrderClick = () => {
    setGoalFormVisible(true);
    setOpen(true);
  };

  const handleCloseGoalForm = () => {
    setGoalFormVisible(false);
    setOpen(false);
  };

  const handleCloseEditGoalForm = () => {
    setEditFormVisible(false);
    setOpenEditForm(false);
    setOpen(false);
  };

  const handleViewDetails = (orderId) => {
    const selectedOrder = orders.find((order) => order.id === orderId);
    setSelectedOrderDetails(selectedOrder.productOrders);
    setDetailsVisible(true);
  };

  const handleCloseDetails = () => {
    setDetailsVisible(false);
  };

  const handleEditGoalClick = (goalId) => {
    setEditFormVisible(true);
    setOrderIdToEdit(goalId);
    const selectedOrder = orders.find((goal) => goal.id === goalId);
    setSelectedOrderDetails(selectedOrder.productOrders);
    setSelectedTotalPrice(selectedOrder.totalPrice);
    setOpen(true);
  };

  const handleDeleteGoalClick = (goalId) => {
    setEditFormVisible(true);
    setOrderIdToEdit(goalId);
    const selectedOrder = orders.find((goal) => goal.id === goalId);
    setSelectedOrderDetails(selectedOrder.productOrders);
    setSelectedTotalPrice(selectedOrder.totalPrice);
    setOpen(true);
  };

  const handleRowClick = (params) => {
    setSelectedRow(params.row);
  };



  const rows = orders.map((order) => ({
    id: order.goalId,
    name: order.name,
    startingDate: order.startingDate,
    endingDate: order.endingDate,
    objectType: order.objectType,
    currentGoal: order.currentGoal,
    goalObjective: order.goalObjective,
    goal: order.currentGoal + "/" + order.goalObjective,
    tempGoal: order.currentGoal < order.goalObjective ? order.goalObjective - order.currentGoal : 0,
    goalObject: order.goalObject,
    status: order.status,
    achieved: order.achieved,
  }));

  const columns = [
    {
      field: "name",
      headerName: "Name",
      flex: 2,
      align: "center",
      headerAlign: "center",
      sortable: true,
      minWidth: 150,
    },
    {
      field: "startingDate",
      headerName: "Start Date",
      flex: 1,
      align: "center",
      headerAlign: "center",
      sortable: true,
      minWidth: 150,
      renderCell: (params) =>
        moment(params.row.startingDate).format("YYYY-MM-DD"),
    },
    {
        field: "endingDate",
        headerName: "End Date",
        flex: 1,
        align: "center",
        headerAlign: "center",
        sortable: true,
        minWidth: 150,
        renderCell: (params) =>
          moment(params.row.endingDate).format("YYYY-MM-DD"),
      },
    {
      field: "goal",
      headerName: "Goal",
      flex: 1,
      align: "center",
      headerAlign: "center",
      sortable: true,
      minWidth: 150,
    },
    {
      field: "status",
      headerName: "Status",
      flex: 1,
      align: "center",
      headerAlign: "center",
      sortable: true,
      minWidth: 150,
      renderCell: (params) => {
        const status = params.row.status;
        const statusColors = {
            Upcoming: "#FDFEBC",
            Expired: "#FEBCBC",
        };

        const fontColors = {
          Upcoming: "black",
          Expired: "black",
        };

        const backgroundColor = statusColors[status] || "white";
        const fontColor = fontColors[status] || "black";

        const statusStyle = {
          backgroundColor,
          color: fontColor,
          textAlign: "center",
          padding: "8px",
          //borderRadius: '4px',
          fontSize: "0.9rem",
          textTransform: "none",
          width: "50%",
          height: "50%",
        };

        return (
          <Fab
            variant="extended"
            size="small"
            color="primary"
            style={statusStyle}
            disabled={true}
          >
            {status}
          </Fab>
        );
      },
    },
    {
        field: "achieved",
        headerName: "Achieved",
        flex: 1,
        align: "center",
        headerAlign: "center",
        sortable: true,
        minWidth: 150,
        renderCell: (params) => {
        const isAchieved = params.row.achieved === "Achieved";
        const isExpired = params.row.status === "Expired";
    
        if (isAchieved && isExpired) {
            return (
            <div style={{ textAlign: "center" }}>
                <AiFillCheckCircle style={{ color: 'rgba(159,193,108)',  fontSize: "2rem" }} />
            </div>
            );
        } else if (!isAchieved && isExpired) {
            return (
            <div style={{ textAlign: "center" }}>
                <AiFillCloseCircle style={{ color: 'rgba(212,150,187)', fontSize: "2rem" }} />
            </div>
            );
        }
    
        return null;
        },
    },
    {
      field: "edit",
      headerName: "Edit",
      flex: 1,
      align: "center",
      headerAlign: "center",
      sortable: false,
      minWidth: 100,
      renderCell: (params) => {
        const status = params.row.status;
        const isClosed = status === "Closed";

        return (
          <button
            onClick={() => handleEditGoalClick(params.row.id)}
            disabled={isClosed}
          >
            <EditIcon />
          </button>
        );
      },
    },
    {
        field: "remove",
        headerName: "Remove",
        flex: 1,
        align: "center",
        headerAlign: "center",
        sortable: false,
        minWidth: 100,
        renderCell: (params) => {
          const status = params.row.status;
          const isClosed = status === "Closed";
  
          return (
            <button
              onClick={() => handleDeleteGoalClick(params.row.id)}
              disabled={isClosed}
            >
              <AiFillDelete />
            </button>
          );
        },
      },
  ];

  const filteredGoals = orders.filter(goal => goal.status === "Expired");
  const achievedCount = filteredGoals.filter(goal => goal.achieved === "Achieved").length;
  const notAchievedCount = filteredGoals.filter(goal => goal.achieved === "Not Achieved").length;

  const goalsCount = {
    "Achieved": achievedCount,
    "Not Achieved": notAchievedCount
  };
  const rowInfo = {
    Name: selectedRow.name,
    Current: selectedRow.currentGoal,
    Goal: selectedRow.goalObjective,
    tempGoal: selectedRow.tempGoal,
  };
 

  

  return (
    <Container>
      <Navbar />
    
      <MainContent visible={contentVisible}>

        {currentUser.role === "ADMIN" ||
        currentUser.role === "MANAGER" ? (
          <Button variant="contained" onClick={handleAddOrderClick}>
            Add Goal
          </Button>
        ) : null}

        <div visible={contentVisible}>
            
          <Modal open={isGoalFormVisible}>
            <ModalContent>
              {isGoalFormVisible && (
                <GoalForm onCancel={handleCloseGoalForm} />
              )}
            </ModalContent>
          </Modal>

          <Modal open={isEditFormVisible}>
            <ModalContent>
              {isEditFormVisible && (
                <EditGoalForm
                  onCancel={handleCloseEditGoalForm}
                  orderId={orderIdToEdit}
                  orderDetails={selectedOrderDetails}
                  totalPrice={selectedTotalPrice}
                  onClose={handleCloseEditGoalForm}
                />
              )}
            </ModalContent>
          </Modal>

          {!isGoalFormVisible && !isEditFormVisible && (
            <div>
              {isLoading ? (
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    marginTop: "20%",
                  }}
                >
                  <CircularProgress style={{ color:"#a4d4cc"}}/>
                </Box>
              ) : (
                <OrdersTable sx={{ width: "100%", backgroundColor: "blue" }}>
                  <Box sx={{ height: 400, width: "100%" }}>
                    <Typography
                      variant="h3"
                      component="h3"
                      sx={{ textAlign: "center", mt: 3, mb: 3, color: "white" }}
                    >
                      Goals
                    </Typography>
                    <DataGrid
                      initialState={{
                        pagination: { paginationModel: { pageSize: 5 } },
                        sorting: {
                          sortModel: [{ field: "dateCreated", sort: "desc" }],
                        },
                      }}

                      autoHeight={true}
                      columns={columns}
                      columnVisibilityModel={columnVisible}
                      rows={rows}

                      onRowClick={handleRowClick}

                      pageSizeOptions={[5, 10, 25]}
                      //rowsPerPageOptions={[5, 10, 25]}
                      pagination
                      pageSize={pageSize}
                      onPageSizeChange={(newPageSize) =>
                        setPageSize(newPageSize)
                      }
                      getRowSpacing={(params) => ({
                        top: params.isFirstVisible ? 0 : 5,
                        bottom: params.isLastVisible ? 0 : 5,
                      })}
                      sx={{
                        fontSize: "1rem",
                        border: 2,
                        borderColor: "#a4d4cc",
                        "& .MuiButtonBase-root": {
                          color: "white",
                        },
                        "& .MuiDataGrid-cell:hover": {
                          color: "#a4d4cc",
                        },
                        ".MuiDataGrid-columnSeparator": {
                          display: "none",
                        },
                        color: "white",
                        fontFamily: "Roboto",
                        fontSize: "1.1rem",
                        ".MuiTablePagination-displayedRows": {
                          color: "white",
                          fontSize: "1.2rem",
                        },
                        ".MuiTablePagination-selectLabel": {
                          color: "white",
                          fontSize: "1.2rem",
                        },
                        "& .MuiSelect-select.MuiSelect-select": {
                          color: "white",
                          fontSize: "1.2rem",
                          marginTop: "0.7rem",
                        },
                        ".MuiDataGrid-sortIcon": {
                          opacity: "inherit !important",
                          color: "white",
                        },
                        "& .MuiDataGrid-cell:focus": {
                          outline: "none",
                        },
                        "@media (max-width: 1000px)": {
                          fontSize: "1rem",
                        },
                        "@media (max-width: 760px)": {
                          fontSize: "1rem",
                        },
                        "@media (max-width: 600px)": {
                          fontSize: "1rem",
                        },
                        "@media (max-width: 535px)": {
                          fontSize: "1.2rem",
                        },
                        "@media (max-width: 435px)": {
                          fontSize: "1rem",
                        },
                        "@media (max-width: 335px)": {
                          fontSize: "0.8rem",
                        },
                      }}
                    />

                    <div style={{  display: "flex", flexDirection: "row" }}>
                        <div style={{ color: "white", width: '33.33%', margin:"1rem" }}>
                            <Doughnut  
                            data={Object(goalsCount)} 
                            label={'Expired Goals'} />
                        </div>
                        
                        <div style={{ width: '33.33%', margin:"1rem" }}>
                          {selectedRow && (
                            <div>
                              <p style={{ color: "white" }}>{selectedRow.name}</p>
                              <p style={{ color: "white" }}>{selectedRow.currentGoal}</p>
                              <p style={{ color: "white" }}>{selectedRow.goalObjective}</p>
                              <p style={{ color: "white" }}>{selectedRow.tempGoal}</p>

                              <GoalGauge  
                                data={Object(rowInfo)}/>
                            </div>
                          )}

                        </div>

                        <div style={{ backgroundColor: 'green', width: '33.33%', margin:"1rem" }}>
                            Div3
                        </div>
                    </div>

                  </Box>
                </OrdersTable>
              )}

            </div>
          )}

        </div>


      </MainContent>

      <Snackbar
        open={openSnackbar}
        autoHideDuration={10000}
        onClose={() => setOpenSnackbar(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          variant="filled"
          severity={isOperationSuccessful ? "success" : "error"}
          sx={{ fontSize: "75%" }}
        >
          {alertText}
        </Alert>
      </Snackbar>
      
    </Container>

    
  );
}

export default Goals;