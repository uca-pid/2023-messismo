import React, { useState, useEffect } from "react";
import { useForm, Controller } from 'react-hook-form'
import styled from "styled-components";
import { GrAddCircle } from 'react-icons/gr'
import { RiDeleteBinLine } from 'react-icons/ri'
import productsService from "../services/products.service";
import categoryService from "../services/category.service";
import goalsService from "../services/goals.service";
import { useSelector } from 'react-redux';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateRangePicker } from '@mui/x-date-pickers-pro/DateRangePicker';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { formatISO } from 'date-fns';
import dayjs from 'dayjs';



const CustomizedDateTimePicker = styled(DateRangePicker)`
   
    .MuiInputBase-input {
        color: #a4d4cc; 
    }
    .MuiInputLabel-root {
        color: black; 
    }
    .MuiOutlinedInput-notchedOutline {
        border-color: #a4d4cc;
    }
    .MuiSvgIcon-root {
        fill: #a4d4cc;
    }
    .MuiOutlinedInput-root {
        &:hover fieldset {
            border-color: #a4d4cc;
        }
        &.Mui-focused fieldset {
            border-color: #a4d4cc;
        }
    }
    :focus-within .MuiInputLabel-root {
        color: #a4d4cc;
    }

`;

const Form = styled.form`
    padding: 2rem;
    background-color: rgb(164, 212, 204, 0.6);

    .fail {
        color: red;
    }

    .form-totalprice{
        margin-top: 1.5rem;
        text-align: center;
        span{
            font-size: 24px;
        }
    }

    small{
        font-size: 10px;

        @media (max-width: 550px) {
            font-size: 9px;
        }

        @media (max-width: 450px) {
            font-size: 8px;
        }

        @media (max-width: 350px) {
            font-size: 7px;
        }
    }

    @media (max-width: 250px) {
        min-width: 250px;
    }

`;

const ProductContainer = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    margin-bottom: 2rem;

    .form-name{

    }

    .form-dates{
        margin-top: 1rem;
    }

    .form-types{
        display: flex;
        flex-direction: row;
    }

    .form-type{
        margin-right: 1rem;
        margin-top: 1rem;
        width: 50%;
    }

    .form-product{
        margin-top: 1rem;
        width: 50%;
    }

    .form-category{
        margin-top: 1rem;
        width: 50%;
    }

    .form-amount{
        margin-right: 1rem;
        margin-top: 1rem;
        width: 100%;
    }

`;

const Label = styled.label`
    display: inline-block;
    margin-bottom: 7px;
    font-size: 1.3rem;
    text-transform: uppercase;
    color: black;
`;

const Select = styled.select`
    border: 1px solid rgb(164, 212, 204, 0.7);
    background-color: transparent;
    display: block;
    font-family: inherit;
    font-size: 16px;
    padding: 1rem;
    width: 100%;   

    &:focus{
        outline: none;
        border-color: #A4D4CC;
    }

    @media(max-width: 350px){
      font-size: 12px;
    }
`;

const Input = styled.input`
    border: 1px solid rgb(164, 212, 204, 0.7);
    background-color: transparent;
    display: block;
    font-family: inherit;
    font-size: 16px;
    padding: 1.1rem;
    width: 100%; 

    &:focus{
        outline: none;
        border-color: #A4D4CC;
    }

    @media(max-width: 350px){
      font-size: 12px;
    }
`;

const AddIcon = styled(GrAddCircle)`
    cursor: pointer;
    font-size: 20px;
    width: 100%;
`;

// const RemoveIcon = styled(RiDeleteBinLine)`
//     color: red;
//     font-size: 20px;
// `;

const Button = styled.button`
    display: block;
    width: 100%;
    font-size: 1.5rem;
    border-radius: 3px;
    padding: 1rem 3.5rem;
    margin-top: 2rem;
    border: 1px solid black;
    background-color: #a4d4cc;
    color: black;
    text-transform: uppercase;
    cursor: pointer;
    letter-spacing: 1px;
    box-shadow: 0 3px #999;
    font-family: 'Roboto',serif;
    text-align: center;

    &:hover{
        background-color: #a7d0cd;
    }
    &:active{
        background-color: #a4d4cc;
        box-shadow: 0 3px #666;
        transform: translateY(4px);
    }
    &:focus{
        outline: none;
    }



`;

const Buttons = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .placeorder{
        margin-right: 1rem;
    }

    @media(max-width: 477px){
      width: 100%;
      flex-direction: column;
      text-align: center;
    }
