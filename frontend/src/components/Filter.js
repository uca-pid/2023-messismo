import * as React from "react";
import ListSubheader from "@mui/material/ListSubheader";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import InboxIcon from "@mui/icons-material/MoveToInbox";
import DraftsIcon from "@mui/icons-material/Drafts";
import SendIcon from "@mui/icons-material/Send";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import StarBorder from "@mui/icons-material/StarBorder";
import Box from "@mui/material/Box";
import Slider from "@mui/material/Slider";
import { useEffect, useState } from "react";
import categoryService from "../services/category.service";
import { Button } from "@mui/material";
import TextField from "@mui/material/TextField";
import "./Filter.css";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import Fab from "@mui/material/Fab";
import ClearIcon from "@mui/icons-material/Clear";
import IconButton from "@mui/material/IconButton";

const Filter = (props) => {
  const [openCategories, setOpenCategories] = React.useState(false);
  const [openMinPrice, setOpenMinPrice] = React.useState(false);
  const [openMaxPrice, setOpenMaxPrice] = React.useState(false);
  const [openMinStock, setOpenMinStock] = React.useState(false);
  const [openMaxStock, setOpenMaxStock] = React.useState(false);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedMinPrice, setSelectedMinPrice] = useState("");
  const [selectedMaxPrice, setSelectedMaxPrice] = useState("");
  const [selectedMinStock, setSelectedMinStock] = useState("");
  const [selectedMaxStock, setSelectedMaxStock] = useState("");
  const [minValue, setMinValue] = useState("");
  const [maxValue, setMaxValue] = useState("");
  const [minValueTemp, setMinValueTemp] = useState("");
  const [maxValueTemp, setMaxValueTemp] = useState("");
  const [minStock, setMinStock] = useState("");
  const [maxStock, setMaxStock] = useState("");
  const [minStockTemp, setMinStockTemp] = useState("");
  const [maxStockTemp, setMaxStockTemp] = useState("");
  const [loadingCategory, setLoadingCategory] = useState(true);
  const [loadingPrice, setLoadingPrice] = useState(true);
  const [loadingStock, setLoadingStock] = useState(true);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [appliedFilters, setAppliedFilters] = useState({});

  const handleChange = () => {};

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

  const handleSelectedCategory = async (value) => {
    setLoadingCategory(true);
    const response = await setSelectedCategoryAsync(value);
    setLoadingCategory(false);
  };

  const setSelectedCategoryAsync = async (value) => {
    setSelectedCategory(value);
  };

  const handleSelectedMinPrice = (value) => {
    setSelectedCategory(value);
  };

  const handleSelectedMaxPrice = (value) => {
    setSelectedCategory(value);
    console.log(selectedCategory);
  };

  const handleSelectedMinStock = (value) => {
    setSelectedCategory(value);
  };

  const handleSelectedMaxStock = (value) => {
    setSelectedCategory(value);
  };

  const valueLabelFormat = (value) => {
    return value.toLocaleString();
  };

  const handlePriceRange = async () => {
    setLoadingPrice(true);
    if (minValueTemp != "" || maxValueTemp != "") {
      const response = await setPriceRange();
      setLoadingPrice(false);
    }
  };

  const setPriceRange = async () => {
    setMinValue(minValueTemp);
    setMaxValue(maxValueTemp);
  };

  const handleStockRange =  async () => {
    setLoadingStock(true);
    if (minStockTemp != "" || maxStockTemp != "") {
      const response = await setStockRange();
      setLoadingStock(false);
    }
  };

  const setStockRange = async () => {
    setMinStock(minStockTemp);
    setMaxStock(maxStockTemp);
  }

  const filterProduct = () => {
    const product = {
      productName: "",
      categoryName: selectedCategory,
      minUnitPrice: minValue === "" ? null : minValue,
      maxUnitPrice: maxValue === "" ? null : maxValue,
      minStock: minStock === "" ? null : minStock,
      maxStock: maxStock === "" ? null : maxStock,
    };

    console.log(product);

    props.onSave(product);
    props.onClose();
  };

  useEffect( () => {
    
    categoryService
      .getAllCategories()
      .then((response) => {
        setCategories(response.data);
      })
      .catch((error) => {
        console.error("Error al obtener categorías:", error);
      });
  }, []);

  useEffect(() => {
    if (props.appliedFilters) {
      console.log("hola")
      console.log(props.appliedFilters);
      handleSelectedCategory(props.appliedFilters.category);
      setMinValueTemp(props.appliedFilters.minUnitPrice);
      setMaxValueTemp(props.appliedFilters.maxUnitPrice);
      setMinStockTemp(props.appliedFilters.minStock);
      setMaxStockTemp(props.appliedFilters.maxStock);
      handlePriceRange();
      handleStockRange();}
  }, [props.appliedFilters]);

  const handleFilters = async () => {
    const response = await applyFilters();
  }

  const applyFilters = async () => {
    if (props.appliedFilters) {
      console.log("hola")
      console.log(props.appliedFilters);
      handleSelectedCategory(props.appliedFilters.category);
      setMinValueTemp(props.appliedFilters.minUnitPrice);
      setMaxValueTemp(props.appliedFilters.maxUnitPrice);
      setMinStockTemp(props.appliedFilters.minStock);
      setMaxStockTemp(props.appliedFilters.maxStock);
      handlePriceRange();
      handleStockRange();
    }
  }
  const selectedCategoryBadge = selectedCategory && (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      backgroundColor="whitesmoke"
      width={"20%"}
      borderRadius="10px"
    >
      <div
        className="badge"
        style={{ display: "flex", flexDirection: "row" }}
      >{`${selectedCategory}`}</div>
      <IconButton
        aria-label="edit"
        size="large"
        color="red"
        onClick={() => setSelectedCategory("")}
      >
        <ClearIcon style={{ justifyContent: "flex-end" }} />
      </IconButton>
    </Box>
  );

  const priceRangeBadge = () => {
    if (minValue !== "" && maxValue !== "") {
      return <div className="badge">{`$${minValue} - $${maxValue}`}</div>;
    } else if (minValue !== "") {
      return <div className="badge">{`More than ${minValue}`}</div>;
    } else if (maxValue !== "") {
      return <div className="badge">{`Less than ${maxValue}`}</div>;
    }
  };

  const stockRangeBadge = () => {
    if (minStock !== "" && maxStock !== "") {
      return (
        <div className="badge">{`Stock Range: ${minStock} - ${maxStock}`}</div>
      );
    } else if (minStock !== "") {
      return <div className="badge">{`More than ${minStock}`}</div>;
    } else if (maxStock !== "") {
      return <div className="badge">{`Less than ${maxStock}`}</div>;
    }
  };

  const resetPriceValues = async () => {
    setMinValue("");
    setMinValueTemp("");
    setMaxValueTemp("");
    setMaxValue("");
    setLoadingPrice(true);
    console.log(loadingPrice);
  };

  const handleClose = () => {
    props.onClose();
  };

  const resetStockValues = async () => {
    setMinStock("");
    setMinStockTemp("");
    setMaxStockTemp("");
    setMaxStock("");
    setLoadingStock(true);
    
  };

  useEffect(() => {
    setLoadingCategory(false);
  }, [selectedCategory]);

  return (
    <div>
      <h1 style={{ marginBottom: "5%", fontSize: "2 rem" }}>Filter Products</h1>
      {!loadingCategory && (
        <div className="badges" style={{ display: "flex", width: "100%" }}>{selectedCategoryBadge}</div>
      )}
      {!loadingPrice && (
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          backgroundColor="whitesmoke"
          width={"25%"}
          borderRadius="10px"
        >
          <div>
            <div className="badges">{priceRangeBadge()}</div>
          </div>
          <IconButton
            aria-label="edit"
            size="large"
            color="red"
            onClick={() => {
              resetPriceValues();
            }}
          >
            <ClearIcon style={{ justifyContent: "flex-end" }} />
          </IconButton>
        </Box>
      )}
      {!loadingStock && (
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          backgroundColor="whitesmoke"
          width={"25%"}
          borderRadius="10px"
        >
          <div>
            <div className="badges">{stockRangeBadge()}</div>
          </div>
          <IconButton
            aria-label="edit"
            size="large"
            color="red"
            onClick={() => {
              resetStockValues();
            }}
          >
            <ClearIcon style={{ justifyContent: "flex-end" }} />
          </IconButton>
        </Box>
      )}
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
                  backgroundColor:
                    selectedCategory === category.name
                      ? "lightgray"
                      : "inherit",
                }}
                onClick={() => handleSelectedCategory(category.name)}
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
              onChange={(e) => setMinValueTemp(e.target.value)}
            />
            <span className="slash">-</span>
            <TextField
              id="outlined-required"
              label="Max"
              value={maxValueTemp}
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
              onChange={(e) => setMinStockTemp(e.target.value)}
            />
            <span className="slash">-</span>
            <TextField
              id="outlined-required"
              label="Max"
              value={maxStockTemp}
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
            fontSize: "1.3rem",
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
            fontSize: "1.3rem",
          }}
          onClick={filterProduct}
        >
          Apply
        </Button>
      </div>
    </div>
  );
};

export default Filter;
