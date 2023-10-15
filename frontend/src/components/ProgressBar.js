import React from 'react'

const ProgressBar = ({bgcolor,name,progress,sliderValue }) => { 

    const containerStyle = {
        display: 'flex',
        alignItems: 'center',
    }
	
	const Parentdiv = { 
		height: "1rem", 
		width: '100%', 
		backgroundColor: bgcolor === '#8CB9B1' ? 'whitesmoke' : 'red',
		borderRadius: 40, 
	} 
	
	const Childdiv = { 
		height: '100%', 
		width: `${(progress / sliderValue) * 100}%`,
		backgroundColor: bgcolor, 
	    borderRadius:40, 
	} 
	
	const productname = { 
		color: 'white', 
		fontWeight: 900,
        fontSize:'1.4rem'
	} 

    const productstock = { 
		color: 'white', 
		fontWeight: 900,
        fontSize:'1.2rem',
        marginLeft:'1rem'
	} 
		
	return ( 
        <div style={{marginTop: '1rem'}}>
            <span style={productname}>{`${name}`}</span>
            <div style={containerStyle}>
                <div style={Parentdiv}>
                    <div style={Childdiv}></div>
                </div>
                <span style={productstock}>{`${progress}`}</span>
            </div>
        </div>
	) 
} 

export default ProgressBar; 
