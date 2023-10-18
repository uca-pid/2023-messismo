import * as React from "react";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import SendIcon from "@mui/icons-material/Send";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import Box from "@mui/material/Box";
import { useEffect, useState } from "react";
import categoryService from "../services/category.service";
import { Button } from "@mui/material";
import TextField from "@mui/material/TextField";
import "./Filter.css";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import Fab from "@mui/material/Fab";
import ClearIcon from "@mui/icons-material/Clear";
import IconButton from "@mui/material/IconButton";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import {
  setMinValue,
  setMaxValue,
  setMaxStock,
  setMinStock,
  setSelectedCategories,
} from "../redux/filtersSlice";
import PriceValidation from "../PriceValidation";
import StockValidation from "../StockValidation";

const FilterRedux = (props) => {
  const [openCategories, setOpenCategories] = React.useState(false);
  const [openMinPrice, setOpenMinPrice] = React.useState(false);
  const [openMaxPrice, setOpenMaxPrice] = React.useState(false);
  const [openMinStock, setOpenMinStock] = React.useState(false);
  const [openMaxStock, setOpenMaxStock] = React.useState(false);
  const [categories, setCategories] = useState([]);
  const [minValueTemp, setMinValueTemp] = useState("");
  const [maxValueTemp, setMaxValueTemp] = useState("");
  const [minStockTemp, setMinStockTemp] = useState("");
  const [maxStockTemp, setMaxStockTemp] = useState("");
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const selectedCategories = useSelector(
    (state) => state.filters.selectedCategories
  );
  const selectedCategory = useSelector(
    (state) => state.filters.selectedCategories
  );
  const minValue = useSelector((state) => state.filters.minValue);
  const maxValue = useSelector((state) => state.filters.maxValue);
  const minStock = useSelector((state) => state.filters.minStock);
  const maxStock = useSelector((state) => state.filters.maxStock);
  const dispatch = useDispatch();
  const [errors, setErrors] = useState({});
  const [appliedFilters, setAppliedFilters] = useState({
    selectedCategory: false,
    minValue: false,
    maxValue: false,
    minStock: false,
    maxStock: false,
  });

  const handleOpenCategories = () => {
    setOpenCategories(!openCategories);
  };
  const handleOpenMinPrice = () => {
    setOpenMinPrice(!openMinPrice);
  };
  const handleOpenMaxPrice = () => {
    setOpenMaxPrice(!openMaxPrice);
  };
  const handleOpenMinStock = () => {
    setOpenMinStock(!openMinStock);
  };
  const handleOpenMaxStock = () => {
    setOpenMaxStock(!openMaxStock);
  };

  /*const handleSelectedCategory = (value) => {
    dispatch(setSelectedCategory(value));

  };*/



  const handlePriceRange = (value) => {
    const validationErrors = PriceValidation({
      minPrice: minValueTemp,
      maxPrice: maxValueTemp,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
      setErrors({});
      dispatch(setMinValue(minValueTemp));
      dispatch(setMaxValue(maxValueTemp));
    }
  };

  const setPriceRange = async () => {
    setMinValue(minValueTemp);
    setMaxValue(maxValueTemp);
  };

  const old = (value) => {
    dispatch(setMinStock(minStockTemp));
    dispatch(setMaxStock(maxStockTemp));
  };

  const handleStockRange = (value) => {
    const validationErrors = StockValidation({
      minStock: minStockTemp,
      maxStock: maxStockTemp,
    });

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.log(validationErrors);
    } else {
      setAppliedFilters({ ...appliedFilters, minStock: true, maxStock: true });

      setErrors({});
      dispatch(setMinStock(minStockTemp));
      dispatch(setMaxStock(maxStockTemp));
    }
  };

  const filterProduct = () => {
    const product = {
      productName: "",
      categories: selectedCategories,
      minUnitPrice: minValue === "" ? null : minValue,
      maxUnitPrice: maxValue === "" ? null : maxValue,
      minStock: minStock === "" ? null : minStock,
      maxStock: maxStock === "" ? null : maxStock,
    };

    console.log(product);

    props.onSave(product);
    props.onClose();
  };

  useEffect(() => {
    categoryService
      .getAllCategories()
      .then((response) => {
        setCategories(response.data);
      })
      .catch((error) => {
        console.error("Error al obtener categorías:", error);
      });
  }, []);

  const selectedCategoryBadge = selectedCategories.map((category) => (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      backgroundColor="whitesmoke"
      width={"100%"}
      padding="5px"
      borderRadius="5px"
    >
      <div
        className="badge"
        style={{ display: "flex", flexDirection: "row" }}
      >{`${category}`}</div>
      <IconButton
        aria-label="edit"
        size="large"
        color="red"
        onClick={() => handleRemoveCategory(category)}
      >
        <ClearIcon style={{ justifyContent: "flex-end" }} />
      </IconButton>
    </Box>
  ));

  const handleRemoveCategory = (category) => {
    const updatedCategories = selectedCategories.filter(
      (selectedCategory) => selectedCategory !== category
    );
    dispatch(setSelectedCategories(updatedCategories));
  };

  const priceRangeBadge = () => {
    if (minValue !== "" && maxValue !== "") {
      return <div className="badge">{`$${minValue} - $${maxValue}`}</div>;
    } else if (minValue !== "") {
      return <div className="badge">{`More than $${minValue}`}</div>;
    } else if (maxValue !== "") {
      return <div className="badge">{`Less than $${maxValue}`}</div>;
    }
  };

  const stockRangeBadge = () => {
    if (minStock !== "" && maxStock !== "") {
      return <div className="badge">{`Stock: ${minStock} - ${maxStock}`}</div>;
    } else if (minStock !== "") {
      return <div className="badge">{`More than ${minStock}`}</div>;
    } else if (maxStock !== "") {
      return <div className="badge">{`Less than ${maxStock}`}</div>;
    }
  };

  const resetPriceValues = async () => {
    dispatch(setMinValue(""));
    setMinValueTemp("");
    setMaxValueTemp("");
    dispatch(setMaxValue(""));
    dispatch(setMinValue(""));
  };

  const handleClose = () => {
    props.onClose();
  };

  const resetStockValues = async () => {
    dispatch(setMinStock(""));
    setMinStockTemp("");
    setMaxStockTemp("");
    dispatch(setMaxStock(""));
  };

  const handleClearAll = () => {
    dispatch(setSelectedCategories([]));
    dispatch(setMinValue(""));
    dispatch(setMaxValue(""));
    dispatch(setMinStock(""));
    dispatch(setMaxStock(""));
    setMinStockTemp("");
    setMaxStockTemp("");
    setMinValueTemp("");
    setMaxValueTemp("");
  };

  const hasFiltersApplied =
    selectedCategory !== "" ||
    minValue !== "" ||
    maxValue !== "" ||
    minStock !== "" ||
    maxStock !== "";

  const resetCategoryValue = () => {
    dispatch(setSelectedCategories([]));
  };

  const handleSelectedCategories = (value) => {
    if (selectedCategories.includes(value)) {
      const updatedCategories = selectedCategories.filter(
        (category) => category !== value
      );
      dispatch(setSelectedCategories(updatedCategories));
    } else {
      const updatedCategories = [...selectedCategories, value];
      dispatch(setSelectedCategories(updatedCategories));
    }
    
  };

  return (
    <div>
      <h1 style={{ marginBottom: "5%", fontSize: "2 rem" }}>Filter Products</h1>
      <div className="appliedFilters">
        <div className="filterBoxes">
          {selectedCategories.length > 0 &&
            selectedCategories.map((category) => (
              <Fab
                key={category} // Añade una clave única para cada categoría
                variant="extended"
                size="medium"
                color="white"
                style={{ marginTop: "2%" }}
                onClick={() => handleRemoveCategory(category)}
              >
                <ClearIcon sx={{ mr: 1 }} />
                {category}
              </Fab>
            ))}
          {(maxValue != "" || minValue != "") && (
            <Fab
              style={{ marginTop: "2%" }}
              variant="extended"
              size="medium"
              color="white"
              onClick={resetPriceValues}
            >
              <ClearIcon sx={{ mr: 1 }} />
              {priceRangeBadge()}
            </Fab>
          )}
          {(minStock != "" || maxStock != "") && (
            <Fab
              style={{ marginTop: "2%" }}
              variant="extended"
              size="medium"
              color="white"
              onClick={resetStockValues}
            >
              <ClearIcon sx={{ mr: 1 }} />
              {stockRangeBadge()}
            </Fab>
          )}
        </div>
        <div>
          {hasFiltersApplied && (
            <Button
              variant="outlined"
              style={{
                color: "black",
                backgroundColor: "white",
                borderColor: "grey",
                width: "40% !important",
                fontSize: "1.1rem",
              }}
              className="clearAllButton"
              onClick={handleClearAll}
            >
              Clear All
            </Button>
          )}
        </div>
      </div>
      <List
        sx={{
          width: "100%",
          maxWidth: 360,
          bgcolor: "background.paper",
          fontSize: "1.8 rem",
        }}
        component="nav"
        aria-labelledby="nested-list-subheader"
      >
        <Box fontSize="2rem">
          <ListItemButton onClick={handleOpenCategories}>
            <ListItemText primary="Categories" style={{ fontSize: "24px" }} />
            {openCategories ? <ExpandLess /> : <ExpandMore />}
          </ListItemButton>
          <Collapse in={openCategories} timeout="auto" unmountOnExit>
            {categories.map((category, index) => (
              <ListItemButton
                key={index}
                sx={{
                  pl: 4,
                  backgroundColor: selectedCategories.includes(category.name)
                    ? "lightgray"
                    : "inherit",
                }}
                onClick={() => handleSelectedCategories(category.name)}
              >
                <ListItemText primary={category.name} />
              </ListItemButton>
            ))}
          </Collapse>
        </Box>
        <ListItemButton onClick={handleOpenMinPrice}>
          <ListItemText primary="Price" sx={{ fontSize: "1.8 rem" }} />
          {openMinPrice ? <ExpandLess /> : <ExpandMore />}
        </ListItemButton>
        <Collapse in={openMinPrice} timeout="auto" unmountOnExit>
          <div className="range">
            <TextField
              id="outlined-required"
              label="Min"
              value={minValueTemp}
              error={errors.minPrice ? true : false}
              helperText={errors.minPrice || ""}
              onChange={(e) => setMinValueTemp(e.target.value)}
            />
            <span className="slash">-</span>
            <TextField
              id="outlined-required"
              label="Max"
              value={maxValueTemp}
              error={errors.maxPrice ? true : false}
              helperText={errors.maxPrice || ""}
              onChange={(e) => setMaxValueTemp(e.target.value)}
            />
            <Box sx={{ "& > :not(style)": { m: 1 } }}>
              <Fab
                color="primary"
                size="small"
                aria-label="add"
                onClick={handlePriceRange}
              >
                <ArrowForwardIosIcon />
              </Fab>
            </Box>
          </div>
        </Collapse>
        <ListItemButton onClick={handleOpenMaxPrice}>
          <ListItemText primary="Stock" sx={{ fontSize: "1.8 rem" }} />
          {openMaxPrice ? <ExpandLess /> : <ExpandMore />}
        </ListItemButton>
        <Collapse in={openMaxPrice} timeout="auto" unmountOnExit>
          <div className="range">
            <TextField
              id="outlined-required"
              label="Min"
              value={minStockTemp}
              error={errors.minStock ? true : false}
              helperText={errors.minStock || ""}
              onChange={(e) => setMinStockTemp(e.target.value)}
            />
            <span className="slash">-</span>
            <TextField
              id="outlined-required"
              label="Max"
              value={maxStockTemp}
              error={errors.maxStock ? true : false}
              helperText={errors.maxStock || ""}
              onChange={(e) => setMaxStockTemp(e.target.value)}
            />
            <Box sx={{ "& > :not(style)": { m: 1 } }}>
              <Fab
                color="primary"
                size="small"
                aria-label="add"
                onClick={handleStockRange}
              >
                <ArrowForwardIosIcon />
              </Fab>
            </Box>
          </div>
        </Collapse>
      </List>
      <div className="buttons-add">
        <Button
          variant="outlined"
          style={{
            color: "grey",
            borderColor: "grey",
            width: "40%",
            fontSize: "1rem",
          }}
          onClick={handleClose}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          style={{
            backgroundColor: "green",
            color: "white",
            borderColor: "green",
            width: "40%",
            fontSize: "1rem",
          }}
          onClick={filterProduct}
        >
          Apply
        </Button>
      </div>
    </div>
  );
};

export default FilterRedux;
