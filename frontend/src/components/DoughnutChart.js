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

    const sortedData = Object.entries(data).sort((a, b) => b[1] - a[1]);
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
                size: 12
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

    return <Doughnut data={midata} options={misoptions} />;
}
