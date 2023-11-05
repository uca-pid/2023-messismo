import { Doughnut } from "react-chartjs-2";
import { Chart, ArcElement } from "chart.js";

Chart.register(ArcElement);


const Gauge = ({ data }) => {

    const info = {
        datasets: [
            {
            data: [data.Current, data.tempGoal],
            backgroundColor: [
                'rgba(159,193,108)',
                'rgb(157,187,191,0)',
            ],
            display: true,
            borderColor: "#D1D6DC"
            }
        ]
        };

    return (
        <div style={{ display: 'flex', flexDirection: 'row', textAlign: "center" }}>
            <Doughnut
                data={info}
                options={{
                plugins: {
                    legend: {
                    display: false
                    },
                    tooltip: {
                    enabled: false
                    },
                    title: {
                        display: true,
                        text: data.Name,
                        color: 'white',
                        font: {
                            size: 16
                        }
                    },
                    
                },
                rotation: -90,
                circumference: 180,
                cutout: "60%",
                maintainAspectRatio: true,
                responsive: true
                }}
            />

            <div style={{
            color: "white",
            textAlign: "center",
            fontSize: 20,
            display: isNaN(parseInt(data.Current / data.Goal * 100)) ? "none" : "block"
            }}>
            {isNaN(parseInt(data.Current / data.Goal * 100)) ? "" : `${parseInt(data.Current / data.Goal * 100)}%`}
            </div>


        </div>
    );
};

export default Gauge;