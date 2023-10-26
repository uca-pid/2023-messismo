import React, { useEffect, useState } from "react";
import { Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { useActionData } from "react-router-dom";
import categoryService from "../services/category.service";
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import Tooltip from "@mui/material/Tooltip";
import Dialog from "@mui/material/Dialog";
import { useSelector } from "react-redux/es/hooks/useSelector";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContentText from "@mui/material/DialogContentText";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import CategoryValidation from "../CategoryValidation";
import './CategoriesList.css'
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';



const CategoriesList = () => {
  const [categories, setCategories] = useState([]);
  const { user: currentUser } = useSelector((state) => state.auth);
  const role = currentUser.role;
  const [openForm, setOpenForm] = useState(false);
  const [categoryName, setCategoryName] = useState("");
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [alertText, setAlertText] = useState("");
  const [isOperationSuccessful, setIsOperationSuccessful] = useState(false);
  const [open, setOpen] = useState(false);
  const [errors, setErrors] = useState({});
  const [sortField, setSortField] = useState(null);
  const [sortOrder, setSortOrder] = useState("asc");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    categoryService
      .getAllCategories()
      .then((response) => {
        setCategories(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("Error al obtener categorías:", error);
        setIsLoading(false);
      });
  }, []);

  const handleOpenForm = () => {
    setOpenForm(true);
  }

  const handleCloseForm = () => {
    setOpenForm(false);
    setErrors({});
  }

  const handleAddCategory = async () => {
    const validationErrors = CategoryValidation({
      name: categoryName,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      
    try {
      const response = await categoryService.addCategory(categoryName);

        const updatedCategoriesResponse = await categoryService.getAllCategories();
        const updatedCategories = updatedCategoriesResponse.data;
  
        setCategories(updatedCategories);
        setIsOperationSuccessful(true);
        setOpenSnackbar(true);
        setAlertText("Category added successfully");
        setCategoryName(""); 
        handleCloseForm();
      
    } catch (error) {
      console.error("Error al agregar categoría:", error);
      setIsOperationSuccessful(false);
      setOpenSnackbar(true);
      if (error.response) {
      setAlertText("Failed to create category: " + error.response.data);
      }
    }
  }
  };

  const handleNameChange = (event) => {
    setCategoryName(event.target.value);
  }

  const handleDeleteClick = (category) => {
    console.log(category);
    setSelectedCategory(category);
    setOpen(true);
  };
  const handleDeleteCategory = async (category) => {
    if (selectedCategory) {
      try {
        console.log(selectedCategory.name);
        await deleteCategoryAsync(selectedCategory.name);
        setSelectedCategory(null);
        setIsOperationSuccessful(true);
        setAlertText("Category deleted successfully");
        categoryService
      .getAllCategories()
      .then((response) => {
        console.log(response.data);
        setCategories(response.data);
      })
      .catch((error) => {
        console.error("Error al obtener categorías:", error);
      });
        setOpenSnackbar(true);
        
      } catch (error) {
        if (error.response) {
          console.log("Datos de respuesta del error:", error.response.data);
          setAlertText("Failed to delete category: " + error.response.data);
        }
        setIsOperationSuccessful(false);
        setOpenSnackbar(true);
      }
      setOpen(false);

    }
    }
  

  const deleteCategoryAsync = async (categoryName) => {
    console.log(categoryName);
    return categoryService.deleteCategory(categoryName);
  }

  const handleClose = () => {
    setOpen(false);
  }

  const handleSort = (field) => {
  
    if (field === sortField) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      
      setSortField(field);
      setSortOrder("asc");
    }


  const sortedProducts = [...categories].sort((a, b) => {
    if (sortOrder === "asc") {
      if (a[sortField] < b[sortField]) {
        return -1;
      }
      if (a[sortField] > b[sortField]) {
        return 1;
      }
      return 0;
    } else {
      if (a[sortField] > b[sortField]) {
        return -1;
      }
      if (a[sortField] < b[sortField]) {
        return 1;
      }
      return 0;
    }
  });

  console.log(sortedProducts);
  setCategories(sortedProducts);


  };
 
  return (
    <div style={{width: "100%", display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center"}}>
      <div className="categoryTitle">
      <h1 style={{ marginBottom: "3%", fontSize: "2.2rem", marginTop: "1%", color: "white"}}>Categories</h1>
      </div>
      <div style={{flex:1, display: "flex", width: "90%" , justifyContent: "flex-start"}}>
      {role === "ADMIN" || role === "MANAGER" ? (
      <Button
        variant="contained"
        endIcon={<AddIcon />}
        style={{
          color: "black",
          backgroundColor: '#a4d4cc',
          borderColor: "#007bff",
          fontSize: "1rem",
          height: "40px",
          marginBottom: "3%",
        }}
        onClick={handleOpenForm}
      >
        Add Category
      </Button>
      ) : (
        <div></div>
      ) }
      </div>
      <Dialog
        open={openForm}
        dividers={true}
        onClose={handleCloseForm}
        aria-labelledby="form-dialog-title"
        className="custom-dialog"
        maxWidth="sm"
        fullWidth
      >
        <DialogContent>
        <h1 style={{ marginBottom: "5%", fontSize: "1.6 rem" }}>New Category</h1>
        <p>Name *</p>
      <TextField
        required
        id="name"
        value={categoryName}
        onChange={handleNameChange}
        variant="outlined"
        error={errors.name ? true : false}
        helperText={errors.name || ''}
        style={{ width: '80%', marginTop: '3%', marginBottom: '3%', fontSize: '1.3rem'}}
        InputProps={{
          style: {
            fontSize: '1.1rem', 
          },}}
          FormHelperTextProps={{
            style: {
              fontSize: '1.1rem', 
            },
          }}
      />
       <div className="buttons-add">
        <Button
          variant="outlined"
          style={{ color: "grey", borderColor: "grey" , width: "40%", fontSize: '1rem'}}
          onClick={handleCloseForm}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          style={{
            backgroundColor: '#a4d4cc',
            color: "black",
            borderColor: "green",
            
            width: "40%",
            fontSize: '1rem',
          }}
          onClick={handleAddCategory}
        >
          Add
        </Button>
      </div>
        </DialogContent>
      </Dialog>
      <div className="title">
          <p style={{ color : "white", fontWeight: "bold"}}>Name</p>
          <IconButton size="small" onClick={() => handleSort("name")}>
      {sortField === "name" ? (
        sortOrder === "asc" ? (
          <ExpandLessIcon style={{ color: "white"}}/>
        ) : (
          <ExpandMoreIcon style={{ color: "white"}}/>
        )
      ) : (
        <ExpandMoreIcon style={{ color: "white"}}/>
      )}
    </IconButton>
        </div>

        {isLoading ? (
  <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '10%' }}>
    <CircularProgress style={{ color:"#a4d4cc"}} />
  </Box>
) : (
      <>
      {categories.map((category) => (
        <div key={category.id} className="category-data" style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginBottom: "1rem", padding: "1rem", backgroundColor: "#f5f5f5", borderRadius: "5px", width: "20%" }}>
          <p className="text">
            {category.name}
          </p>
          <div className="buttons-edit">
            {role === "ADMIN" || role === "MANAGER" ? (
              <Tooltip
                title="Delete Product"
                arrow
                style={{ fontSize: "1.5rem" }}
              >
                <IconButton
                  aria-label="delete"
                  size="large"
                  style={{ color: "red", fontSize: "1 rem" }}
                  onClick={() => handleDeleteClick(category)}
                >
                  <DeleteIcon style={{ fontSize: "1.7rem" }} />
                </IconButton>
              </Tooltip>
            ) : (
              console.log("")
            )}
          </div>
        </div>
      ))}
      </>
      )}
      <Dialog
          open={open}
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
              `Are you sure you want to delete the category ${selectedCategory.name}?`}
          </DialogTitle>
          <DialogContent>
            <DialogContentText
              id="alert-dialog-description"
              style={{ fontSize: "1rem" }}
            >
              The category will be permanently deleted
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
    </div>
  );
};

export default CategoriesList;