`;

const GoalForm = ({onCancel}) => {

    const { control, register, handleSubmit, formState: {errors}, watch } = useForm()
    const { user: currentUser } = useSelector((state) => state.auth);
    const [selectedProducts, setSelectedProducts] = useState({});
    const [selectedCategories, setSelectedCategories] = useState({});
    const [productStocks, setProductStocks] = useState({});
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedType, setSelectedType] = useState('');
    const [formField, setFormField] = useState([
        { product: '', amount: '' }
    ]);
    const [options, setOptions] = useState({
        products: [],
      });
    const [catOptions, setCatOptions] = useState({
        categories: [],
      });
    const [selectedProductNames, setSelectedProductNames] = useState([]);
    const [selectedCategoryNames, setSelectedCategoryNames] = useState([]);
    const [selectedDate, setSelectedDate] = useState("");
    const [selectedDateRange, setSelectedDateRange] = useState([null, null]);


    useEffect(() => {
        productsService
          .getAllProducts()
          .then((response) => {
            const formattedProducts = response.data.map(product => ({
                id: product.productId,
                name: product.name,
                unitPrice: product.unitPrice,
                description: product.description,
                stock: product.stock,
                category: product.category,
                unitCost: product.unitCost
            }));
            setProducts(formattedProducts);
            const productNames = formattedProducts.map(product => product.name);
            setOptions(prevOptions => ({
              ...prevOptions,
              products: productNames
            }));
          })
          .catch((error) => {
            console.error("Error al mostrar los productos", error);
          });
    }, []);

    useEffect(() => {
        categoryService
          .getAllCategories()
          .then((response) => {
            const formattedCategories = response.data.map(category => ({
                id: category.categoryId,
                name: category.name,
            }));
            setCategories(formattedCategories);
            const categoryNames = formattedCategories.map(category => category.name);
            setCatOptions(prevOptions => ({
              ...prevOptions,
              categories: categoryNames
            }));
          })
          .catch((error) => {
            console.error("Error al mostrar las categorias", error);
          });
    }, []);

    useEffect(() => {
        const stockData = {};
        products.forEach(product => {
            stockData[product.name] = product.stock;
        });
        setProductStocks(stockData);
    }, [products]);
    
    const addField = () => {
        let object = {
            product: '', 
            amount: ''
        }
        setFormField([...formField, object])
    }


    const orderSubmit = (data) => {

        const startFormattedDate = dayjs(selectedDateRange[0]).format('YYYY-MM-DDTHH:mm:ss.SSSZ');
        const endFormattedDate = dayjs(selectedDateRange[1]).format('YYYY-MM-DDTHH:mm:ss.SSSZ');

        formField.forEach((form, index) => {
            const goalType = watch(`options-${index}`);
            const selectedProduct = selectedProducts[`product-${index}`];
            const selectedCategory = selectedCategories[`category-${index}`];
            const amount = parseInt(watch(`amount-${index}`)) || 0;

            let goalObject = "";
            if (goalType === "Product") {
                goalObject = selectedProduct;
            } else if (goalType === "Category") {
                goalObject = selectedCategory;
            }

            const goalData = {
                name: data[`name-${index}`],
                startingDate: startFormattedDate,
                endingDate: endFormattedDate,
                objectType: goalType,
                goalObject: goalObject,
                goalObjective: amount,
            };

            goalsService.addGoal(goalData)
                .then(response => {
                    console.log("Orden agregada con éxito:", response.data);
                    onCancel();
                })
                .catch(error => {
                    console.error("Error al agregar la orden:", error);
                });

            console.log(goalData);
        });
    };


    const handleCancelClick = () => {
        onCancel();
    };



    const handleDateChange = (newDateRange) => {
        setSelectedDateRange(newDateRange);
    };

    const totalPrice = formField.reduce((total, form, index) => {
        const productName = watch(`product-${index}`);
        const product = products.find(product => product.name === productName);
        const amount = parseInt(watch(`amount-${index}`)) || 0;
        return total + (product?.unitPrice || 0) * amount;
    }, 0);

  

    return(
        <>
            <Form onSubmit={ handleSubmit(orderSubmit) } className="form-react">

                {formField.map((form, index) => {
                    return(
                        <ProductContainer key={index}>

                            <div className="form-name">
                                <Label>Name</Label>
                                <Input
                                name={`name-${index}`}
                                type="text"
                                {...register(`name-${index}`, {
                                    required: true,
                                })}
                                />
                                {errors[`name-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                            </div>

                            <div className="form-types">
                                <div className="form-type">
                                    <Label>Type</Label>
                                    <Controller
                                        name={`options-${index}`}
                                        control={control}
                                        defaultValue=""
                                        {...register(`options-${index}`, { required: true })}
                                        render={({ field }) => (
                                            <Select
                                                {...field}
                                                onChange={(e) => {
                                                    const selectedOption = e.target.value;
                                                    setSelectedType(selectedOption);
                                                    field.onChange(selectedOption);
                                                }}
                                            >
                                                <option value="" disabled></option>
                                                <option value="Product">Product</option>
                                                <option value="Category">Category</option>
                                                <option value="Total">Total</option>
                                            </Select>
                                        )}
                                    />
                                    {errors[`options-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                                </div>

                                {selectedType === 'Product' && (
                                    <div className="form-product">
                                        <Label>Product</Label>

                                        <Controller
                                        name={`product-${index}`}
                                        control={control}
                                        defaultValue=""
                                        {...register(`product-${index}`, { required: true })}
                                        render={({ field }) => (
                                            <Select
                                                {...field}
                                                onChange={(e) => {
                                                    const selectedProduct = e.target.value;
                                                    setSelectedProductNames(prevSelectedProductNames => (
                                                        prevSelectedProductNames.includes(selectedProduct)
                                                            ? prevSelectedProductNames
                                                            : [...prevSelectedProductNames, selectedProduct]
                                                    ));
                                                    setSelectedProducts(prevState => ({
                                                        ...prevState,
                                                        [field.name]: selectedProduct
                                                    }));
                                                    field.onChange(selectedProduct);
                                                }}
                                            >
                                                <option value="" disabled></option>
                                                {options.products.map(product => (
                                                    <option
                                                        key={product}
                                                        value={product}
                                                        disabled={selectedProductNames.includes(product)}
                                                    >
                                                        {product}
                                                    </option>
                                                ))}
                                            </Select>
                                        )}
                                        />
                                        {errors[`product-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                                    </div>
                                )}

                                {selectedType === 'Category' && (
                                    <div className="form-category">
                                        <Label>Category</Label>

                                        <Controller
                                        name={`category-${index}`}
                                        control={control}
                                        defaultValue=""
                                        {...register(`category-${index}`, { required: true })}
                                        render={({ field }) => (
                                            <Select
                                                {...field}
                                                onChange={(e) => {
                                                    const selectedCategory = e.target.value;
                                                    setSelectedCategoryNames(prevSelectedCategoryNames => (
                                                        prevSelectedCategoryNames.includes(selectedCategory)
                                                            ? prevSelectedCategoryNames
                                                            : [...prevSelectedCategoryNames, selectedCategory]
                                                    ));
                                                    setSelectedCategories(prevState => ({
                                                        ...prevState,
                                                        [field.name]: selectedCategory
                                                    }));
                                                    field.onChange(selectedCategory);
                                                }}
                                            >
                                                <option value="" disabled></option>
                                                {catOptions.categories.map(category => (
                                                    <option
                                                        key={category}
                                                        value={category}
                                                        disabled={selectedCategoryNames.includes(category)}
                                                    >
                                                        {category}
                                                    </option>
                                                ))}
                                            </Select>
                                        )}
                                        />
                                        {errors[`category-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                                    </div>
                                )}

                            </div>

                            <div className="form-dates">
                                <Label>Dates</Label>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DemoContainer components={['DateRangePicker']}>
                                        <CustomizedDateTimePicker
                                        localeText={{ start: 'Start', end: 'End' }}
                                        value={selectedDateRange}
                                        format="YYYY-MM-DD"
                                        onChange={handleDateChange}
                                        
                                        />
                                    </DemoContainer>
                                </LocalizationProvider>
                            </div>

                            <div className="form-amount">
                                <Label>Amount</Label>
                                <Input
                                name={`amount-${index}`}
                                type="number"
                                {...register(`amount-${index}`, {
                                    required: true,
                                    min: 0,
                                })}
                                />
                                {errors[`amount-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}

                                {errors[`amount-${index}`]?.type === 'min' && <small className="fail">Min: 0</small>}
                            </div>

                            {/* <RemoveIcon onClick={() => removeField(index)}/> */}

                        </ProductContainer>
                    )
                })}
        
                <Buttons>
                    <Button type="submit" className="placeorder">Set Goal</Button>
                    <Button type="submit" className="cancel" onClick={handleCancelClick}>Cancel</Button>
                </Buttons>

            </Form>
        </>
    )

}

export default GoalForm;