import React from 'react';
import { Doughnut } from 'react-chartjs-2';
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
    ArcElement
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    BarElement,
    ArcElement,
    Title,
    Tooltip,
    Legend,
    Filler
);

export default function Doughnuts({ data, label }) {

    const sortedData = Object.entries(data)
  .filter(([_, value]) => value !== 0) 
  .sort((a, b) => b[1] - a[1]);
    const first5 = sortedData.slice(0, 5);
    const labels = first5.map(([key]) => key);
    const values = first5.map(([_, value]) => value);
   

    var misoptions = {
      responsive: true,
      animation: false,
      plugins: {
          legend: {
              position: 'bottom',
              display: true,
              labels: {
                  color: 'white'
              }
          },
          title: {
            display: true,
            text: label,
            color: 'white',
            font: {
                size: 16
            }
           }
      },
      
    };

    var midata = {
        labels: labels,
        datasets: [
            {
                label: label,
                data: values,
                backgroundColor: [
                  'rgba(212,150,187)',
                  'rgba(159,193,108)',
                  'rgba(164,200,233)',
                  'rgba(239,202,102)',
                  'rgba(181,164,227)',
                ],
                borderColor: [
                    'rgba(212,150,187)',
                    'rgba(159,193,108)',
                    'rgba(164,200,233)',
                    'rgba(239,202,102)',
                    'rgba(181,164,227)',
                ],
                borderWidth: 1,
            }
        ]
    };

    const plugins = [
        {
          afterDraw: function (chart) {
            console.log(chart.data.datasets[0].data)
            const allZeros = chart.data.datasets[0].data.every(value => value === 0);
            if (chart.data.datasets[0].data.length < 1 || allZeros) {
              let ctx = chart.ctx;
              let width = chart.width;
              let height = chart.height;
              ctx.fillStyle = 'white';
              ctx.textAlign = "center";
              ctx.textBaseline = "middle";
              ctx.font = "25px Arial";
              ctx.fillText("No data to display", width / 2, height / 2);
              ctx.restore();
            }
          },
        },
      ];

    return <Doughnut data={midata} options={misoptions} plugins={plugins}/>;
}
