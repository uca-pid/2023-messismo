import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
    Filler,
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
    Filler
);

export default function Bars({ data, label, max }) {

    const sortedData = Object.entries(data).sort(([a], [b]) => a - b);
    const labels = sortedData.map(([key]) => key);
    const values = sortedData.map(([_, value]) => value);

    var misoptions = {
        responsive: true,
        animation: false,
        plugins: {
            legend: {
                display: true
            }
        },
        scales: {
            y: {
                min: 0,
                max: max
            },
            x: {
                ticks: { color: 'rgba(0, 220, 195)' }
            }
        }
    };

    var midata = {
        labels: labels,
        datasets: [
            {
                label: label,
                data: values,
                backgroundColor: 'rgba(0, 220, 195, 0.5)'
            }
        ]
    };

    return <Bar data={midata} options={misoptions} />;
}
