import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Line, Bar } from 'react-chartjs-2';

const Dashboard = () => {
    const [totalProducts, setTotalProducts] = useState(0);
    const [totalOrders, setTotalOrders] = useState(0);
    const [totalCustomers, setTotalCustomers] = useState(0);

    const [productsChartData, setProductsChartData] = useState(null);
    const [ordersChartData, setOrdersChartData] = useState(null);


    

    useEffect(() => {
        // http://localhost:8082/api/public/${productId}
        try {
            axios.get(`http://localhost:8082/api/admin/NumberOfOrders`)
                .then(response => setTotalOrders(response.data));
    
            axios.get(`http://localhost:8082/api/admin/totalUser`)
                .then(response => setTotalCustomers(response.data));
    
            axios.get(`http://localhost:8082/api/admin/NumberOfProducts`)
                .then(response => setTotalProducts(response.data));
    
        } catch (error) {
            console.error('Error fetching product details', error);
        };
    }, []);

    //useEffect(() => {



    //     const fetchDashboardData = async () => {
    //         try {
    //             const productsResponse = await axios.get('http://localhost:8082/api/admin/NumberOfProducts');
    //             const ordersResponse = await axios.get('http://localhost:8082/api/admin/NumberOfOrders');
    //             const customersResponse = await axios.get('http://localhost:8082/api/admin/totalUser');

    //             setTotalProducts(productsResponse.data.totalProducts);
    //             setTotalOrders(ordersResponse.data.totalOrders);
    //             setTotalCustomers(customersResponse.data.totalCustomers);

                

    //             const productsChartData = {
    //                 labels: productsResponse.data.labels,
    //                 datasets: [
    //                     {
    //                         label: 'Products',
    //                         data: productsResponse.data.data,
    //                         fill: false,
    //                         borderColor: 'rgba(75,192,192,1)',
    //                     },
    //                 ],
    //             };

    //             const ordersChartData = {
    //                 labels: ordersResponse.data.labels,
    //                 datasets: [
    //                     {
    //                         label: 'Orders',
    //                         data: ordersResponse.data.data,
    //                         backgroundColor: 'rgba(75,192,192,0.2)',
    //                         borderColor: 'rgba(75,192,192,1)',
    //                         borderWidth: 1,
    //                     },
    //                 ],
    //             };

    //             setProductsChartData(productsChartData);
    //             setOrdersChartData(ordersChartData);
    //         } catch (error) {
    //             console.error('Error fetching dashboard data:', error.message);
    //         }
    //     };

    //     fetchDashboardData();
    // }, []);

    return (
        <div className="flex flex-wrap w-full">

            <div className="w-full md:w-1/2 lg:w-1/4 p-4">
                <div className="bg-blue-500 text-white p-4 rounded">
                    <h3 className="text-xl font-bold mb-2">Total Products</h3>
                    <p className="text-2xl">{totalProducts}</p>
                </div>
            </div>

            <div className="w-full md:w-1/2 lg:w-1/4 p-4">
                <div className="bg-green-500 text-white p-4 rounded">
                    <h3 className="text-xl font-bold mb-2">Total Orders</h3>
                    <p className="text-2xl">{totalOrders}</p>
                </div>
            </div>

            <div className="w-full md:w-1/2 lg:w-1/4 p-4">
                <div className="bg-yellow-500 text-white p-4 rounded">
                    <h3 className="text-xl font-bold mb-2">Total Customers</h3>
                    <p className="text-2xl">{totalCustomers}</p>
                </div>
            </div>

            <div className="w-full md:w-1/2 lg:w-1/4 p-4">
                <div className="bg-purple-500 text-white p-4 rounded">
                    <h3 className="text-xl font-bold mb-2">Chart - Products Over Time</h3>
                    {productsChartData ? (
                        <Line data={productsChartData} />
                    ) : (
                        <p>No data available for products chart.</p>
                    )}
                </div>
            </div>

            <div className="w-full md:w-1/2 lg:w-1/4 p-4">
                <div className="bg-orange-500 text-white p-4 rounded">
                    <h3 className="text-xl font-bold mb-2">Chart - Orders Over Time</h3>
                    {ordersChartData ? (
                        <Bar data={ordersChartData} />
                    ) : (
                        <p>No data available for orders chart.</p>
                    )}
                </div>
            </div>

        </div>
    );
};

export default Dashboard;
