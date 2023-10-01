import React, { useState, useEffect } from "react";
import { useForm, Controller } from 'react-hook-form'
import styled from "styled-components";
import { GrAddCircle } from 'react-icons/gr'
import { RiDeleteBinLine } from 'react-icons/ri'

const Form = styled.form`
    padding: 2rem;
    background-color: rgb(164, 212, 204, 0.6);

    .fail {
        color: red;
    }

    .form-paymentmethod{

    }

`;

const ProductContainer = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 2rem;

    .form-product{
        margin-right: 1rem;
        width: 84%;
    }

    .form-amount{
        width: 16%;
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
`;


const options = {
    products: ['chocolate', 'vanilla', 'strawberry'],
    paymentMethods: ['cash', 'credit card', 'debit card'],
};

const OrderForm = ({onCancel}) => {

    const { control, register, handleSubmit, formState: {errors} } = useForm()
    const stock = 50

    const [formField, setFormField] = useState([
        { product: '', amount: '' }
    ]);

    const addField = () => {
        let object = {
            product: '', 
            amount: ''
        }
        setFormField([...formField, object])
    }

    // const removeField = (index) => {
    //     let data = [...formField];
    //     data.splice(index, 1);
    //     setFormField(data);
    // };

    const orderSubmit = (data) => {
        console.log(data)
    }

    const handleCancelClick = () => {
        onCancel();
    };

    return(
        <>
            <Form onSubmit={ handleSubmit(orderSubmit) } className="form-react">

                {formField.map((form, index) => {
                    return(
                        <ProductContainer key={index}>

                            <div className="form-product">
                                <Label>Product</Label>
                                <Controller
                                    name="product"
                                    control={control}
                                    defaultValue=""
                                    {...register(`product-${index}`, { required: true })}
                                    render={({ field }) => (
                                        <Select {...field}>
                                            <option value="" disabled></option>
                                            {options.products.map(product => (
                                                <option key={product} value={product}>
                                                    {product}
                                                </option>
                                            ))}
                                        </Select>
                                    )}
                                />
                                {errors[`product-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                            </div>

                            <div className="form-amount">
                                <Label>Amount</Label>
                                <Input name="amount" type="number" {...register(`amount-${index}`, { required: true, min: 1, max: stock })} />
                                {errors[`amount-${index}`]?.type === 'required' && <small className="fail">Field is empty</small>}
                                {errors[`amount-${index}`]?.type === 'min' && <small className="fail">The minimum amount is 1</small>}
                                {errors[`amount-${index}`]?.type === 'max' && <small className="fail">The maximum amount is {stock}</small>}
                            </div>

                            {/* <RemoveIcon onClick={() => removeField(index)}/> */}

                        </ProductContainer>
                    )
                })}

                <AddIcon onClick={addField} />

                <div className="form-paymentmethod">
                    <Label>Payment Method</Label>
                    <Controller
                        control={control}
                        defaultValue=""
                        {...register('paymentmethod', { required: true })}
                        render={({ field }) => (
                            <Select {...field}>
                                <option value="" disabled></option>
                                {options.paymentMethods.map(method => (
                                    <option key={method} value={method}>
                                        {method}
                                    </option>
                                ))}
                            </Select>
                        )}
                    />
                    {errors.paymentmethod?.type === 'required' && <small className="fail">Field is empty</small>}
                </div>
        
                <Buttons>
                    <Button type="submit" className="placeorder">Place Order</Button>
                    <Button type="submit" className="cancel" onClick={handleCancelClick}>Cancel</Button>
                </Buttons>

            </Form>
        </>
    )

}

export default OrderForm;