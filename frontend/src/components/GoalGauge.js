import { Doughnut } from "react-chartjs-2";
import { Chart, ArcElement } from "chart.js";

Chart.register(ArcElement);


const Gauge = ({ data }) => {

    let name = data.Name
    let percentage = isNaN(parseInt((data.Current / data.Goal) * 100)) ? 0 : parseInt((data.Current / data.Goal) * 100);
    percentage = `${percentage}% completed`;

    if (name === undefined) {
        name = "";
        percentage = "";
    }

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
                        text: [name, "", percentage],
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

        </div>
    );
};

export default Gauge;