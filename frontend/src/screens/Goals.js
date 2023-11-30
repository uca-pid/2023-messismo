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
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContentText from "@mui/material/DialogContentText";
import Fab from "@mui/material/Fab";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import CircularProgress from "@mui/material/CircularProgress";
import Doughnut from "../components/DoughnutChart";
import GoalGauge from "../components/GoalGauge";
import { Button } from "@mui/material";


const Container = styled.div``;

const Graphs = styled.div`
      display: flex;
      flex-direction: row;
      width: "100%";
      height: "auto";
      justify-content: center;
      margin: 3rem;
    @media (max-width: 1000px) {
        display: flex;
        flex-direction: column;
    }
`;

const DoughnutDiv = styled.div`
    max-width: 50%;
    display: flex;
    margin-right: 5rem;
    justify-content: center;

    @media (max-width: 1000px) {
        max-width: 100%;
        margin-right: 0rem;
        height: auto;
        flex-direction: column;
    }        
`;

const GaugeDiv = styled.div`
    max-width: 50%;
    display: flex;
    justify-content: center;

    @media (max-width: 1000px) {
        max-width: 100%;
        height: auto;
        flex-direction: column;
        margin-top: 2rem;
    }
`;

const AddGoalButton = styled.button`
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
    margin: 1rem;
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

const MOBILE_COLUMNS = {
  id: true,
  name: false,
  startingDate: true,
  endingDate: true,
  objectType: false,
  currentGoal: false,
  goalObjective: false,
  goal: true,
  tempGoal: false,
  goalObject: false,
  status: true,
  achieved: true,

};
const ALL_COLUMNS = {
  id: false,
  name: true,
  startingDate: true,
  endingDate: true,
  objectType: true,
  currentGoal: false,
  goalObjective: false,
  goal: true,
  tempGoal: false,
  goalObject: true,
  status: true,
  achieved: true,
};


function Goals() {
  const { user: currentUser } = useSelector((state) => state.auth);
  const clicked = useSelector((state) => state.navigation.clicked);
  const [isGoalFormVisible, setGoalFormVisible] = useState(false);
  const [isEditGoalFormVisible, setEditGoalFormVisible] = useState(false);
  const [isDetailsVisible, setDetailsVisible] = useState(false);
  const [open, setOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
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
  const [initialRows, setInitialRows] = useState([]);
  const [selectedRow, setSelectedRow] = useState(initialRows);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [editGoalId, setEditGoalId] = useState(null);



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
        const goalsData = response.data;
        setGoals(goalsData);
        setInitialRows(goalsData.length > 0 ? [goalsData[goalsData.length-1]] : []);
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
    setIsLoading(true);
  };

  const handleEditGoalClick = (goalId) => {
    setEditGoalId(goalId);
    setEditGoalFormVisible(true);
    setOpen(true);
  };
  const handleCloseEditGoalForm = () => {
    setEditGoalFormVisible(false);
    setOpenEditForm(false);
    setIsLoading(true);
  };

  const handleViewDetails = (orderId) => {
    const selectedOrder = orders.find((order) => order.id === orderId);
    setSelectedOrderDetails(selectedOrder.productOrders);
    setDetailsVisible(true);
  };

  const handleCloseDetails = () => {
    setDetailsVisible(false);
  };

  const handleRowClick = (params) => {
    setSelectedRow(params.row);
  };

  const handleClose = () => {
    setConfirmOpen(false);
  }

  const handleDeleteClick = (goalId) => {
    setSelectedCategory(goalId);
    setConfirmOpen(true);
  };

  const handleDeleteCategory = async () => {
    const goalData = {
      status: [], 
      achieved: [],
    };
    if (selectedCategory) {
      try {
        await deleteCategoryAsync(selectedCategory);
        setSelectedCategory(null);
        setIsOperationSuccessful(true);
        setAlertText("Goal deleted successfully");
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
        setOpenSnackbar(true);
        setSelectedRow(initialRows);
        
      } catch (error) {
        if (error.response) {
          console.log("Datos de respuesta del error:", error.response.data);
          setAlertText("Failed to delete goal: " + error.response.data);
        }
        setIsOperationSuccessful(false);
        setOpenSnackbar(true);
      }
      setConfirmOpen(false);

    }
  };
  
  const deleteCategoryAsync = async (id) => {
    return goalsService.deleteGoal(id);
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
      field: "id",
      headerName: "ID",
      flex: 2,
      align: "center",
      headerAlign: "center",
      sortable: true,
    },
    {
      field: "name",
      headerName: "Name",
      flex: 2,
      align: "center",
      headerAlign: "center",
      sortable: true,
    },
    {
      field: "objectType",
      headerName: "Type",
      flex: 2,
      align: "center",
      headerAlign: "center",
      sortable: true,
      maxWidth: 100,
    },
    {
      field: "goalObject",
      headerName: "Object",
      flex: 2,
      align: "center",
      headerAlign: "center",
      sortable: true,
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
        maxWidth: 100,
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
      headerName: "",
      flex: 1,
      align: "center",
      headerAlign: "center",
      sortable: false,
      maxWidth: 80,
      renderCell: (params) => {
        const status = params.row.status;
        const isExpired = status === "Expired";

        return (
          <button
            onClick={() => handleEditGoalClick(params.row.id)}
            disabled={isExpired}
          >
            <EditIcon style={{ fontSize: "2rem" }}/>
          </button>
        );
      },
    },

    // Upcoming: edit, delete
    // In Process: edit
    // Expired: delete

    {
        field: "remove",
        headerName: "",
        flex: 1,
        align: "center",
        headerAlign: "center",
        sortable: false,
        maxWidth: 80,
        renderCell: (params) => {
          const status = params.row.status;
          const isInProcess = status === "In Process";
  
          return (
            <button
              onClick={() => handleDeleteClick(params.row.id)}
              disabled={isInProcess}
            >
              <AiFillDelete style={{ fontSize: "2rem" }}/>
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

        {(currentUser.role === "ADMIN" ||
        currentUser.role === "MANAGER") &&
        !isGoalFormVisible && !isEditGoalFormVisible ? (
          <AddGoalButton variant="contained" onClick={handleAddOrderClick}>
            Add Goal
          </AddGoalButton>
        ) : null}

        <div visible={contentVisible}>
            
          <Modal open={isGoalFormVisible}>
            <ModalContent>
              {isGoalFormVisible && (
                <GoalForm onCancel={handleCloseGoalForm} />
              )}
            </ModalContent>
          </Modal>

          <Modal open={isEditGoalFormVisible}>
            <ModalContent>
              {isEditGoalFormVisible && (
                <EditGoalForm onCancel={handleCloseEditGoalForm} goalId={editGoalId}/>
              )}
            </ModalContent>
          </Modal>

          {!isGoalFormVisible && !isEditGoalFormVisible && !isEditFormVisible && (
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

                  <Graphs id="wrap">

                      <DoughnutDiv>
                          <Doughnut  
                          data={Object(goalsCount)} 
                          label={'Expired Goals'} />
                      </DoughnutDiv>
                      
                      <GaugeDiv>
                        {selectedRow && (
                          <div>
                            <GoalGauge  
                              data={Object(rowInfo)}/>
                          </div>
                        )}

                      </GaugeDiv>

                  </Graphs>

                  </Box>
                </OrdersTable>
              )}

            </div>
          )}

        </div>


      </MainContent>

      <Dialog
          open={confirmOpen}
          onClose={handleClose}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
          PaperProps={{
            style: {
              backgroundColor: "white",
              boxShadow: "none",
              zIndex: 1000,
              fontSize: "24px",
            },
          }}
        >
          <DialogTitle id="alert-dialog-title" style={{ fontSize: "1.3rem" }}>
            {selectedCategory &&
              `Are you sure you want to delete this goal?`}
          </DialogTitle>
          <DialogContent>
            <DialogContentText
              id="alert-dialog-description"
              style={{ fontSize: "1rem" }}
            >
              The goal will be permanently deleted
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose} style={{ fontSize: "1rem" }}>
              Cancel
            </Button>
            <Button
              onClick={handleDeleteCategory}
              style={{ color: "red", fontSize: "1rem" }}
              autoFocus
            >
              Delete
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar
          open={openSnackbar}
          autoHideDuration={10000} 
          onClose={() => setOpenSnackbar(false)}
          anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        >
          <Alert onClose={() => setOpenSnackbar(false)} severity={isOperationSuccessful ? "success" : "error"} variant="filled" sx={{fontSize: '80%'}}>
            {alertText}
          </Alert>
        </Snackbar>
      
    </Container>

    
  );
}

export default Goals;