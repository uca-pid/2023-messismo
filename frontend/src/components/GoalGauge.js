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
        <div>
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
                        size: 12
                    }
                   }
                
            },
            rotation: -90,
            circumference: 180,
            cutout: "60%",
            maintainAspectRatio: true,
            responsive: true
            }}
        />
        <div
            style={{
            position: "absolute",
            top: "55%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            textAlign: "center"
            }}
        >
            <div>Text Here</div>
        </div>
        </div>
    );
};

export default Gauge;